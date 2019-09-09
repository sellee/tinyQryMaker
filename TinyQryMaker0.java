package com.github.sellee.tinyQryMaker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TinyQryMaker0 extends TinyQryMakerz {

	protected void txtRead(String fnm) throws Exception {
		nowLno = 0;
		String s;
		//csp String[] lines = System.IO.File.ReadAllLines(fnm);
		BufferedReader BFR = new BufferedReader(new InputStreamReader(new FileInputStream(fnm), "UTF8"));

		try {
			while ((s = BFR.readLine()) != null) {
				nowLno++;
				add2List(s + "\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			BFR.close();
		}
		if (nowLno >= 1000)
			df = df4;
		else if (nowLno >= 100)
			df = df3;
		else if (nowLno >= 10)
			df = df2;
		else
			df = df1;

		// verify control structure
		String ctrl = null;
		for (int lp1 = arLn.size() - 1; lp1 >= 0; lp1--) {
			ctrl = arCtrl.get(lp1);
			if (ctrl == null)
				continue;
			else {
				if (!ctrl.equals("ed"))
					throw new Exception("control structure not end with ##} [line:" + nowLno + "]");
				break;
			}
		}

		if (!stkb.isEmpty()) //
			throw new Exception("no [ ##} ] end of file");
	}

	private void dbgSplt(String s) {
		//System.out.println(nowLno + s.replaceAll("\\\n", "") + "]");
	}

	private static int idxOf(String ln, String s) {
		if (ln == null)
			return mxLen;
		int n = ln.indexOf(s);
		return n == -1 ? mxLen : n;
	}

	private int[] getKeyW(String ln) throws Exception {
		String lw = ln.toLowerCase();
		int[] ii = new int[3];
		int nc1 = 0, nc2 = 0, nc3 = 0;
		if ((nc1 = idxOf(lw, "##if")) == mxLen && (nc2 = idxOf(lw, "##else")) == mxLen && (nc3 = idxOf(lw, "##}")) == mxLen)
			return null;

		ii[0] = nc1;
		ii[1] = nc2;
		ii[2] = nc3;
		return ii;
	}

	private static String spt0 = "##(if.*\\{|else|\\})";
	private static String spt1 = "/\\*.*?" + spt0 + ".*?\\*/";
	private static String spt2 = "--.*?" + spt0 + ".*"; // -- ##

	private static Pattern ptnC1 = Pattern.compile(spt1);
	private static Pattern ptnC2 = Pattern.compile(spt2);

	private String[] lnSplit(String ln) throws Exception {
		String lw = ln.toLowerCase();

		int nCmt = mxLen;

		String cmt = null;
		{
			String cm1 = null, cm2 = null;
			Matcher m1 = ptnC1.matcher(lw);
			int cmx1 = mxLen, cmx2 = mxLen;
			if (m1.find()) {
				cmt = cm1 = m1.group();
				cmx1 = idxOf(lw, cm1);
			}
			Matcher m2 = ptnC2.matcher(lw);
			if (m2.find()) {
				cm2 = m2.group();
				cmx2 = idxOf(lw, cm2);
				if (cmx2 < cmx1) {
					cmt = cm2;
				}
			}
			nCmt = Math.min(cmx1, cmx2);
			if (nCmt == mxLen)
				return null;
			cmt = ln.substring(nCmt, nCmt + cmt.length());
		}

		int[] kIdx = getKeyW(cmt);
		String[] expAr = new String[3];
		expAr[0] = ln.substring(0, nCmt);
		expAr[2] = ln.substring(nCmt + cmt.length());

		String ctrl = cmt.substring(cmt.indexOf("##"));
		dbgSplt("=[" + ln);
		//dbgSplt("-[" + cmt + "]		=========>[" + ctrl);
		if (getKeyW(ctrl.substring(3)) != null)
			throw new Exception("duplicated KEYword [line:" + nowLno + "]");
		try {
			if (kIdx[0] != mxLen) {
				if (ctrl.indexOf('{') != ctrl.lastIndexOf('{'))
					throw new Exception("multi braket '{' [line:" + nowLno + "]");
				//expAr[1] = getCondi(ln.substring(nCmt + kIdx[0], nCmt + cmt.length()));
				expAr[1] = getCondIf(ctrl);
				stkb.push(false);
			} else if (kIdx[1] != mxLen) {
				expAr[1] = "el";
				stkb.push(!stkb.pop());
			} else if (kIdx[2] != mxLen) {
				expAr[1] = "ed";
				stkb.pop();
			}
		} catch (java.util.EmptyStackException e) {
			throw new Exception("if begin end count not match [line:" + nowLno + "]");
		}
		dbgSplt("<[" + expAr[0]);
		dbgSplt("o[" + expAr[1]);
		dbgSplt(">[" + expAr[2]);
		dbgSplt("------------------------------------------------------------");

		return expAr;
	}

	private void add2List(String ln) throws Exception {
		String[] arIf = lnSplit(ln);
		if (arIf == null && ln.length() > 0) {
			arCtrl.add(null);
			arNo.add(nowLno);
			arLn.add(ln);
		} else {
			if (arIf[0].length() > 0) {
				arCtrl.add(null);
				arNo.add(nowLno);
				arLn.add(arIf[0]);
			}
			arCtrl.add(arIf[1]);
			arNo.add(nowLno);
			arLn.add(null);
			if (arIf[2].length() > 0) {
				add2List(arIf[2]);
			}
		}
	}

	private String getCondIf(String ln) throws Exception {
		String expIf = ln.substring(ln.indexOf("##") + 4, ln.indexOf('{')).trim();
		if (expIf.replaceAll("\\(", "").replaceAll("\\)", "").trim().length() == 0)
			throw new Exception("if condition empty [line:" + nowLno + "]");

		int qcnt = 0;
		Stack<Boolean> lnstk = new Stack<Boolean>();
		char ch = ' ';
		try {
			for (int i = 0; i < expIf.length(); i++) {
				ch = expIf.charAt(i);
				if (ch == '(')
					lnstk.push(true);
				if (ch == ')')
					lnstk.pop();
				if(ch=='\'')
					qcnt++;
			}
		} catch (java.util.EmptyStackException e) {
			throw new Exception("if parentheses  not match [line:" + nowLno + "]");
		}
		if (!lnstk.isEmpty())
			throw new Exception("if parentheses  not match [line:" + nowLno + "]");
		if(qcnt %2 !=0)
			throw new Exception("sinle quotation  not match [line:" + nowLno + "]");
		
		return expIf.replaceAll(",", " ");
	}
}
