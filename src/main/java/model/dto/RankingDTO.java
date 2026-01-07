package model.dto;

import java.io.Serializable;

public class RankingDTO implements Serializable {
	private String goodsName;
	private int orderCount;
	private double ratio;

	public RankingDTO(String goodsName, int orderCount) {
		this.goodsName = goodsName;
		this.orderCount = orderCount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}