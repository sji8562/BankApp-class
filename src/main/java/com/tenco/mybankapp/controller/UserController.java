package com.tenco.mybankapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.mybankapp.dto.SignInFormDto;
import com.tenco.mybankapp.dto.SignUpFormDto;
import com.tenco.mybankapp.handler.exception.CustomRestfullException;
import com.tenco.mybankapp.repository.entity.User;
import com.tenco.mybankapp.service.UserService;
import com.tenco.mybankapp.utils.Define;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;
//	DI처리
//	public UserController(UserService userService) {
//		this.userService = userService;
//		
//	}

	// 회원가입 페이지 요청
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		return "user/signUp";
	}

	@GetMapping("/sign-in")
	public String signIn() {
		return "user/signIn";
	}

	// DTO - Object Mapper
	/**
	 * 회원가입처리
	 * 
	 * @param dto
	 * @return 리다이렉트 로그인 페이지 처리
	 */
	@PostMapping("/sign-up")
	public String signUpProc(SignUpFormDto dto) {
		// 1. 유효성 검사

		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("유저네임을 입력하세요", HttpStatus.BAD_REQUEST);
		}

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하세요", HttpStatus.BAD_REQUEST);
		}

		if (dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfullException("풀네임을 입력하세요", HttpStatus.BAD_REQUEST);
		}

		int resultRowCount = userService.signUp(dto);
		if (resultRowCount != 1) {
			// 다른처리
		}

		return "redirect:/user/sign-in";
	}

	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("유저네임을 입력하세요", HttpStatus.BAD_REQUEST);
		}

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하세요", HttpStatus.BAD_REQUEST);
		}
		User principal = userService.signIn(dto);
		session.setAttribute(Define.PRINCIPAL, principal); // 사용자 정보를 세션에 저장
		System.out.println("로그인 되었습니다.");
		return "redirect:/account/list";
	}

	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/sign-in";
	}
}
