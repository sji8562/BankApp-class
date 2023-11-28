package com.tenco.mybankapp.repository.entity;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
	private Integer id; 
	private Long amount; 
	private Long wBalance; 
	private Long dBalance;
	private Integer wAccountId; 
	private Integer dAccountId; 
	private Timestamp createdAt; 

	//거래내역 정보 추가

	private String sender;
	private String receiver;
	private Long balance;

	public String formatCreatedAt(Timestamp createdAt){
		
		// Timestamp to String
		String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(createdAt);
		return time;
	}
	public String formatBalance(Long balance){
		DecimalFormat decFormat = new DecimalFormat("###,###");
		String value = decFormat.format(balance);
		
		return value;
	}
}