package model.dto;

import java.io.Serializable;

public class SalesDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String displayDate;
	private long totalAmount; // ★ int から long へ変更
	private long visitorCount; // ★ 念のため来店数も long へ変更
	private int dayOfWeek;

	public SalesDTO(String displayDate, long totalAmount, long visitorCount) {
		this.displayDate = displayDate;
		this.totalAmount = totalAmount;
		this.visitorCount = visitorCount;
	}

	public SalesDTO(String displayDate, long totalAmount, long visitorCount, int dayOfWeek) {
		this(displayDate, totalAmount, visitorCount);
		this.dayOfWeek = dayOfWeek;
	}

	public String getDisplayDate() {
		return displayDate;
	}

	public long getTotalAmount() {
		return totalAmount;
	} // ★ long

	public long getVisitorCount() {
		return visitorCount;
	} // ★ long

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * 客単価を計算するメソッド
	 */
	public long getAveragePrice() { // ★ long
		if (visitorCount == 0)
			return 0;
		return totalAmount / visitorCount;
	}
}