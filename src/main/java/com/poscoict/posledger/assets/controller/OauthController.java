package com.poscoict.posledger.assets.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscoict.posledger.assets.TokenIssuer;
import com.poscoict.posledger.assets.exception.UserNotFoundException;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.service.UserService;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
@Validated
@Controller
@RequestMapping(value = "/oauth")
public class OauthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenIssuer tokenIssuer;

	@Autowired UserDao userDao;

	/**
	 * 로그인 화면 
	 */
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("userList", userService.getAllUsers());
		return "login";
	}

	/**
	 * 로그인 처리 및 토큰 발급
	 * 
	 * @return
	 */
	@PostMapping("/token")
	public String token(@RequestParam(value="userId", required=true) String userId, HttpServletRequest request) throws Exception {
		
		//User user = userService.getUser(userId);

		String _userId = request.getParameter("userId");
		String _userPasswd = request.getParameter("userPasswd");

		//log.info(userId);
		//if (user == null) throw new UserNotFoundException(userId);

		Map<String, Object> testMap = (userDao.getUser(_userId, _userPasswd));
		User user = new User();

		user.setId((String)testMap.get("id"));
		user.setPassword((String)testMap.get("password"));
		user.setCreatedate((Date)testMap.get("crate_date"));
		user.setName((String)testMap.get("name"));

		//User user = (User)(testMap.get(("yoongdoo1")));
		//User user = userDao.getUser("yoongdoo1");

		//for(String user : testMap) {
		//	log.info("kk " + user);
		//}
		log.info("login user : " + user.getId());
		
		request.getSession().setAttribute("sessionUser", user);
		request.getSession().setAttribute("accessToken", tokenIssuer.generateAuthenticateToken(user));
		
		// 토큰 발급 및 반환
		return "redirect:/main";
	}
}