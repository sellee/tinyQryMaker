package com.github.sellee.tinyQryMaker;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

public class TinyQryMakerz {
	public String error = null;
	public String debugQry = "";

	protected static String qryDir;
	protected Map<String, Object> mm;

	protected int nowLno = 0;
	protected Stack<Boolean> stkb = new Stack<Boolean>();
	protected Stack<String> stks = new Stack<String>();
	protected List<String> arLn = new ArrayList<String>();
	protected List<String> arCtrl = new ArrayList<String>();
	protected List<Integer> arNo = new ArrayList<Integer>();
	protected String qry = "";

	protected DecimalFormat df;
	protected static DecimalFormat df1 = new DecimalFormat("0");
	protected static DecimalFormat df2 = new DecimalFormat("00");
	protected static DecimalFormat df3 = new DecimalFormat("000");
	protected static DecimalFormat df4 = new DecimalFormat("0000");

	protected String qryNm;

	final protected static int mxLen = 999999;

	protected static void dprintln(String s) {
		System.err.println(s);
	}

	protected static String setQryDir() {
		Properties properties = new Properties();
		try {
			String webInf = TinyQryMaker.class.getResource("/").getPath();
			properties.load(new FileInputStream(webInf +"/config/project.properties"));
			qryDir = properties.getProperty("tinyQryMaker_dir");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qryDir;
	}

	protected void debugQrya(boolean tf, String ctl, String s) {
		String sp = "";
		int sz = stkb.size();
		for (int i = 1; i < sz; i++) {
			sp += "\t";
		}
		String dd = null;
		if (ctl == null)
			dd = ("[" + df.format(nowLno) + (tf ? "o" : "x") + "]" + sp + s);
		else
			dd = ("[" + df.format(nowLno) + (tf ? "o:" : "x:") + ctl + "]");
		debugQry += dd;
	}

	protected boolean isStkT() {
		return stkb.isEmpty() || stkb.search(false) == -1 ? true : false;
	}
}
