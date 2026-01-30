package viewmodel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import model.dto.MenuOptionDTO;

/**
 * 注文明細表示用ViewModel
 * 余計な型変更は行わず、計算時のオーバーフローのみ対策したバージョンです。
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

	private List<MenuOptionDTO> selectedOptions;

	private Timestamp orderTime;
	private String status;
	private int tableNo;

	public OrderItem() {
	}

	// --- Getter / Setter (型は一切変えていません) ---

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
	 * ★修正：計算時に一度 long にキャストしてオーバーフローを防ぎ、最後に int に戻します。
	 */
	public int getSubtotal() {
		return (int) ((long) this.price * this.quantity);
	}

	/**
	 * オプション合計金額
	 * ★修正：内部の計算変数を long にし、最後に int に戻します。
	 */
	public int getOptionTotalPrice() {
		long total = 0; // 計算用
		if (selectedOptions != null) {
			for (MenuOptionDTO opt : selectedOptions) {
				total += opt.getOptionPrice();
			}
		}
		return (int) total;
	}
}