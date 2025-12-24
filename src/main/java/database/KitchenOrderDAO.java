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

	public List<KitchenOrderDTO> findAll() throws Exception {
		// order_item_id をキーにして、重複する商品行をまとめつつオプションをリスト化する
		Map<Integer, KitchenOrderDTO> map = new LinkedHashMap<>();

		String sql = """
				SELECT
				    o.order_id, o.table_no, o.order_date,
				    i.order_item_id, i.goods_name, i.quantity,
				    oio.option_name
				FROM orders o
				JOIN order_items i ON o.order_id = i.order_id
				LEFT JOIN order_item_options oio ON i.order_item_id = oio.order_item_id
				WHERE o.status = 'NEW' OR o.status = 'COOKING'
				ORDER BY o.order_date ASC, i.order_item_id ASC
				""";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int itemId = rs.getInt("order_item_id");
				KitchenOrderDTO dto = map.get(itemId);

				if (dto == null) {
					dto = new KitchenOrderDTO();
					dto.setOrderId(rs.getLong("order_id"));
					dto.setTableNo(rs.getString("table_no"));
					dto.setOrderDate(rs.getTimestamp("order_date"));
					dto.setGoodsName(rs.getString("goods_name"));
					dto.setQuantity(rs.getInt("quantity"));
					// 文字列のリストとして初期化
					dto.setOptions(new ArrayList<String>());
					map.put(itemId, dto);
				}

				String optName = rs.getString("option_name");
				if (optName != null) {
					// String型なのでそのまま追加可能
					dto.getOptions().add(optName);
				}
			}
		}
		return new ArrayList<>(map.values());
	}
}