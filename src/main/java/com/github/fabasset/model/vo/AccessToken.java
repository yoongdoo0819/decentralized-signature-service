package com.github.fabasset.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccessToken {

	public static final int expireTime = 1000*60*60*24*7; // 7 day
	
	public static final String secret = "mytoken";
	
	private String type;

	private String token;

	private Date issueDate;
	
	private Date expireDate;
	
	private String scope;
}
