package model.service;

import java.util.List;

import database.CartDAO;
import viewmodel.CartItem;

public class CartService {

	private final CartDAO dao = new CartDAO();

	/**
	 * カート内の商品を DB に登録し、生成された orderId を返す
	 * @param cart - カート（セッション保存など）
	 * @param tableNo - テーブル番号 or 席番号
	 * @return order_id
	 * @throws Exception 
	 */
	public long registerOrder(List<CartItem> cart, String tableNo) throws Exception {
		if (cart == null || cart.isEmpty()) {
			throw new IllegalArgumentException("カートが空です");
		}
		return dao.insertOrder(cart, tableNo);
	}

}
