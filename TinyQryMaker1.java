package com.github.sellee.tinyQryMaker;

public class TinyQryMaker1 extends TinyQryMaker0 {

	private static String strue = "true";
	private static String sfalse = "false";

	private void arShift(String[] oar, int n, int rpt) {
		for (int i = n; i < oar.length; i++) {
			oar[i - rpt] = oar[i];
		}
		oar[oar.length - (rpt)] = null;
	}

	private void opEval_Comparison(String[] oar, int n) throws Exception {
		//dbgOper("\t[" + ar2s(oar) + "]");
		if (n + 1 >= oar.length) {
			throw new Exception(oar.length + "[" + oar[n] + "] needs operand [line:" + nowLno + "]");
		}
		String cmpop = oar[n], w1 = oar[n - 1], w2 = oar[n + 1];
		if (isOper_any(w1) || w2 == null || isOper_any(w2)) {
			throw new Exception("[" + cmpop + "] needs operand [line:" + nowLno + "]");
		}
		for (int i = -1; i <= 1; i++) {
			if (i == 0)
				continue;
			String ww = i == -1 ? w1 : w2;
			if (ww.charAt(0) == '\'' || ww.charAt(ww.length() - 1) == '\'') {
				if (ww.charAt(0) != '\'' || ww.charAt(ww.length() - 1) != '\'') {
					throw new Exception("quot not match[" + ww + "]");
				} else {
					oar[n + i] = ww.substring(1, ww.length() - 2);
				}
			} else if (mm.containsKey(ww)) {
				oar[n + i] = mmS(ww);
			}
		}
		//dbgOper("\t\t[" + ar2s(oar) + "]");
		w1 = oar[n - 1];
		w2 = oar[n + 1];

		if (cmpop.equals("=") || cmpop.equals("==")) {
			oar[n - 1] = w1.equals(w2) ? strue : sfalse;
		} else if (cmpop.equals("!=") || cmpop.equals("<>")) {
			oar[n - 1] = !w1.equals(w2) ? strue : sfalse;
		} else {
			try {
				double d1 = Double.parseDouble(w1);
				double d2 = Double.parseDouble(w2);
				if (cmpop.equals("<"))
					oar[n - 1] = (d1 < d2) ? strue : sfalse;
				else if (cmpop.equals(">"))
					oar[n - 1] = (d1 > d2) ? strue : sfalse;
				else if (cmpop.equals("<="))
					oar[n - 1] = (d1 <= d2) ? strue : sfalse;
				else if (cmpop.equals(">="))
					oar[n - 1] = (d1 >= d2) ? strue : sfalse;
			} catch (Exception e) {
				if (cmpop.equals("<"))
					oar[n - 1] = (w1.compareTo(w2) < 0) ? strue : sfalse;
				else if (cmpop.equals(">"))
					oar[n - 1] = (w1.compareTo(w2) > 0) ? strue : sfalse;
				else if (cmpop.equals("<="))
					oar[n - 1] = (w1.compareTo(w2) < 0 || w1.equals(w2)) ? strue : sfalse;
				else if (cmpop.equals(">="))
					oar[n - 1] = (w1.compareTo(w2) > 0 || w1.equals(w2)) ? strue : sfalse;
			}
		}
		arShift(oar, n + 2, 2);
		dbgOper("\t[" + ar2s(oar) + "]");
	}

	private void dbgOper(String s) {
		//System.err.println(nowLno + "[" + s + "]");
	}

	private void opEval_Bool(String[] oar, int n) throws Exception {
		if (n + 1 >= oar.length) {
			throw new Exception(oar.length + "[" + oar[n] + "] needs operand [line:" + nowLno + "]");
		}
		if (oar[n].equals("not")) {
			if (oar[n + 1] == null || isOper_any(oar[n + 1])) {
				throw new Exception(oar.length + "[" + oar[n] + "] needs operand [line:" + nowLno + "]");
			}
			oar[n] = !inmm(oar[n + 1]) ? strue : sfalse;
			arShift(oar, n + 2, 1);
		} else {
			if (isOper_any(oar[n - 1]) || isOper_any(oar[n + 1])) {
				throw new Exception("[" + oar[n] + "] needs operand [line:" + nowLno + "]");
			}
			if (oar[n].equals("or")) {
				oar[n - 1] = inmm(oar[n - 1]) || inmm(oar[n + 1]) ? strue : sfalse;
			} else {
				oar[n - 1] = inmm(oar[n - 1]) && inmm(oar[n + 1]) ? strue : sfalse;
			}
			arShift(oar, n + 2, 2);
		}
	}

	private boolean isOper_any(String opr) {
		return isOper_Comparison(opr) || isOper_Bool(opr);
	}

	private boolean isOper_Comparison(String opr) {
		return (opr.equals("=") || opr.equals("==") || opr.equals("!=") || opr.equals("<>") || opr.equals("<") || opr.equals(">")
				|| opr.equals("<=") || opr.equals(">=")) ? true : false;
	}

