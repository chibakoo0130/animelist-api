package animelistapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.openapitools.api.V1Api;
import org.openapitools.model.AnimeBroadcastInfo;
import org.openapitools.model.AnimeBroadcastInfoBroadcastInfoListInner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import animelistapi.service.AnimelistService;

@RestController
@CrossOrigin
public class AnimelistApiController implements V1Api {

	@Autowired
	private AnimelistService service;
	
	@Override
	public ResponseEntity<List<AnimeBroadcastInfo>> v1AnimesBroadcastinfosGet() {
		
		List<animelistapi.domain.AnimeBroadcastInfo> animeList = service.selectAnimeBroadcastInfoList();

		List<AnimeBroadcastInfo> resAnimelist = new ArrayList<AnimeBroadcastInfo>();
		AnimeBroadcastInfo resAnime = null;
		for (int i = 0; i < animeList.size(); i++) {
			animelistapi.domain.AnimeBroadcastInfo anime = animeList.get(i);
			// 前のループと同じアニメの場合は放送情報のみ追加
			if (i != 0 && anime.getTitle().equals(animeList.get(i-1).getTitle())) {
				List<AnimeBroadcastInfoBroadcastInfoListInner> resInnerList = resAnime.getBroadcastInfoList();
				AnimeBroadcastInfoBroadcastInfoListInner resInner = new AnimeBroadcastInfoBroadcastInfoListInner();
				BeanUtils.copyProperties(anime, resInner);
				resInnerList.add(resInner);
			} else {
				// 最初のループと前のループと異なるアニメの場合は、アニメ放送情報ごと追加
				resAnime = new AnimeBroadcastInfo(anime.getTitle());
				List<AnimeBroadcastInfoBroadcastInfoListInner> innerList = new ArrayList<>();
				AnimeBroadcastInfoBroadcastInfoListInner inner = new AnimeBroadcastInfoBroadcastInfoListInner();
				BeanUtils.copyProperties(anime, inner);
				innerList.add(inner);
				resAnime.setBroadcastInfoList(innerList);
			}

			// 一つのアニメについてデータを作り切ったら追加
			// 最後のアニメか、次のループのアニメと異なるタイトルなら追加と判断
			if (i == animeList.size()-1 || !anime.getTitle().equals(animeList.get(i+1).getTitle())) {
				resAnimelist.add(resAnime);
			}
		}
		
		return ResponseEntity.ok(resAnimelist);

	}
}
