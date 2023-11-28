package com.tenco.mybankapp.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeStampUtil {
    //상태값을 가지는 변수를 사용하면 안된다
    public String formatCreatedAt(Timestamp createdAt){
		
		// Timestamp to String
		String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(createdAt);
		return time;
	}
}
