package com.github.fabasset;

import com.github.fabasset.service.UserService;
import com.github.fabasset.util.DateUtil;
import com.github.fabasset.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

@ServletComponentScan
@SpringBootApplication
public class StartAssetsApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StartAssetsApplication.class);
    }

	@Bean
	public ServletWebServerFactory servletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}
//
	/*
	 * initialize user info
	 */
	@Bean
	public CommandLineRunner initUser(UserService userService) {

		return args -> {

			for (int i=1; i<=5; i++) {
				User user = User.builder().id("test0"+i).name("테스트0"+i).password("test1234").createDate(DateUtil.getDateObject()).build();
				userService.addUser(user);
			}
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(StartAssetsApplication.class, args);
	}
}