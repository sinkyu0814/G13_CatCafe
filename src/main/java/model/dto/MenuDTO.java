package model.dto;

import java.io.Serializable;
import java.util.List;

public class MenuDTO implements Serializable {

	
	private int id;
	private String name;
	private int price;
	private int quantity;
	private String category;
	private String img;
	private int isVisible;

	// オプションリスト
	private List<MenuOptionDTO> options;

	public MenuDTO() {
	}

	public MenuDTO(int id, String name, int quantity, int price, String img, String category) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.img = img;
		this.category = category;
	}

	// --- Getter / Setter ---

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}

	// ★ 追加した Setter
	public List<MenuOptionDTO> getOptions() {
		return options;
	}

	public void setOptions(List<MenuOptionDTO> options) {
		this.options = options;
	}
}