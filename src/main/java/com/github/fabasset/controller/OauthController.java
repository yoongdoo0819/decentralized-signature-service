package com.github.fabasset.controller;

import com.github.fabasset.TokenIssuer;
import com.github.fabasset.model.User;
import com.github.fabasset.model.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Validated
@Controller
@RequestMapping(value = "/oauth")
public class OauthController {

	@Autowired
	private TokenIssuer tokenIssuer;

	@Autowired
    UserDao userDao;


	/**
	 * login & issuing token
	 * 
	 * @return
	 */
	@PostMapping("/token")
	public String token(@RequestParam(value="userId", required=true) String userId, HttpServletRequest request) throws Exception {

		String _userId = request.getParameter("userId");
		String _userPasswd = request.getParameter("userPasswd");

		Map<String, Object> testMap = (userDao.getUser(_userId, _userPasswd));
		User user = new User();

		user.setId((String)testMap.get("userid"));
		user.setPassword((String)testMap.get("password"));

		log.info("login user : " + user.getId());
		
		request.getSession().setAttribute("sessionUser", user);
		request.getSession().setAttribute("accessToken", tokenIssuer.generateAuthenticateToken(user));
		
		if(user.getId().equals("admin0"))
			return "redirect:/admin";
		else
			return "redirect:/main";
	}
}