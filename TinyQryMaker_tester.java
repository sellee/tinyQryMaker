package com.github.sellee.tinyQryMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TinyQryMaker_tester {
	public static void main(String[] args) {
		//main_ptn();
		main_q();
		//		listTest();
	}

	//private static String ln00 = " ddd -- /* fds  ##if {fds */ ---";
	private static String ln00 = " ''";
	private static String spt0 = "##(else\\s*|if\\s.*\\{|\\}\\s*)";
	private static String spt1 = "/\\*.*?" + spt0 + ".*?\\*/";
	private static String spt2 = "--.*?" + spt0 + ".*"; // -- ##

	private static String ssq = "'.*(['']).*?'"; // -- ##

	private static Pattern ptnC1 = Pattern.compile(spt1);
	private static Pattern ptnC2 = Pattern.compile(spt2);
	private static Pattern ptnSQ = Pattern.compile(ssq);

	public static void main_ptn() {
		String lw = ln00.toLowerCase();

		String cmt = null;
		{
			String cm1 = null, cm2 = null;
			Matcher m1 = ptnSQ.matcher(lw);
			int cmx1 = TinyQryMakerz.mxLen, cmx2 = TinyQryMakerz.mxLen;
			if (m1.find()) {
				cmt = cm1 = m1.group();
				System.err.println(cm1);
			}

			/*Matcher m2 = ptnC2.matcher(lw);
			if (m2.find()) {
				cm2 = m2.group();
				System.err.println(cm2);
				if (cmx2 < cmx1) {
					cmt = cm2;
				}
			}
			*/
		}

	}

	public static void listTest() {
		List<String> otxt = new ArrayList<String>();
		otxt.add("0");
		otxt.add("e");
		for (int i = 0; i < otxt.size(); i++) {
			if (i == 0)
				otxt.add(1, "1");
			System.err.println(i + "-" + otxt.get(i));
		}
	}

	public static void main_q() {
		//System.err.println(setQryDir());
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("T", "T");
		//TinyQryMaker qmk = new TinyQryMaker(map1, "zTest/a.sql");
		TinyQryMaker qmk = new TinyQryMaker(map1, "zTest/a_if.txt");
		String qry = qmk.getQry();
		System.err.println(qry);
		System.err.println("--------------------------------debugQry----------------------------------------");
		System.err.println(qmk.debugQry);
		if (qmk.error != null) {
			System.err.println("----------------------------------- E R R O R -------------------------------------");
			System.err.println(qmk.error);
		}
	}

}
