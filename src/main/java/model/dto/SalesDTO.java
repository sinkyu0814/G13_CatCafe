package model.dto;

import java.io.Serializable;

/**
 * 売上データを保持するDTOクラス
 */
public class SalesDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String displayDate;
	private int totalAmount;
	private int visitorCount;
	private int dayOfWeek; // 1:月 ... 6:土, 7:日

	// 基本コンストラクタ
	public SalesDTO(String displayDate, int totalAmount, int visitorCount) {
		this.displayDate = displayDate;
		this.totalAmount = totalAmount;
		this.visitorCount = visitorCount;
	}

	// 曜日情報付きコンストラクタ（日間表示で使用）
	public SalesDTO(String displayDate, int totalAmount, int visitorCount, int dayOfWeek) {
		this(displayDate, totalAmount, visitorCount);
		this.dayOfWeek = dayOfWeek;
	}

	// JSPからプロパティとしてアクセスするためのゲッター
	public String getDisplayDate() {
		return displayDate;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public int getVisitorCount() {
		return visitorCount;
	}

	/**
	 * JSPの ${s.dayOfWeek} で呼び出されるメソッド
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * 客単価を計算するメソッド
	 */
	public int getAveragePrice() {
		if (visitorCount == 0)
			return 0;
		return totalAmount / visitorCount;
	}
}