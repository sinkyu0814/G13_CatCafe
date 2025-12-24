package viewmodel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import model.dto.MenuOptionDTO;

/**
 * 注文明細表示用ViewModel
 * DBのorder_itemsテーブルおよび関連する注文情報を保持します。
 */
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	// 商品・明細情報
	private int orderItemId;
	private int orderId;
	private String menuId;
	private String name; // JSPで ${item.name} としてアクセス
	private int price;
	private int quantity;
	private String image;
	private List<MenuOptionDTO> options;

	// 注文全体に関わる情報
	private Timestamp orderTime;
	private String status;
	private int tableNo;

	public OrderItem() {
	}

	// --- Getter / Setter ---

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<MenuOptionDTO> getOptions() {
		return options;
	}

	public void setOptions(List<MenuOptionDTO> options) {
		this.options = options;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTableNo() {
		return tableNo;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	/**
	 * 小計（価格 × 個数）を取得します。
	 * JSP等で ${item.subtotal} として呼び出せます。
	 */
	public int getSubtotal() {
		return this.price * this.quantity;
	}
}