package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.RankingDTO;

public class RankingDAO {

	public List<RankingDTO> getTopTenRanking(String startDate, String filterType) {
		List<RankingDTO> list = new ArrayList<>();
		String sql;

		if ("option".equals(filterType)) {
			// オプションランキング (名前は option_name)
			sql = "SELECT oio.option_name AS name, COUNT(*) AS total_qty " +
					"FROM ORDER_ITEM_OPTIONS oio " +
					"JOIN order_items i ON oio.order_item_id = i.order_item_id " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') " +
					"GROUP BY oio.option_name ORDER BY total_qty DESC FETCH FIRST 10 ROWS ONLY";
		} else {
			// 商品ランキング (名前は goods_name) ※セットでも数量ベースなら商品は同じ
			sql = "SELECT i.goods_name AS name, SUM(i.quantity) AS total_qty " +
					"FROM order_items i " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') " +
					"GROUP BY i.goods_name ORDER BY total_qty DESC FETCH FIRST 10 ROWS ONLY";
		}

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, startDate);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(new RankingDTO(rs.getString("name"), rs.getInt("total_qty")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getTotalOrderCount(String startDate, String filterType) {
		String sql;
		if ("option".equals(filterType)) {
			sql = "SELECT COUNT(*) FROM ORDER_ITEM_OPTIONS oio " +
					"JOIN order_items i ON oio.order_item_id = i.order_item_id " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
		} else {
			sql = "SELECT SUM(i.quantity) FROM order_items i " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
		}

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, startDate);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}