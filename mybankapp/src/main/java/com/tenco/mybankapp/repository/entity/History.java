package com.tenco.mybankapp.repository.entity;

import java.security.Timestamp;

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
	private Long amount = 10L;
	private Long wBalance;
	private Long dBalance;
	private Integer wAccountId;
	private Integer dAccountId;
	private Timestamp createdAt;
}
