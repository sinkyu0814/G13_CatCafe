package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.dto.MenuOptionDTO;
import viewmodel.CartItem;

public class CartDAO {

	public long insertOrder(List<CartItem> cart, String tableNo) throws Exception {

		for (CartItem ci : cart) {
			if (ci.getQuantity() > 15) {
				throw new Exception(ci.getGoodsName() + " の数量が15個を超えています。一度に注文できるのは15個までです。");
			}
		}

		Connection conn = null;
		PreparedStatement psSeq = null;
		PreparedStatement psInsertOrder = null;
		PreparedStatement psInsertItem = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			conn.setAutoCommit(false);

			String seqSql = "SELECT seq_orders.nextval FROM dual";
			psSeq = conn.prepareStatement(seqSql);
			rs = psSeq.executeQuery();

			long orderId;
			if (rs.next()) {
				orderId = rs.getLong(1);
			} else {
				throw new SQLException("seq_orders の取得に失敗しました");
			}

			// ★修正：mapToLongを使って21億超えに対応
			long totalAmount = cart.stream()
					.mapToLong(ci -> (long) ci.getPrice() * ci.getQuantity())
					.sum();

			String insertOrderSql = "INSERT INTO orders (order_id, table_no, order_date, total_amount) "
					+ "VALUES (?, ?, SYSDATE, ?)";
			psInsertOrder = conn.prepareStatement(insertOrderSql);
			psInsertOrder.setLong(1, orderId);
			psInsertOrder.setInt(2, Integer.parseInt(tableNo));
			// ★setLongを使う（DB側がNUMBER(18)ならこれで安全）
			psInsertOrder.setLong(3, totalAmount);
			psInsertOrder.executeUpdate();

			String insertItemSql = "INSERT INTO order_items "
					+ "(order_item_id, order_id, menu_id, goods_name, price, quantity) "
					+ "VALUES (seq_order_items.nextval, ?, ?, ?, ?, ?)";

			psInsertItem = conn.prepareStatement(insertItemSql);

			for (viewmodel.CartItem ci : cart) {
				psInsertItem.setLong(1, orderId);
				psInsertItem.setString(2, String.valueOf(ci.getGoodsId()));
				psInsertItem.setString(3, ci.getGoodsName());
				psInsertItem.setInt(4, ci.getPrice());
				psInsertItem.setInt(5, ci.getQuantity());
				psInsertItem.addBatch();
			}

			psInsertItem.executeBatch();
			conn.commit();

			return orderId;

		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			throw e;
		} finally {
			// (以下、close処理は元のコードと同じため省略)
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

	public void insertOrderItems(int orderId, List<CartItem> cart) throws Exception {
		for (CartItem ci : cart) {
			if (ci.getQuantity() > 15) {
				throw new Exception(ci.getGoodsName() + " の数量が15個を超えています。一度に注文できるのは15個までです。");
			}
		}

		Connection conn = null;
		PreparedStatement psItem = null;
		PreparedStatement psOpt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			conn.setAutoCommit(false);

			String sqlItem = "INSERT INTO order_items (order_item_id, order_id, menu_id, goods_name, price, quantity) "
					+ "VALUES (seq_order_items.nextval, ?, ?, ?, ?, ?)";
			psItem = conn.prepareStatement(sqlItem, new String[] { "order_item_id" });

			String sqlOpt = "INSERT INTO order_item_options (order_item_id, option_id, option_name, option_price) "
					+ "VALUES (?, ?, ?, ?)";
			psOpt = conn.prepareStatement(sqlOpt);

			for (CartItem ci : cart) {
				psItem.setInt(1, orderId);
				psItem.setString(2, String.valueOf(ci.getGoodsId()));
				psItem.setString(3, ci.getGoodsName());
				psItem.setInt(4, ci.getPrice());
				psItem.setInt(5, ci.getQuantity());
				psItem.executeUpdate();

				rs = psItem.getGeneratedKeys();
				if (rs.next()) {
					int newItemId = rs.getInt(1);
					if (ci.getSelectedOptions() != null && !ci.getSelectedOptions().isEmpty()) {
						for (MenuOptionDTO opt : ci.getSelectedOptions()) {
							psOpt.setInt(1, newItemId);
							psOpt.setInt(2, opt.getOptionId());
							psOpt.setString(3, opt.getOptionName());
							psOpt.setInt(4, opt.getOptionPrice());
							psOpt.executeUpdate();
						}
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (psItem != null)
				try {
					psItem.close();
				} catch (Exception e) {
				}
			if (psOpt != null)
				try {
					psOpt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}
}