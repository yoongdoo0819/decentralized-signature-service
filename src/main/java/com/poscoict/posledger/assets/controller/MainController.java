package com.poscoict.posledger.assets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.poscoict.posledger.assets.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Controller
public class MainController {

	@GetMapping("/")
	public String index() {
		log.info("index!");
		return "index";
	}
	
	/**
	 * Welcome 화면 
	 */
	@GetMapping("/welcome")
	public String welcome(Model model) {
		model.addAttribute("now", DateUtil.formatDate(DateUtil.getDateObject(), "yyyy.MM.dd HH:mm:ss"));
		return "welcome";
	}
}