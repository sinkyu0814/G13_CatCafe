package model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import database.DBManager;
import database.OrderItemsDAO;
import database.OrdersDAO;
import viewmodel.CartItem;

public class OrderService {

	private final OrdersDAO ordersDAO = new OrdersDAO();
	private final OrderItemsDAO itemsDAO = new OrderItemsDAO();

	/**
	 * カート内容を注文として登録し、カートをクリアする
	 *
	 * @param cart カート商品リスト
	 * @return 作成された注文ID
	 * @throws Exception 
	 */
	public int createOrder(List<CartItem> cart) throws Exception {

		if (cart == null || cart.isEmpty()) {
			throw new IllegalArgumentException("カートに商品がありません");
		}

		Connection conn = null;

		try {
			// トランザクション開始
			conn = DBManager.getConnection();
			conn.setAutoCommit(false);

			// -----------------------------
			// 1. ordersテーブルに注文ヘッダー登録
			// -----------------------------
			int orderId = ordersDAO.insertOrder(conn);

			// -----------------------------
			// 2. order_itemsテーブルに商品ごと登録
			// -----------------------------
			for (CartItem item : cart) {
				itemsDAO.insertItem(conn, orderId, item);
			}

			// 正常終了 → コミット
			conn.commit();

			return orderId;

		} catch (SQLException e) {

			if (conn != null)
				conn.rollback();
			throw e; // サーブレットに投げ直す

		} finally {

			if (conn != null)
				conn.setAutoCommit(true);
			if (conn != null)
				conn.close();

		}
	}
}
