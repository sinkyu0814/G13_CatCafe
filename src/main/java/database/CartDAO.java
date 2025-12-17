package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import viewmodel.CartItem;

public class CartDAO {

	/**
	 * カートの中身を orders + order_items に登録する.
	 * @throws Exception
	 */
	public long insertOrder(List<CartItem> cart, String tableNo) throws Exception {

		Connection conn = null;
		PreparedStatement psSeq = null;
		PreparedStatement psInsertOrder = null;
		PreparedStatement psInsertItem = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			conn.setAutoCommit(false); // トランザクション開始

			//━━━━━━━━━━━━━━━━━━━━━
			// 1) order_id（採番）
			//━━━━━━━━━━━━━━━━━━━━━
			String seqSql = "SELECT seq_orders.nextval FROM dual";
			psSeq = conn.prepareStatement(seqSql);
			rs = psSeq.executeQuery();

			long orderId;
			if (rs.next()) {
				orderId = rs.getLong(1);
			} else {
				throw new SQLException("seq_orders の取得に失敗しました");
			}

			//━━━━━━━━━━━━━━━━━━━━━
			// 2) 合計金額を算出
			//━━━━━━━━━━━━━━━━━━━━━
			int totalAmount = cart.stream()
					.mapToInt(ci -> ci.getPrice() * ci.getQuantity())
					.sum();

			//━━━━━━━━━━━━━━━━━━━━━
			// 3) orders（ヘッダ）INSERT
			//━━━━━━━━━━━━━━━━━━━━━
			String insertOrderSql = "INSERT INTO orders (order_id, table_no, order_date, total_amount) "
					+ "VALUES (?, ?, SYSDATE, ?)";
			psInsertOrder = conn.prepareStatement(insertOrderSql);
			psInsertOrder.setLong(1, orderId);
			psInsertOrder.setInt(2, Integer.parseInt(tableNo));
			psInsertOrder.setInt(3, totalAmount);
			psInsertOrder.executeUpdate();

			//━━━━━━━━━━━━━━━━━━━━━
			// 4) order_items（明細）INSERT
			//━━━━━━━━━━━━━━━━━━━━━
			String insertItemSql = "INSERT INTO order_items "
					+ "(order_item_id, order_id, menu_id, goods_name, price, quantity) "
					+ "VALUES (seq_order_items.nextval, ?, ?, ?, ?, ?)";

			psInsertItem = conn.prepareStatement(insertItemSql);

			for (viewmodel.CartItem ci : cart) {
				psInsertItem.setLong(1, orderId);
				psInsertItem.setString(2, String.valueOf(ci.getGoodsId())); // menu_id は VARCHAR2
				psInsertItem.setString(3, ci.getGoodsName());
				psInsertItem.setInt(4, ci.getPrice());
				psInsertItem.setInt(5, ci.getQuantity());
				psInsertItem.addBatch();
			}

			psInsertItem.executeBatch(); // バルクINSERT

			//━━━━━━━━━━━━━━━━━━━━━
			// 5) コミット
			//━━━━━━━━━━━━━━━━━━━━━
			conn.commit();

			return orderId;

		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			throw e;

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (psSeq != null)
				try {
					psSeq.close();
				} catch (SQLException e) {
				}
			if (psInsertOrder != null)
				try {
					psInsertOrder.close();
				} catch (SQLException e) {
				}
			if (psInsertItem != null)
				try {
					psInsertItem.close();
				} catch (SQLException e) {
				}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * 既存の orderId に対して order_items を登録する
	 */
	public void insertOrderItems(int orderId, List<CartItem> cart) throws Exception {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = DBManager.getConnection();
			conn.setAutoCommit(false);

			String sql = "INSERT INTO order_items "
					+ "(order_item_id, order_id, menu_id, goods_name, price, quantity) "
					+ "VALUES (seq_order_items.nextval, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(sql);

			for (CartItem ci : cart) {
				ps.setInt(1, orderId);
				ps.setString(2, String.valueOf(ci.getGoodsId()));
				ps.setString(3, ci.getGoodsName());
				ps.setInt(4, ci.getPrice());
				ps.setInt(5, ci.getQuantity());
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();

		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			throw e;

		} finally {
			if (ps != null)
				ps.close();
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}

	}
}
