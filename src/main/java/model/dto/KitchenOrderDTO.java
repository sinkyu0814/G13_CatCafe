package model.dto;

import java.sql.Timestamp;
import java.util.List;

public class KitchenOrderDTO {
	private long orderId;
	private int orderItemId; // ★追加：行を特定するために必要
	private String tableNo;
	private Timestamp orderDate;
	private String goodsName;
	private int quantity;
	private String kitchenStatus; // ★追加：ステータス保持
	private List<String> options;

	
	// Getter / Setter
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getKitchenStatus() {
		return kitchenStatus;
	}

	public void setKitchenStatus(String kitchenStatus) {
		this.kitchenStatus = kitchenStatus;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
}