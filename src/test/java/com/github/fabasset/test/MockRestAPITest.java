package com.github.fabasset.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabasset.controller.MainController;
import com.github.fabasset.model.vo.AccessToken;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
//
//	/**
//	 * login test
//	 */
//	@Test
//	public void loginTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders
//				.get("/oauth/list")
//			    .accept(MediaType.TEXT_HTML))
//				.andDo(print())
//				.andExpect(status().isOk());
//	}
}
