package viewmodel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import model.dto.MenuOptionDTO;

/**
 * 注文明細表示用ViewModel
 * JSPの ${item.selectedOptions} および ${item.optionTotalPrice} に完全対応させたバージョンです。
 */
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private int orderItemId;
	private int orderId;
	private String menuId;
	private String name;
	private int price;
	private int quantity;
	private String image;

	// ★ 重要：JSPの ${item.selectedOptions} に合わせるため名前を修正
	private List<MenuOptionDTO> selectedOptions;

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

	// ★ 重要：JSPから呼ばれるGetter
	public List<MenuOptionDTO> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<MenuOptionDTO> selectedOptions) {
		this.selectedOptions = selectedOptions;
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
	 * 小計（商品単価 × 個数）
	 * JSPで ${item.subtotal} として使用
	 */
	public int getSubtotal() {
		return this.price * this.quantity;
	}

	/**
	 * オプション合計金額
	 * JSPで ${item.optionTotalPrice} として使用
	 */
	public int getOptionTotalPrice() {
		int total = 0;
		if (selectedOptions != null) {
			for (MenuOptionDTO opt : selectedOptions) {
				total += opt.getOptionPrice();
			}
		}
		return total;
	}
}