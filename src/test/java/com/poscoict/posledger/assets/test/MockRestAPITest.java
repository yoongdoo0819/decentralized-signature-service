package com.poscoict.posledger.assets.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.assets.controller.MainController;
import com.poscoict.posledger.assets.model.vo.AccessToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MockRestAPITest {

	private static final Logger logger = LoggerFactory.getLogger(MockRestAPITest.class);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
    MainController mainController;

	@Before
	public void setUp() {
		//this.user = User.builder().id("test02").password("1234").build();
        //mockMvc = mainController.index();
		logger.info("RestAPITest's setup is done...");
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static AccessToken toAccessToken(final String contents) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final AccessToken object = mapper.readValue(contents, AccessToken.class);
	        return object;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	/**
	 * 로그인 화면 테스트
	 */
	@Test
	public void loginTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/oauth/list")
			    .accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}
/*
	@Test
	public void index() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/assets/index")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void signUpForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/assets/signUpForm")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void signUp() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.post("/assets/signUp").param("userId", "userPaawd")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void main() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/assets/main")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void img() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/assets/img")//, "strImg", "abcd", "df", "qw", "ff")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				//.andExpect());
				.andExpect(status().isOk());
	}

	@Test
	public void upload() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.post("/assets/upload").param("hh", "20")
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getRedirectedUrl();
	}

	@Test
	public void doSign() throws Exception {
		//MultiValuedMap<String, String> A = new MultiValuedMap();

		mockMvc.perform(MockMvcRequestBuilders
				.post("/assets/doSign").param("userid", "a")//.params(new MultiValuedMap<"abc", "b">)
				.accept(MediaType.TEXT_HTML))
				.andDo(print())
				.andExpect(status().isOk());
	}*/
}
