package viewmodel;

/**
 * 注文品目データを保持するクラス
 */
public class OrderItem {
	private String name;
	private int price;

	public OrderItem(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	// Setterは今回は不要ですが、必要に応じて追加してください
}