package com.github.sellee.spring.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

public interface TQMMapper {
	@SelectProvider(type = TQMBuilder.class, method = "selQry1")
	public List<Map<String, Object>> select1(Map<String, Object> param); // sql-TQM.xml
}
