package animelistapi.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import animelistapi.domain.AnimeBroadcastInfo;

/**
 * アニメリストリポジトリ。
 */
@Mapper
public interface AnimelistRepository {

	/**
	 * アニメ放送情報を取得する。
	 * 
	 * @return アニメ放送情報
	 */
	public List<AnimeBroadcastInfo> selectAnimeBroadcastInfoList();
}
