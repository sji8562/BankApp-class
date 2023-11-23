package com.tenco.mybankapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.mybankapp.handler.exception.CustomPageException;

@Controller
@RequestMapping("/account")
public class AccountController {

	
	@GetMapping("list")
	public void list() {
		throw new CustomPageException("페이지가 없어요", HttpStatus.NOT_FOUND);
	}
}