package com.github.sellee.spring.mapper;

import java.util.Map;

import com.github.sellee.tinyQryMaker.TinyQryMaker;

public class TQMBuilder {

	public String selQry1(Map param) {

		TinyQryMaker tqm = new TinyQryMaker(param, (String) param.get("sql"));

		return tqm.getQry();
	}
}
