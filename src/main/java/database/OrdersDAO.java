package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersDAO {

	public int insertOrder(Connection conn) throws SQLException {

		// order_id を seq_orders.nextval の取得で生成
		String sql = "INSERT INTO orders (order_id) VALUES (seq_orders.NEXTVAL)";

		try (PreparedStatement stmt = conn.prepareStatement(sql, new String[] { "order_id" })) {

			stmt.executeUpdate();

			// 生成された order_id を取得
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new SQLException("注文ID取得に失敗");
			}
		}
	}
}
