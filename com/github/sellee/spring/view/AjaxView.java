package com.github.sellee.spring.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class AjaxView extends AbstractView {

	public AjaxView() {
	}

	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Exception ex = (Exception) model.get("exception");
		StringBuffer returnMessage = new StringBuffer();

		if (ex != null) {
			Throwable throwable = ex.getCause();
			if (throwable == null) {
				throwable = ex;
			}

			String message = throwable.getMessage();
			if (message == null || message.equals("")) {
				message = throwable.getClass().getSimpleName();
			}
			response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			returnMessage.append("{\"resultCode\":\"error\", \"resultMessage\":\"" + "일시적인 오류가 발생하였습니다." + "\"}");
		} else {
			returnMessage.append((String) model.get("result"));
		}

		response.setContentType("text/json; charset=UTF-8");
		response.getWriter().write(returnMessage.toString());
	}
}
