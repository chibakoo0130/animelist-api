package animelistapi.controller.v1api.animesbroadcastinfoget;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.openapitools.model.AnimeBroadcastInfo;
import org.openapitools.model.AnimeBroadcastInfoBroadcastInfoListInner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

import animelistapi.exception.Error;
import animelistapi.service.AnimelistService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;


@SpringBootTest
@AutoConfigureMockMvc
@DBRider
class AnimelistApiControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	// モックにせず実メソッドを呼ぶケースもあるためSpy
    @SpyBean
    private AnimelistService mockService;
    
    private ListAppender<ILoggingEvent> mockAppender;
    @Captor
    private ArgumentCaptor<ILoggingEvent> captorLoggingEvent;
    
	private static final String API_URL = "/v1/animes/broadcastinfos";
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger("ROOT");
	
	@BeforeEach
	public void setup() {
		mockAppender = new ListAppender<>();
		mockAppender.start();
		LOGGER.addAppender(mockAppender);
	}
	
	@AfterEach
	public void teardown() {
		LOGGER.detachAppender(mockAppender);
	}
	
	@Test
	@DisplayName("正常系_1_アニメ放送情報が0件")
	@DataSet(cleanBefore = true)
	void アニメ放送情報が0件() throws Exception {
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
        Error res = objectMapper.readValue(resStr, Error.class);
		assertEquals(404, res.getStatusCode());
		assertEquals("検索結果が0件です。", res.getErrorMessage());
		assertNull(res.getErrorDetail());
		
		// ログ確認
		List<ILoggingEvent> logs = removeNotTargetLog(mockAppender.list);
		assertEquals(Level.INFO, logs.get(0).getLevel());
		assertEquals("処理開始：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()", 
				logs.get(0).getMessage());
		assertEquals(Level.INFO, logs.get(1).getLevel());
		assertEquals("処理開始：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()", 
				logs.get(1).getMessage());
		assertEquals(Level.INFO, logs.get(2).getLevel());
		assertEquals("処理終了：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()",
				logs.get(2).getMessage());
		assertEquals(Level.ERROR, logs.get(3).getLevel());
		assertEquals("処理異常終了：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()",
				logs.get(3).getMessage());
	}
	
	@Test
	@DisplayName("正常系_2_アニメ放送情報が1件")
	@DataSet("/animelistapicontroller/animesbroadcastinfosget/insert_one.yaml")
	void アニメ放送情報が1件() throws Exception {
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		List<AnimeBroadcastInfo> res = objectMapper.readValue(resStr, new TypeReference<List<AnimeBroadcastInfo>>() {});
		
		assertEquals(1, res.size());
		AnimeBroadcastInfo anime = res.get(0);
		assertEquals("Naruto", anime.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners = anime.getBroadcastInfoList();
		assertEquals(1, broadcastInfoListInners.size());
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_1 = broadcastInfoListInners.get(0);
		assertEquals("TV Tokyo", broadcastInfo_1.gettVStation());
		assertEquals("2023-01-05 18:00:00", broadcastInfo_1.getBroadcastDate());
		
		// ログ確認
		List<ILoggingEvent> logs = removeNotTargetLog(mockAppender.list);
		assertEquals(Level.INFO, logs.get(0).getLevel());
		assertEquals("処理開始：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()", 
				logs.get(0).getMessage());
		assertEquals(Level.INFO, logs.get(1).getLevel());
		assertEquals("処理開始：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()", 
				logs.get(1).getMessage());
		assertEquals(Level.INFO, logs.get(2).getLevel());
		assertEquals("処理終了：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()",
				logs.get(2).getMessage());
		assertEquals(Level.INFO, logs.get(3).getLevel());
		assertEquals("処理終了：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()",
				logs.get(3).getMessage());
		assertEquals(Level.INFO, logs.get(4).getLevel());
		assertEquals("返り値：<200 OK OK,[class AnimeBroadcastInfo {\n"
				+ "    title: Naruto\n"
				+ "    broadcastInfoList: [class AnimeBroadcastInfoBroadcastInfoListInner {\n"
				+ "        tVStation: TV Tokyo\n"
				+ "        broadcastDate: 2023-01-05 18:00:00\n"
				+ "    }]\n"
				+ "}],[]>", logs.get(4).getMessage());
	}
	
	@Test
	@DisplayName("正常系_3_アニメ放送情報が3件")
	@DataSet("/animelistapicontroller/animesbroadcastinfosget/insert_three.yaml")
	void アニメ放送情報が3件() throws Exception {
		
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		List<AnimeBroadcastInfo> res = objectMapper.readValue(resStr, new TypeReference<List<AnimeBroadcastInfo>>() {});
		
		assertEquals(3, res.size());
		// 1件目
		AnimeBroadcastInfo anime_1 = res.get(0);
		assertEquals("Naruto", anime_1.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_1 = anime_1.getBroadcastInfoList();
		assertEquals(1, broadcastInfoListInners_1.size());
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_1 = broadcastInfoListInners_1.get(0);
		assertEquals("TV Tokyo", broadcastInfo_1.gettVStation());
		assertEquals("2023-01-05 18:00:00", broadcastInfo_1.getBroadcastDate());
		// 2件目
		AnimeBroadcastInfo anime_2 = res.get(1);
		assertEquals("One Piece", anime_2.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_2 = anime_2.getBroadcastInfoList();
		assertEquals(broadcastInfoListInners_2.size(), 1);
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_2 = broadcastInfoListInners_2.get(0);
		assertEquals("Fuji TV", broadcastInfo_2.gettVStation());
		assertEquals("2023-01-06 19:00:00", broadcastInfo_2.getBroadcastDate());
		// 2件目
		AnimeBroadcastInfo anime_3 = res.get(2);
		assertEquals("Attack on Titan", anime_3.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_3 = anime_3.getBroadcastInfoList();
		assertEquals(1, broadcastInfoListInners_2.size());
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_3 = broadcastInfoListInners_3.get(0);
		assertEquals("NHK", broadcastInfo_3.gettVStation());
		assertEquals("2023-01-07 20:00:00", broadcastInfo_3.getBroadcastDate());
		
		// ログ確認
		List<ILoggingEvent> logs = removeNotTargetLog(mockAppender.list);
		assertEquals(Level.INFO, logs.get(0).getLevel());
		assertEquals("処理開始：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()", 
				logs.get(0).getMessage());
		assertEquals(Level.INFO, logs.get(1).getLevel());
		assertEquals("処理開始：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()", 
				logs.get(1).getMessage());
		assertEquals(Level.INFO, logs.get(2).getLevel());
		assertEquals("処理終了：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()",
				logs.get(2).getMessage());
		assertEquals(Level.INFO, logs.get(3).getLevel());
		assertEquals("処理終了：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()",
				logs.get(3).getMessage());
		assertEquals(Level.INFO, logs.get(4).getLevel());
		assertEquals("返り値：<200 OK OK,[class AnimeBroadcastInfo {\n"
				+ "    title: Naruto\n"
				+ "    broadcastInfoList: [class AnimeBroadcastInfoBroadcastInfoListInner {\n"
				+ "        tVStation: TV Tokyo\n"
				+ "        broadcastDate: 2023-01-05 18:00:00\n"
				+ "    }]\n"
				+ "}, class AnimeBroadcastInfo {\n"
				+ "    title: One Piece\n"
				+ "    broadcastInfoList: [class AnimeBroadcastInfoBroadcastInfoListInner {\n"
				+ "        tVStation: Fuji TV\n"
				+ "        broadcastDate: 2023-01-06 19:00:00\n"
				+ "    }]\n"
				+ "}, class AnimeBroadcastInfo {\n"
				+ "    title: Attack on Titan\n"
				+ "    broadcastInfoList: [class AnimeBroadcastInfoBroadcastInfoListInner {\n"
				+ "        tVStation: NHK\n"
				+ "        broadcastDate: 2023-01-07 20:00:00\n"
				+ "    }]\n"
				+ "}],[]>", logs.get(4).getMessage());
	}
	
	@Test
	@DisplayName("正常系_4_アニメに対して放送情報0件")
	@DataSet("/animelistapicontroller/animesbroadcastinfosget/insert_one_with_zero_info.yaml")
	void アニメに対して放送情報0件() throws Exception {
		
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		// このケースではjoinでデータが取れず0件
		Error res = objectMapper.readValue(resStr, Error.class);
		assertEquals(404, res.getStatusCode());
		assertEquals("検索結果が0件です。", res.getErrorMessage());
		assertNull(res.getErrorDetail());
	}
	
	@Test
	@DisplayName("正常系_5_アニメに対して放送情報2件")
	@DataSet("/animelistapicontroller/animesbroadcastinfosget/insert_one_with_two_info.yaml")
	void アニメに対して放送情報2件() throws Exception {
		
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		List<AnimeBroadcastInfo> res = objectMapper.readValue(resStr, new TypeReference<List<AnimeBroadcastInfo>>() {});
		
		assertEquals(1, res.size());
		AnimeBroadcastInfo anime = res.get(0);
		List<AnimeBroadcastInfoBroadcastInfoListInner> inner = anime.getBroadcastInfoList();
		assertEquals(2, inner.size());
		// 1の1件
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_1 = inner.get(0);
		assertEquals("TV Tokyo", broadcastInfo_1.gettVStation());
		assertEquals("2023-01-05 18:00:00", broadcastInfo_1.getBroadcastDate());
		// 1の2件
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_2 = inner.get(1);
		assertEquals("Fuji TV", broadcastInfo_2.gettVStation());
		assertEquals("2023-01-06 19:00:00", broadcastInfo_2.getBroadcastDate());
	}
	
	@Test
	@DisplayName("正常系_6_放送情報各項目のテスト")
	@DataSet("/animelistapicontroller/animesbroadcastinfosget/insert_one_with_many.yaml")
	void 放送情報各項目のテスト() throws Exception {
		
		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
		
		List<AnimeBroadcastInfo> res = objectMapper.readValue(resStr, new TypeReference<List<AnimeBroadcastInfo>>() {});
		
		assertEquals(4, res.size());
		// 空文字
		AnimeBroadcastInfo anime_1 = res.get(0);
		assertEquals("", anime_1.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_1 = anime_1.getBroadcastInfoList();
		assertEquals(1, broadcastInfoListInners_1.size());
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_1 = broadcastInfoListInners_1.get(0);
		assertEquals("", broadcastInfo_1.gettVStation());
		assertEquals("", broadcastInfo_1.getBroadcastDate());
		// 半角スペースかつ1文字
		AnimeBroadcastInfo anime_2 = res.get(1);
		assertEquals(" ", anime_2.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_2 = anime_2.getBroadcastInfoList();
		assertEquals(broadcastInfoListInners_2.size(), 1);
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_2 = broadcastInfoListInners_2.get(0);
		assertEquals(" ", broadcastInfo_2.gettVStation());
		assertEquals(" ", broadcastInfo_2.getBroadcastDate());
		// 全角スペース、その他記号
		AnimeBroadcastInfo anime_3 = res.get(2);
		String symbols = "　!\"#$%&¥'()=~|！”＃＄％＆’（）＝〜｜";
		assertEquals(symbols, anime_3.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_3 = anime_3.getBroadcastInfoList();
		assertEquals(broadcastInfoListInners_3.size(), 1);
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_3 = broadcastInfoListInners_3.get(0);
		assertEquals(symbols.substring(0, 19), broadcastInfo_3.gettVStation());
		assertEquals(symbols, broadcastInfo_3.getBroadcastDate());
		// 最大文字数
		AnimeBroadcastInfo anime_4 = res.get(3);
		assertEquals("1234567890223456789032345678904234567890523456789062345678907234567890823456789092345678900234567890", anime_4.getTitle());
		List<AnimeBroadcastInfoBroadcastInfoListInner> broadcastInfoListInners_4 = anime_4.getBroadcastInfoList();
		assertEquals(1, broadcastInfoListInners_2.size());
		AnimeBroadcastInfoBroadcastInfoListInner broadcastInfo_4 = broadcastInfoListInners_4.get(0);
		assertEquals("1234567890234567890", broadcastInfo_4.gettVStation());
		assertEquals("12345678902234567890323456789042345678905234567890", broadcastInfo_4.getBroadcastDate());
	}

	@Test
	@DisplayName("異常系_7_DB処理でエラー")
	void DB処理エラーの場合のテスト() throws Exception {

		// PSQLExceptionなどは定義外でモックのエラーになるためnullpoで例外発生させる
		Mockito.when(mockService.selectAnimeBroadcastInfoList()).thenThrow(NullPointerException.class);

		String resStr = mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json; charset=utf-8"))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		Error res = objectMapper.readValue(resStr, Error.class);
		assertEquals(500, res.getStatusCode());
		assertEquals("DB処理に失敗しました。", res.getErrorMessage());
		assertNull(res.getErrorDetail());
		
		// ログ確認
		List<ILoggingEvent> logs = removeNotTargetLog(mockAppender.list);
		// モック分でもログが出るので3要素目からアサート
		assertEquals(Level.INFO, logs.get(2).getLevel());
		assertEquals("処理開始：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()", 
				logs.get(2).getMessage());
		assertEquals(Level.INFO, logs.get(3).getLevel());
		assertEquals("処理開始：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()", 
				logs.get(3).getMessage());
		assertEquals(Level.INFO, logs.get(4).getLevel());
		assertEquals("処理終了：List animelistapi.service.AnimelistService.selectAnimeBroadcastInfoList()",
				logs.get(4).getMessage());
		assertEquals(Level.ERROR, logs.get(5).getLevel());
		assertEquals("処理異常終了：ResponseEntity animelistapi.controller.AnimelistApiController.v1AnimesBroadcastinfosGet()",
				logs.get(5).getMessage());
	}
	
	private List<ILoggingEvent> removeNotTargetLog(List<ILoggingEvent> logs) {
		
		Iterator<ILoggingEvent> it = logs.iterator();
		while(it.hasNext()) {
			ILoggingEvent log = it.next();
			String logMessage = log.getMessage();
			if (logMessage.equals("The property ending with 'schema' was not found. Please notify a dbunit developer to add the property to the class org.dbunit.database.DatabaseConfig")
					|| logMessage.equals("The property ending with 'prologTimeout' was not found. Please notify a dbunit developer to add the property to the class org.dbunit.database.DatabaseConfig")
					|| logMessage.equals("The property ending with 'replacers' was not found. Please notify a dbunit developer to add the property to the class org.dbunit.database.DatabaseConfig")
					|| logMessage.contains("DBUnit configuration for dataset executor")
					|| logMessage.equals("Database will not be populated because no dataset has been provided.")) {
				it.remove();
			}
		}
		
		return logs;
	}


}
