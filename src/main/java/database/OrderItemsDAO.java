package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemsDAO {

	public void insertItem(Connection conn, int orderId, viewmodel.CartItem item) throws SQLException {

		String sql = "INSERT INTO order_items "
				+ "(order_item_id, order_id, menu_id, goods_name, price, quantity) "
				+ "VALUES (seq_order_items.NEXTVAL, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, orderId);
			stmt.setInt(2, item.getGoodsId());
			stmt.setString(3, item.getGoodsName());
			stmt.setInt(4, item.getPrice());
			stmt.setInt(5, item.getQuantity());

			stmt.executeUpdate();
		}
	}
}
