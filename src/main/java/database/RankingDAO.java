package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.RankingDTO;

public class RankingDAO {

	/**
	 * 指定期間内・指定タイプの売れ筋TOP10を取得します
	 * @param startDate 集計開始日時 (yyyy-MM-dd HH:mm:ss)
	 * @param filterType 集計対象 (option: オプション, all/item: 商品)
	 */
	public List<RankingDTO> getTopTenRanking(String startDate, String filterType) {
		List<RankingDTO> list = new ArrayList<>();
		String sql;

		if ("option".equals(filterType)) {
			// オプションランキング
			// オプションが紐づいている商品の数量(i.quantity)を合算することで、実質的な提供数を算出
			sql = "SELECT oio.option_name AS name, SUM(i.quantity) AS total_qty " +
					"FROM ORDER_ITEM_OPTIONS oio " +
					"JOIN order_items i ON oio.order_item_id = i.order_item_id " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') " +
					"GROUP BY oio.option_name ORDER BY total_qty DESC FETCH FIRST 10 ROWS ONLY";
		} else {
			// 商品ランキング (セットメニュー・単品商品)
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

	/**
	 * 指定期間内・指定タイプの総注文(提供)数を取得します
	 * @param startDate 集計開始日時
	 * @param filterType 集計対象
	 */
	public int getTotalOrderCount(String startDate, String filterType) {
		String sql;
		if ("option".equals(filterType)) {
			// オプションの総提供数
			sql = "SELECT SUM(i.quantity) FROM ORDER_ITEM_OPTIONS oio " +
					"JOIN order_items i ON oio.order_item_id = i.order_item_id " +
					"JOIN orders o ON i.order_id = o.order_id " +
					"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";
		} else {
			// 商品の総注文数
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