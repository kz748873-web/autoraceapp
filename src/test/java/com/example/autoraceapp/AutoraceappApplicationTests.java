package com.example.autoraceapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AutoRaceAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void homePageReturnsUtf8HtmlWithMainContent() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("text/html"))
				.andExpect(content().encoding("UTF-8"))
				.andExpect(content().string(containsString("<html lang=\"ja\"")))
				.andExpect(content().string(containsString("TOPページ")))
				.andExpect(content().string(containsString("レース場とレース番号を選んで次に進めます")))
				.andExpect(content().string(containsString("予想準備")))
				.andExpect(content().string(containsString("公式リンク")));
	}

}
