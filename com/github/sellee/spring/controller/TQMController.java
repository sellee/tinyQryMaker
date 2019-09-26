package com.github.sellee.spring.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.sellee.spring.mapper.TQMMapper;
import com.lgchem.cm.common.service.CommonService;
import com.lgchem.cm.common.util.JSONUtils;

@Controller
public class TQMController {

	@Autowired
	private SqlSession sqlSession;

	@Resource(name = "commonService")
	private CommonService commonService;

	@RequestMapping("/common/comAjax.do")
	public Object comAjax(HttpServletRequest req, @RequestBody Map<String, Object> param, ModelMap model) throws Exception {
		Map<String, Object> rets = new HashMap<String, Object>();
		TQMMapper mapper = sqlSession.getMapper(TQMMapper.class);
		for (int i = 1; i < 10; i++) {
			String sql = (String) param.get("sql" + i);
			String lst = (String) param.get("lst" + i);
			if (sql == null)
				break;
			if (!sql.endsWith(".sql"))
				sql += ".sql";
			param.put("sql", sql);
			if (lst == null)
				rets.put("lst" + i, mapper.select1(param));
			else
				rets.put(lst, mapper.select1(param));

			model.addAttribute("result", JSONUtils.convertToMessage(rets));

			return "ajaxView";
		}
	}

	@RequestMapping("/common/comUIsel.do")
	public Object retrieveRqForm(HttpServletRequest req, ModelMap model) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		Enumeration enums = req.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName = (String) enums.nextElement();
			String[] parameters = req.getParameterValues(paramName);
			if (parameters.length > 1) {
				param.put(paramName, parameters);
			} else {
				param.put(paramName, parameters[0]);
			}
		}

		TQMMapper mapper = sqlSession.getMapper(TQMMapper.class);
		for (int i = 1; i < 10; i++) {
			String sql = (String) param.get("sql" + i);
			String lst = (String) param.get("lst" + i);
			if (sql == null)
				break;
			if (!sql.endsWith(".sql"))
				sql += ".sql";
			param.put("sql", sql);
			if (lst == null)
				model.addAttribute("lst" + i, mapper.select1(param));
			else
				model.addAttribute(lst, mapper.select1(param));
		}

		String ui = (String) param.get("ui");
		if (ui != null)
			return ui;
		return "error";
	}

}
