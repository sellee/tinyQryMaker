package com.github.sellee.tinyQryMaker;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TQM_oper {

	private static Pattern ptnArlst = Pattern.compile("\\[\\s*[0-9]*\\s*\\]");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static String mapGet(String key, Map<String, Object> pm) throws Exception {
		int arIdx = 0;
		String[] ks = key.split("\\.");
		if (ks.length > 2)
			throw new Exception("variable depth greater then 2");

		Object ob1 = null;
		arIdx = ks[0].indexOf("[");
		if (arIdx > 0) {
			Matcher m1 = ptnArlst.matcher(ks[0]);
			if (m1.find()) {
				ob1 = pm.get(ks[0].substring(0, arIdx));
				arIdx = Integer.parseInt(m1.group().replaceAll("(\\[|\\])", "").trim());
			} else {
				throw new Exception("bracket[] not match");
			}
		} else {
			ob1 = pm.get(ks[0]);
		}

		if (ob1 instanceof java.util.Map) {
			java.util.Map hs = ((java.util.Map) ob1);
			int sz = hs.size();
			if (sz == 0)
				return "";

			String v = "" + hs.get(ks[1]);
			if (v.trim().length() == 0 || "null".equals(v))
				return "";
			return v;
		} else if (ob1 instanceof List) {
			List lst = ((List) ob1);
			int sz = lst.size();
			if (sz == 0 || arIdx >= sz)
				return "";
			if (arIdx == -1) {
				if (ks.length > 1)
					throw new Exception("list[n] ,n missing");
				return TinyQryMaker1.strue;
			}

			Object ob2 = lst.get(arIdx);
			if (ob2 instanceof java.util.Map) { // lst[n] == map
				return (mapGet(ks[1], (Map<String, Object>) ob2));
			} else {// lst[n] == String
				String v = "" + ob2;
				if (v.trim().length() == 0 || "null".equals(v))
					return "";
				return v;
			}
		} else {
			if (ob1 == null)
				return "";
			String v = "" + ob1;
			if (v.trim().length() == 0)
				return "";
			return v;
		}
	}
}
