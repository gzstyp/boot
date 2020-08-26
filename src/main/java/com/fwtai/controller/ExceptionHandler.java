package com.fwtai.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fwtai.tool.ToolClient;

/**
 * 全局的系统异常处理json数据格式提示
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年5月14日 下午1:01:22
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@ControllerAdvice
public class ExceptionHandler implements HandlerExceptionResolver{

	@ResponseBody
	@Override
	public ModelAndView resolveException(final HttpServletRequest request,final HttpServletResponse response,final Object object,final Exception exception){
		ToolClient.responseException(response);
		return null;
	}
}