package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.dto.KitchenOrderDTO;

public class KitchenOrderDAO {

	// キッチン表示用（提供済み以外）
	public List<KitchenOrderDTO> findActiveOrders() throws Exception {
		return findByStatus("NOT_DELIVERED");
	}

	// 履歴表示用（提供済みのみ）
	public List<KitchenOrderDTO> findDeliveredOrders() throws Exception {
		return findByStatus("DELIVERED");
	}
	
	
	private List<KitchenOrderDTO> findByStatus(String mode) throws Exception {
		Map<Integer, KitchenOrderDTO> map = new LinkedHashMap<>();
		String statusCondition = mode.equals("DELIVERED") ? "= 'DELIVERED'" : "!= 'DELIVERED'";

		String sql = "SELECT o.order_id, o.table_no, o.order_date, i.order_item_id, i.goods_name, i.quantity, i.kitchen_status, oio.option_name "
				+
				"FROM orders o JOIN order_items i ON o.order_id = i.order_id " +
				"LEFT JOIN order_item_options oio ON i.order_item_id = oio.order_item_id " +
				"WHERE i.kitchen_status " + statusCondition + " AND o.status != 'PAID' " +
				"ORDER BY o.order_date ASC, i.order_item_id ASC";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int itemId = rs.getInt("order_item_id");
				KitchenOrderDTO dto = map.get(itemId);
				if (dto == null) {
					dto = new KitchenOrderDTO();
					dto.setOrderId(rs.getLong("order_id"));
					dto.setOrderItemId(itemId);
					dto.setTableNo(rs.getString("table_no"));
					dto.setOrderDate(rs.getTimestamp("order_date"));
					dto.setGoodsName(rs.getString("goods_name"));
					dto.setQuantity(rs.getInt("quantity"));
					dto.setKitchenStatus(rs.getString("kitchen_status"));
					dto.setOptions(new ArrayList<>());
					map.put(itemId, dto);
				}
				String opt = rs.getString("option_name");
				if (opt != null)
					dto.getOptions().add(opt);
			}
		}
		return new ArrayList<>(map.values());
	}

	// ステータス更新
	public void updateStatus(int orderItemId, String status) throws Exception {
		String sql = "UPDATE order_items SET kitchen_status = ? WHERE order_item_id = ?";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ps.setInt(2, orderItemId);
			ps.executeUpdate();
		}
	}

	// 履歴からの物理削除
	public void deleteOrderItem(int orderItemId) throws Exception {
		// オプションを先に消す必要がある(外部キー制約)
		String sqlOpt = "DELETE FROM order_item_options WHERE order_item_id = ?";
		String sqlItem = "DELETE FROM order_items WHERE order_item_id = ?";
		try (Connection conn = DBManager.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps1 = conn.prepareStatement(sqlOpt);
					PreparedStatement ps2 = conn.prepareStatement(sqlItem)) {
				ps1.setInt(1, orderItemId);
				ps1.executeUpdate();
				ps2.setInt(1, orderItemId);
				ps2.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		}
	}

	public void deleteAllDeliveredItems() throws Exception {
		String sqlOpt = "DELETE FROM order_item_options WHERE order_item_id IN " +
				"(SELECT order_item_id FROM order_items WHERE kitchen_status = 'DELIVERED')";
		String sqlItem = "DELETE FROM order_items WHERE kitchen_status = 'DELIVERED'";

		try (Connection conn = DBManager.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps1 = conn.prepareStatement(sqlOpt);
					PreparedStatement ps2 = conn.prepareStatement(sqlItem)) {
				ps1.executeUpdate();
				ps2.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
		}
	}
	
}