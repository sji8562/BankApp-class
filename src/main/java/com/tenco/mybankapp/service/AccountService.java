package com.tenco.mybankapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.mybankapp.dto.AccountSaveFormDTO;
import com.tenco.mybankapp.dto.DepositFormDto;
import com.tenco.mybankapp.dto.TransferFormDto;
import com.tenco.mybankapp.dto.WithdrawFormDTO;
import com.tenco.mybankapp.handler.exception.CustomRestfullException;
import com.tenco.mybankapp.repository.entity.Account;
import com.tenco.mybankapp.repository.entity.History;
import com.tenco.mybankapp.repository.interfaces.AccountRepository;
import com.tenco.mybankapp.repository.interfaces.HistoryRepository;

@Service  //IoC대상 + 싱글톤 관
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private HistoryRepository historyRepository;
	/**
	 * 계좌 생성 기능
	 * @param dto
	 * @param principalId
	 */
	@Transactional
	public void save(AccountSaveFormDTO dto, Integer principalId) {
		
		//계좌 중복 여부 확인
	
		
		Account account = Account.builder().number(dto.getNumber()).password(dto.getPassword()).balance(dto.getBalance()).userId(principalId).build();
		int resultRowCount = accountRepository.insert(account);
		
		if(resultRowCount != 1) {
			throw new CustomRestfullException("계좌생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	public List<Account> myAccountlist(Integer userId) {
		List<Account> list = accountRepository.findByUserId(userId);
		
		return list;
		
		
	}
	
	
	//출금 기능 로직 고민해보기
	// 1. 계좌 존재여부 확인  ->select
	// 2. 본인 계좌 확인 -> select
	// 3. 비밀번호 체크 -> selsect
	// 4. 잔액 여부확인 ->select
	// 5. 출금 처리 --> update
	// 6. 히스토리 남기가 -> insert
	// 7. 트랜잭션 처리
	@Transactional
	public void withdraw(WithdrawFormDTO dto, Integer userId) {
		System.out.println(userId);
		Account account = accountRepository.findByNumber(dto.getWAccountNumber());
		System.out.println(account.toString());
		System.out.println(dto.getPassword());
		if(account == null) {
			throw new CustomRestfullException("계좌가 없습니다.",HttpStatus.BAD_REQUEST);
		}
		if(account.getUserId() != userId) {
			throw new CustomRestfullException("본인 계좌가 아닙니다.",HttpStatus.BAD_REQUEST);
		}
		if(!account.getPassword().equals(dto.getPassword())) {
			throw new CustomRestfullException("패스워드가 틀렸습니다.",HttpStatus.BAD_REQUEST);
		}
		if(account.getBalance() < dto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다.",HttpStatus.BAD_REQUEST);
		}
		account.setBalance(account.getBalance() - dto.getAmount());
		System.out.println(account.toString());
		
		
		int resultRowCount = accountRepository.updateById(account);
		System.out.println(resultRowCount);
		History history = History.builder().amount(dto.getAmount()).wAccountId(account.getId()).wBalance(account.getBalance()).build();
		historyRepository.insert(history);
			
	}
	
	// 입금 처리 기능 - AMT 입금 처리 
	// 트랜젝션 처리 
	// 1. 계좌 존재 여부 확인 
	// 2. 입금 처리 -> update 
	// 3. 거래 내역 등록 처리 -> insert 
	public void updateAccountDeposit(DepositFormDto dto, Integer userId) {
		
		Account accountEnity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(accountEnity == null) {
			throw new CustomRestfullException("해당 계좌는 존재하지 않습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 객체 상태값 변경 
		accountEnity.deposit(dto.getAmount());
		accountRepository.updateById(accountEnity);
		
		// 거래 내역 등록 
		History history = new History(); 
		history.setAmount(dto.getAmount());
		history.setWBalance(null);
		history.setDBalance(accountEnity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEnity.getId());
		
		int resultRowCount = historyRepository.insert(history);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리 되지 않았습니다",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	public void updateAccountTransferProc(TransferFormDto dto, Integer principalId) {
		try {
			Account wAccountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
			Account dAccountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
			
			if(wAccountEntity == null) {
				throw new CustomRestfullException("출금 계좌가 없습니다.", HttpStatus.BAD_REQUEST);
			}
			if(dAccountEntity == null) {
				throw new CustomRestfullException("입금 계좌가 없습니다.", HttpStatus.BAD_REQUEST);
			}
			if(wAccountEntity.getUserId() != principalId) {
				throw new CustomRestfullException("본인 소유 계좌가 아닙니다", HttpStatus.UNAUTHORIZED);
			}
			if(wAccountEntity.getPassword().equals(dto.getPassword()) == false) {
				throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다", HttpStatus.BAD_REQUEST);
			}
			if(wAccountEntity.getBalance() < dto.getAmount()) {
				throw new CustomRestfullException("계좌 잔액이 부족합니다", HttpStatus.BAD_REQUEST);
			}
			// 객체 모델 상태값 변경 처리
			wAccountEntity.withdraw(dto.getAmount());
			dAccountEntity.deposit(dto.getAmount());
			accountRepository.updateById(wAccountEntity);
			accountRepository.updateById(dAccountEntity);
			
			// 거래 내역 등록
			History history = new History();
			history.setAmount(dto.getAmount());
			
			//
			history.setWBalance(wAccountEntity.getBalance());
			history.setDBalance(dAccountEntity.getBalance());
			history.setWAccountId(wAccountEntity.getId());
			history.setDAccountId(dAccountEntity.getId());
			int resultRowCount = historyRepository.insert(history);
			if(resultRowCount != 1) {
				throw new CustomRestfullException("정상 처리되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);}			
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
