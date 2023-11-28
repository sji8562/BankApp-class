package com.tenco.mybankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.mybankapp.repository.entity.History;

@Mapper
public interface HistoryRepository {
	// 거래내역등록
	public int insert(History history);

	// 거래내역조회
	public List<History> findByAccountNumber(String id);

	// 동적쿼리 생성
	// 입금 / 출금 / 전체
	// 반드시 두개 이상의 파라미터 사용시 @Param 를 사용 해야 한다.
	public List<History> findByIdAndDynamicType(@Param("type") String type, @Param("accountId") Integer accountId);

}
