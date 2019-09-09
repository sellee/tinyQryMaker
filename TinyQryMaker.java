package com.github.sellee.tinyQryMaker;

import java.util.HashMap;
import java.util.Map;

public class TinyQryMaker extends TinyQryMaker1 {

	// Comparison operator : = ,==,!= ,<> ,< , >
	// bool operator :or and not
	public TinyQryMaker(Map<String, Object> map1, String qryNm1) {
		if (map1 == null)
			mm = new HashMap<String, Object>();
		else
			mm = map1;
		qryNm = qryNm1;
		if (qryDir == null)
			setQryDir();
	}

	public String getQry() {
		try {
			return getSql();
		} catch (Exception e) {
			e.printStackTrace();
			//error = e.toString().replaceFirst("java.lang.Exception:", "");
			return "";
		}
	}

	protected String getSql() throws Exception {
		txtRead(qryDir + "/" + qryNm);
		boolean addLn = true;
		String ctl = null;
		String ln = null;

		for (int lp1 = 0; lp1 < arLn.size(); lp1++) {
			ln = arLn.get(lp1);
			nowLno = arNo.get(lp1);
			ctl = arCtrl.get(lp1);
			//System.out.print(ln+ "  ===>"+ctl);
			//System.out.println("[" + nowLno + "." + (cmd == null ? "  " : cmd) + "]" + ln00);

			if (ctl == null) {
				if (addLn) {
					qry += ln;
				}
			} else {
				if (ctl.equals("ed")) {
					stkb.pop();
					stks.pop();
					addLn = isStkT();
				} else if (ctl.equals("el")) {
					stkb.push(!stkb.pop());
					stks.peek();
					addLn = isStkT();
				} else {
					if (isStkT()) {
						stkb.push(parIfCond(ctl));
						stks.push(ctl);
						addLn = isStkT();
					} else {
						stkb.push(false);
						stks.push("[X]" + ctl);
						addLn = false;
					}
				}
			}
			debugQrya(addLn, ctl, ln);
		}
		arLn.clear();
		return qry;
	}
}
