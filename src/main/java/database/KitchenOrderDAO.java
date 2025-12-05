package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.KitchenOrderDTO;

public class KitchenOrderDAO {

	public List<KitchenOrderDTO> findAll() throws Exception {
		List<KitchenOrderDTO> list = new ArrayList<>();

		String sql = """
				SELECT
				    o.order_id,
				    o.table_no,
				    o.order_date,
				    i.goods_name,
				    i.quantity
				FROM orders o
				JOIN order_items i
				  ON o.order_id = i.order_id
				ORDER BY o.order_id DESC
				""";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				model.dto.KitchenOrderDTO dto = new KitchenOrderDTO();
				dto.setOrderId(rs.getLong("order_id"));
				dto.setTableNo(rs.getString("table_no"));
				dto.setOrderDate(rs.getTimestamp("order_date"));
				dto.setGoodsName(rs.getString("goods_name"));
				dto.setQuantity(rs.getInt("quantity"));

				list.add(dto);
			}
		}

		return list;
	}

}
	