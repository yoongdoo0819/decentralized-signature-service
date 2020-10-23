package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.TokenIssuer;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.dao.UserDao;
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

		user.setId((String)testMap.get("userid"));
		user.setPassword((String)testMap.get("password"));
		//user.setCreatedate((Date)testMap.get("crate_date"));
		//user.setName((String)testMap.get("name"));

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