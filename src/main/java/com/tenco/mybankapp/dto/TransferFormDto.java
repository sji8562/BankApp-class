package com.tenco.mybankapp.dto;

import lombok.Data;

@Data
public class TransferFormDto {
	Long amount;
	String dAccountNumber;
	String wAccountNumber;
	String password;
}
