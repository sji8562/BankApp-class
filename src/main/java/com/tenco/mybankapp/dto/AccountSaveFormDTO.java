package com.tenco.mybankapp.dto;

import lombok.Data;

@Data
public class AccountSaveFormDTO {
	private String number;
	private String password;
	private Long balance;
}
