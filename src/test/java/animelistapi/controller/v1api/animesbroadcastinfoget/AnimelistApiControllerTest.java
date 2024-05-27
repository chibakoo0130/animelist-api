package animelistapi.controller.v1api.animesbroadcastinfoget;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openapitools.model.AnimeBroadcastInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.junit5.api.DBRider;


@SpringBootTest
@AutoConfigureMockMvc
@DBRider
class AnimelistApiControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static String API_URL = "/v1/animes/broadcastinfos";
	
	@Test
	void アニメ放送情報が0件() throws Exception {
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
        List<AnimeBroadcastInfo> animeBroadcastInfoList = objectMapper.readValue(resStr, objectMapper.getTypeFactory().constructCollectionType(List.class, AnimeBroadcastInfo.class));
		assertEquals(animeBroadcastInfoList.size(), 0);
	}
}