	private boolean isOper_Bool(String opr) {
		return isOper_not(opr) || (opr.equals("or") || opr.equals("and")) ? true : false;
	}

	private boolean isOper_not(String opr) {
		return (opr.equals("not")) ? true : false;
	}

	private boolean inmm(String key) {
		if (key == null)
			return true;
		String k = key.trim();
		if (k.length() == 0)
			return false;
		if (k.equals(strue))
			return true;
		if (k.equals(sfalse))
			return false;
		String v = mm.containsKey(k) ? mmS(k) : null;
		if (v == null || v.trim().length() == 0)
			return false;
		return true;
	}

	private String mmS(String key) {
		String r = (String) mm.get(key);
		return r == null ? "" : r;
	}

	private String ar2s(String[] oar) {
		return ar2s(oar, 0, oar.length);
	}

	private String ar2s(String[] oar, int s) {
		return ar2s(oar, s, oar.length);
	}

	private String ar2s(String[] oar, int s, int e) {
		if (oar.length == 0 || s >= oar.length || s > e)
			return "";
		if (e >= oar.length)
			e = oar.length - 1;
		String ln = oar[s];
		if (ln == null)
			return "";

		for (int i = s + 1; i <= e; i++) {
			if (oar[i] == null)
				break;
			ln += " " + oar[i];
		}
		return ln.trim();
	}

	private String nowCondi = null;

	protected boolean parIfCond(String expIf0) throws Exception {
		String expIf = expIf0.trim();
		if (expIf.indexOf('(') > 0 || expIf.indexOf(')') > 0) {
			char c = ' ';
			int p1 = 0;
			for (int i = 0; i < expIf.length(); i++) {
				//csp c = expIf[i];
				c = expIf.charAt(i);
				if (c == '(') {
					p1 = i;
				}
				if (c == ')') {
					//csp String parnts = expIf.substring(p1 + 1, i - p1 - 1); // java from,to csp from,len = START, END - START
					//csp expIf = expIf.substring(0, p1) + par1(parnts) + expIf.substring(i + 1);
					String[] par = condi2ar(expIf.substring(p1 + 1, i));
					expIf = expIf0.substring(0, p1) + " " + (parsAr(par) ? strue : sfalse) + " " + expIf0.substring(i + 1);
					return parIfCond(expIf);
				}
			}
		} else {
			String[] par = condi2ar(expIf);
			return parsAr(par);
		}
		return true;
	}

	private boolean isTrue(String ww) throws Exception {
		boolean ret = true;
		if (isOper_any(ww)) {
			throw new Exception("[" + ww + "] needs operand [line:" + nowLno + "]");
		}
		if (ww.equals(strue))
			return true;
		if (ww.equals(sfalse))
			return false;

		ret = inmm(ww);
		return ret;
	}

	private boolean parsAr(String[] oar) throws Exception {
		nowCondi = ar2s(oar);
		boolean ret = true;
		if (oar.length == 1) {
			return isTrue(oar[0]);
		} else {
			if (oar[1] == null)
				return isTrue(oar[0]);

			boolean hasOp = false;
			for (int i = 0; i < oar.length; i++) {
				if (oar[i] == null)
					break;
				if (isOper_any(oar[i])) {
					hasOp = true;
					break;
				}
			}
			if (!hasOp)
				throw new Exception("needs operator [line:" + nowLno + "]");
		}

		for (int i = 0; i < oar.length; i++) {
			if (oar[i] == null)
				break;
			if (isOper_not(oar[i])) {
				opEval_Bool(oar, i);
				ret = parsAr(oar);
				return ret;
			}
		}

		for (int i = 0; i < oar.length; i++) {
			if (oar[i] == null)
				break;
			if (isOper_Comparison(oar[i])) {
				throw new Exception("now, Comparison operator not supported");
				//opEval_Comparison(oar, i);
				//ret = parsAr(oar);
				//return ret;
			} else if (isOper_Bool(oar[i])) {
				opEval_Bool(oar, i);
				ret = parsAr(oar);
				return ret;
			}
		}

		ret = oar[0].equals(strue) ? true : false;
		return ret;
	}

	private String[] condi2ar(String expIf) throws Exception {
		String exp = expIf.trim();
		if (exp.length() == 0) {
			throw new Exception("condition not exists");
		}
		//csp String[] oar = Regex.Split(exp, @"\s+");
		String[] oar = exp.split("\\s+");
		for (int i = 0; i < oar.length; i++) {
			String lw = oar[i].toLowerCase();
			if (isOper_any(lw))
				oar[i] = lw;
		}
		return oar;
	}
	/*
	protected String getQuto(String ln) throws Exception {
		int qcnt = 0;
		char ch = ' ';
		for (int i = 0; i < ln.length(); i++) {
			ch = ln.charAt(i);
			if (ch == '\'')
				qcnt++;
		}
		if (qcnt % 2 != 0)
			throw new Exception("sinle quotation  not match [line:" + nowLno + "]");
	
		return ln.replaceAll("''", "'");
	}
	 */
}
