package animelistapi.domain;

import lombok.Data;

@Data
public class AnimeBroadcastInfo {

	/** タイトル */
	private String title;
	/** 放送局 */
	private String tVStation;
	/** 放送日 */
	private String broadcastDate;
}
