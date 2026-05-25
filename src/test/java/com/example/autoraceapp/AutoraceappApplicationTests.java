package com.example.autoraceapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.autoraceapp.entity.RaceRecord;
import com.example.autoraceapp.repository.RaceRecordRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AutoRaceAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RaceRecordRepository raceRecordRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void recordListAndNewFormRender() throws Exception {
		RaceRecord record = new RaceRecord();
		record.setRaceDate("2026-05-25");
		record.setVenue("\u5ddd\u53e3");
		record.setRaceNumber(1);
		record = raceRecordRepository.save(record);

		mockMvc.perform(get("/records"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("text/html"))
				.andExpect(content().string(containsString("/records/new")))
				.andExpect(content().string(containsString("/records/" + record.getId() + "/edit")));

		mockMvc.perform(get("/records/new"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("text/html"))
				.andExpect(content().string(containsString("id=\"raceRecordForm\"")))
				.andExpect(content().string(containsString("name=\"id\"")))
				.andExpect(content().string(containsString("startLineupPositions")));
	}

	@Test
	void editFormRendersAndPostKeepsSameRecordId() throws Exception {
		String savedStartDiagram = "{\"version\":1,\"positions\":{\"1\":{\"number\":1,\"x\":12.5,\"y\":34.5,\"placed\":true}}}";
		RaceRecord record = new RaceRecord();
		record.setRaceDate("2026-05-25");
		record.setVenue("\u5ddd\u53e3");
		record.setRaceNumber(1);
		record.setPreRacePrediction("pre-race prediction kept");
		record.setPreRaceNote("pre-race note kept");
		record.setStartLineupPositions(savedStartDiagram);
		record.setWeatherCondition("\u6674\u308c");
		record.setTrackCondition("\u826f\u8d70\u8def");
		record.setTrialTime1("3.41");
		record.setRaceOverallNote("day note kept");
		record.setBetLine1Type("3\u9023\u5358");
		record.setBetLine1Count(3);
		record.setBetLine1Amount(300);
		record.setFinalPrediction("final prediction kept");
		record.setPredictionNote("prediction note kept");
		record.setPayoutAmount(1000);
		record = raceRecordRepository.save(record);

		mockMvc.perform(get("/records/{id}/edit", record.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("text/html"))
				.andExpect(content().string(containsString("id=\"raceRecordForm\"")))
				.andExpect(content().string(containsString("id=\"id\" name=\"id\" value=\"" + record.getId() + "\"")))
				.andExpect(content().string(containsString("pre-race prediction kept")))
				.andExpect(content().string(containsString("pre-race note kept")))
				.andExpect(content().string(containsString("id=\"startLineupPositions\" name=\"startLineupPositions\" value=\"{&quot;version&quot;:1")))
				.andExpect(content().string(containsString("&quot;positions&quot;")))
				.andExpect(content().string(containsString("12.5")))
				.andExpect(content().string(containsString("34.5")))
				.andExpect(content().string(containsString("value=\"\u6674\u308c\" selected=\"selected\"")))
				.andExpect(content().string(containsString("value=\"\u826f\u8d70\u8def\" selected=\"selected\"")))
				.andExpect(content().string(containsString("id=\"trialTime1\"")))
				.andExpect(content().string(containsString("name=\"trialTime1\" value=\"3.41\"")))
				.andExpect(content().string(containsString("day note kept")))
				.andExpect(content().string(containsString("value=\"3\u9023\u5358\" selected=\"selected\"")))
				.andExpect(content().string(containsString("name=\"betLine1Amount\" value=\"300\"")))
				.andExpect(content().string(containsString("name=\"finalPrediction\" value=\"final prediction kept\"")))
				.andExpect(content().string(containsString("prediction note kept")));

		mockMvc.perform(post("/records")
						.param("id", record.getId().toString())
						.param("raceDate", "2026-05-25")
						.param("venue", "\u98ef\u585a")
						.param("raceNumber", "2")
						.param("preRacePrediction", "pre-race prediction kept")
						.param("preRaceNote", "pre-race note kept")
						.param("startLineupPositions", savedStartDiagram)
						.param("weatherCondition", "\u6674\u308c")
						.param("trackCondition", "\u826f\u8d70\u8def")
						.param("trialTime1", "3.41")
						.param("raceOverallNote", "day note kept")
						.param("betLine1Type", "3\u9023\u5358")
						.param("betLine1Count", "3")
						.param("betLine1Amount", "300")
						.param("finalPrediction", "final prediction updated")
						.param("predictionNote", "prediction note kept")
						.param("payoutAmount", "1000")
						.param("raceResult", "1-2"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/records"));

		RaceRecord updatedRecord = raceRecordRepository.findById(record.getId()).orElseThrow();
		assertEquals("\u98ef\u585a", updatedRecord.getVenue());
		assertEquals(2, updatedRecord.getRaceNumber());
		assertEquals("pre-race prediction kept", updatedRecord.getPreRacePrediction());
		assertEquals("pre-race note kept", updatedRecord.getPreRaceNote());
		assertEquals(savedStartDiagram, updatedRecord.getStartLineupPositions());
		assertEquals("\u6674\u308c", updatedRecord.getWeatherCondition());
		assertEquals("\u826f\u8d70\u8def", updatedRecord.getTrackCondition());
		assertEquals("3.41", updatedRecord.getTrialTime1());
		assertEquals("day note kept", updatedRecord.getRaceOverallNote());
		assertEquals("final prediction updated", updatedRecord.getFinalPrediction());
		assertEquals("prediction note kept", updatedRecord.getPredictionNote());
		assertEquals(300, updatedRecord.getTotalBetAmount());
		assertEquals(700, updatedRecord.getProfitLoss());
		assertTrue(raceRecordRepository.existsById(record.getId()));
	}

	@Test
	void sectionSaveKeepsAllStepsOnSameRecord() throws Exception {
		String savedStartDiagram = "{\"version\":1,\"positions\":{\"2\":{\"number\":2,\"x\":21.5,\"y\":45.25,\"placed\":true}}}";
		long countBefore = raceRecordRepository.count();

		MvcResult preRaceSaveResult = mockMvc.perform(post("/records/section")
						.param("raceDate", "2026-05-25")
						.param("venue", "\u5ddd\u53e3")
						.param("raceNumber", "1")
						.param("preRacePrediction", "pre-race step saved")
						.param("preRaceNote", "pre-race note saved"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json"))
				.andReturn();

		Long recordId = objectMapper.readTree(preRaceSaveResult.getResponse().getContentAsString()).get("id").asLong();
		assertEquals(countBefore + 1, raceRecordRepository.count());

		mockMvc.perform(post("/records/section")
						.param("id", recordId.toString())
						.param("raceDate", "2026-05-25")
						.param("venue", "\u5ddd\u53e3")
						.param("raceNumber", "1")
						.param("preRacePrediction", "pre-race step saved")
						.param("preRaceNote", "pre-race note saved")
						.param("startLineupPositions", savedStartDiagram)
						.param("weatherCondition", "\u6674\u308c")
						.param("trackCondition", "\u826f\u8d70\u8def")
						.param("trialTime1", "3.39")
						.param("raceOverallNote", "same-day step saved")
						.param("betLine1Type", "3\u9023\u5358")
						.param("betLine1Count", "4")
						.param("betLine1Amount", "400")
						.param("finalPrediction", "same-day final saved")
						.param("predictionNote", "same-day note saved"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json"));

		assertEquals(countBefore + 1, raceRecordRepository.count());

		mockMvc.perform(post("/records/section")
						.param("id", recordId.toString())
						.param("raceDate", "2026-05-25")
						.param("venue", "\u5ddd\u53e3")
						.param("raceNumber", "1")
						.param("preRacePrediction", "pre-race step saved")
						.param("preRaceNote", "pre-race note saved")
						.param("startLineupPositions", savedStartDiagram)
						.param("weatherCondition", "\u6674\u308c")
						.param("trackCondition", "\u826f\u8d70\u8def")
						.param("trialTime1", "3.39")
						.param("raceOverallNote", "same-day step saved")
						.param("betLine1Type", "3\u9023\u5358")
						.param("betLine1Count", "4")
						.param("betLine1Amount", "400")
						.param("finalPrediction", "same-day final saved")
						.param("predictionNote", "same-day note saved")
						.param("raceResult", "result step saved")
						.param("resultComparison", "comparison step saved")
						.param("reviewNote", "review step saved")
						.param("note", "next note saved"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("application/json"));

		assertEquals(countBefore + 1, raceRecordRepository.count());

		RaceRecord savedRecord = raceRecordRepository.findById(recordId).orElseThrow();
		assertEquals("pre-race step saved", savedRecord.getPreRacePrediction());
		assertEquals(savedStartDiagram, savedRecord.getStartLineupPositions());
		assertEquals("same-day step saved", savedRecord.getRaceOverallNote());
		assertEquals("result step saved", savedRecord.getRaceResult());
		assertEquals("comparison step saved", savedRecord.getResultComparison());
		assertEquals("review step saved", savedRecord.getReviewNote());

		mockMvc.perform(get("/records/{id}/edit", recordId))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("pre-race step saved")))
				.andExpect(content().string(containsString("same-day step saved")))
				.andExpect(content().string(containsString("result step saved")))
				.andExpect(content().string(containsString("comparison step saved")))
				.andExpect(content().string(containsString("review step saved")))
				.andExpect(content().string(containsString("21.5")))
				.andExpect(content().string(containsString("45.25")));

		mockMvc.perform(get("/records/{id}", recordId))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("pre-race step saved")))
				.andExpect(content().string(containsString("same-day step saved")))
				.andExpect(content().string(containsString("result step saved")))
				.andExpect(content().string(containsString("comparison step saved")))
				.andExpect(content().string(containsString("review step saved")))
				.andExpect(content().string(containsString("21.5")))
				.andExpect(content().string(containsString("45.25")));
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
