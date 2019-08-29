package com.poscoict.posledger.assets.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poscoict.posledger.assets.TokenIssuer;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.vo.AccessToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenIssuerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenIssuerTest.class);
	
	@Autowired
	private TokenIssuer tokenResolver;
	
	@Test
	public void generateTokenTest() throws Exception {
		
		User user = User.builder().id("mytest").password(DigestUtils.sha256Hex("1234")).build();
		logger.info("origin user : " + user);
		AccessToken accessToken = tokenResolver.generateAuthenticateToken(user);
		logger.info("accessToken : " + accessToken);
		
		// 토큰 만료 시간 테스트
		Thread.sleep(50000);
		logger.info("dcrypt token user : " + tokenResolver.decryptUserAccessToken(accessToken.getToken()));
	}
}
