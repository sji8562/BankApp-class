package com.tenco.mybankapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tenco.mybankapp.dto.AccountSaveFormDTO;
import com.tenco.mybankapp.dto.DepositFormDto;
import com.tenco.mybankapp.dto.TransferFormDto;
import com.tenco.mybankapp.dto.WithdrawFormDTO;
import com.tenco.mybankapp.handler.exception.CustomRestfullException;
import com.tenco.mybankapp.handler.exception.UnAuthorizedException;
import com.tenco.mybankapp.repository.entity.Account;
import com.tenco.mybankapp.repository.entity.History;
import com.tenco.mybankapp.repository.entity.User;
import com.tenco.mybankapp.service.AccountService;
import com.tenco.mybankapp.utils.Define;

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
			throw new CustomRestfullException("계좌번호를 입력하시오",HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null ||dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀번호를 입력하시오",HttpStatus.BAD_REQUEST);
		}
		accountService.withdraw(dto,principal.getId());
		return "redirect:/account/list";
	}
	
	@GetMapping("/deposit")
	public String deposit() {
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", 
					HttpStatus.UNAUTHORIZED);
		}
		return "account/deposit";
	}
	
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto dto) {
		User principal = (User)session.getAttribute("principal");
		if(principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요",HttpStatus.UNAUTHORIZED);
		}
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력학시오",HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null ||dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력하시오",HttpStatus.BAD_REQUEST);
		}
		accountService.updateAccountDeposit(dto , principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 이체 페이지 요청
			@GetMapping("/transfer")
			public String transfer() {

				// 1. 인증검사
				User principal = (User) session.getAttribute(Define.PRINCIPAL);
				if (principal == null) {
					throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
				}

				return "account/transfer";
			}

			@PostMapping("/transfer")
			public String transferProc(TransferFormDto dto) {
				System.out.println("transfer 들어오나? ");
				// 1. 인증검사
				User principal = (User) session.getAttribute(Define.PRINCIPAL);
				if (principal == null) {
					throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
				}

				if (dto.getAmount() == null) {
					throw new CustomRestfullException("금액을 입력하시오", HttpStatus.BAD_REQUEST);
				}
				if (dto.getAmount().longValue() <= 0) {
					throw new CustomRestfullException("이체 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
				}
				if (dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
					throw new CustomRestfullException("출금 계좌 번호를 입력하시오", HttpStatus.BAD_REQUEST);
				}
				if (dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
					throw new CustomRestfullException("입금 계좌 번호를 입력하시오", HttpStatus.BAD_REQUEST);
				}
				if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
					throw new CustomRestfullException("계좌 비밀번호를 입력하시오", HttpStatus.BAD_REQUEST);
				}
				
				// 2. 서비스 호출
				accountService.updateAccountTransferProc(dto, principal.getId());

				
				return "redirect:/account/list";
			}

			//계좌 상세보기 화면 요청 처리 - 데이터를 입력 받는 방법 정리!
			//기본값 세팅가능
			//인증검사
			@GetMapping("/detail/{accountId}")
			public String detail(@PathVariable Integer accountId,@RequestParam(name = "type",defaultValue = "all" ,required = false) String type, Model model){
				System.out.println(accountId);
				System.out.println(type);
				
				User principal = (User) session.getAttribute(Define.PRINCIPAL);
				if (principal == null) {
					throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
				}

				//상세 보기 화면 요청시 --> 데이터를 내려주어야한다.
				//account 데이터, 접근 주체 , 거래내역 정보
				Account account = accountService.readAccount(accountId);
				List<History> historyList = accountService.readHistoryListByAccount(type, accountId);

				
				model.addAttribute(Define.PRINCIPAL, principal);
				model.addAttribute("account", account);
				model.addAttribute("historyList", historyList);

				return "account/detail";
			}
		
}