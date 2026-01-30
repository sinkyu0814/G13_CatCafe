package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dto.MenuOptionDTO;
import viewmodel.OrderItem;

public class AccountingDAO {
	// 注文詳細と合計金額の取得
	public Map<String, Object> getOrderDetails(int orderId) throws Exception {
		List<OrderItem> list = new ArrayList<>();
		int tableNo = 0;

		String sql = """
				SELECT o.table_no, oi.order_item_id, oi.goods_name, oi.price, oi.quantity,
				       oio.option_name, oio.option_price
				FROM orders o
				JOIN order_items oi ON o.order_id = oi.order_id
				LEFT JOIN order_item_options oio ON oi.order_item_id = oio.order_item_id
				WHERE o.order_id = ? AND o.status IN ('NEW', 'CHECKOUT_REQUEST')
				ORDER BY oi.order_item_id ASC
				""";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			try (ResultSet rs = ps.executeQuery()) {
				Map<Integer, OrderItem> itemMap = new LinkedHashMap<>();
				while (rs.next()) {
					int itemId = rs.getInt("order_item_id");
					tableNo = rs.getInt("table_no");
					OrderItem item = itemMap.computeIfAbsent(itemId, id -> {
						OrderItem oi = new OrderItem();
						oi.setOrderItemId(id);
						try {
							oi.setName(rs.getString("goods_name"));
							oi.setPrice(rs.getInt("price"));
							oi.setQuantity(rs.getInt("quantity"));
						} catch (SQLException e) {
						}
						oi.setSelectedOptions(new ArrayList<>());
						return oi;
					});

					String optName = rs.getString("option_name");
					if (optName != null) {
						MenuOptionDTO opt = new MenuOptionDTO();
						opt.setOptionName(optName);
						opt.setOptionPrice(rs.getInt("option_price"));
						item.getSelectedOptions().add(opt);
					}
				}
				Map<String, Object> result = new HashMap<>();
				result.put("list", new ArrayList<>(itemMap.values()));
				result.put("tableNo", tableNo);
				return result;
			}
		}
	}

	// 会計ステータスの更新
	public void updateStatusToPaid(int orderId) throws Exception {
		String sql = "UPDATE orders SET status = 'PAID' WHERE order_id = ?";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			ps.executeUpdate();
		}
	}

	// テーブル番号から注文IDを検索
	public int findActiveOrderIdByTable(int tableNo) throws Exception {
		String sql = "SELECT order_id FROM orders WHERE table_no = ? AND status IN ('NEW', 'CHECKOUT_REQUEST')";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, tableNo);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getInt("order_id") : -1;
			}
		}
	}
}