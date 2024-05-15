package animelistapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import animelistapi.domain.AnimeBroadcastInfo;
import animelistapi.repository.AnimelistRepository;

@Service
public class AnimelistService {

	@Autowired
	private AnimelistRepository repository;
	
	/**
	 * アニメ放送情報を取得する。
	 * 
	 * @return アニメ放送情報
	 */
	public List<AnimeBroadcastInfo> selectAnimeBroadcastInfoList() {
		return repository.selectAnimeBroadcastInfoList();
	}
}
