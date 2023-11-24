package com.tenco.mybankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.mybankapp.dto.AccountSaveFormDTO;
import com.tenco.mybankapp.dto.WithdrawFormDTO;
import com.tenco.mybankapp.handler.exception.CustomRestfullException;
import com.tenco.mybankapp.handler.exception.UnAuthorizedException;
import com.tenco.mybankapp.repository.entity.Account;
import com.tenco.mybankapp.repository.entity.User;
import com.tenco.mybankapp.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping({"/list", "/"})
	public String list(Model model) {
//		throw new CustomPageException("페이지가 없어요", HttpStatus.NOT_FOUND);
		//prefix
		//suffix
		
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다.",HttpStatus.UNAUTHORIZED);
		}
		List<Account> accountList = accountService.myAccountlist(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList",null);	
		}else {
			model.addAttribute("accountList",accountList);
		}
		System.out.println("리스트 출력 완료");
		return "account/list";
	}
	
	@GetMapping("/save")
	public String save() {
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요",HttpStatus.UNAUTHORIZED);
		}
		
		return "account/listsave";
		
	}
	@PostMapping("/save")
	public String saveProc(AccountSaveFormDTO dto) {
		
		//1.인증검사
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요",HttpStatus.UNAUTHORIZED);
		}
		
		//2.유효성 검사
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("계좌비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getBalance() == null || dto.getBalance() <= 0) {
			throw new CustomRestfullException("입금할 금액을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		accountService.save(dto, principal.getId());
		
		return "account/list";
		
	}
	
	@GetMapping("/withdraw")
	public String withdraw() {
		
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요",HttpStatus.UNAUTHORIZED);
		}
		
		return "account/withdraw";
	}
	
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDTO dto) {
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요",HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력학시오",HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("출금 금액이 0원이하 일 수 없습니다.",HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getWAccountNumber() == null ||dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("금액을 입력학시",HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null ||dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("금액을 입력학시",HttpStatus.BAD_REQUEST);
		}
		accountService.withdraw(dto,principal.getId());
		return "redirect:/account/list";
	}
	
}