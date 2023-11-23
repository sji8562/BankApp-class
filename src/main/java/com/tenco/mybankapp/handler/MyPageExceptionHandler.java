package com.tenco.mybankapp.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.mybankapp.handler.exception.CustomPageException;

/*
 * 
 * View 렌더링을 위해 modelView
 * 객체를 반환하도록 설정 되어 있다.
 * 예외처리 Page 리턴할 때 사용
 * */

@ControllerAdvice
public class MyPageExceptionHandler {
	
	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handleRuntimeException(CustomPageException e) {
		ModelAndView modelAndView = new ModelAndView("errorPage");
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
		
	}
	
}
