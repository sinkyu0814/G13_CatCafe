package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.dto.RankingDTO;

public class RankingDAO {

	public List<RankingDTO> getTopTenRanking(String startDate) {
		List<RankingDTO> list = new ArrayList<>();

		// ★修正ポイント：Oracleでは LIMIT 10 ではなく FETCH FIRST 10 ROWS ONLY を使います
		String sql = "SELECT i.goods_name, SUM(i.quantity) AS total_qty " +
				"FROM order_items i " +
				"JOIN orders o ON i.order_id = o.order_id " +
				"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') " +
				"GROUP BY i.goods_name " +
				"ORDER BY total_qty DESC " +
				"FETCH FIRST 10 ROWS ONLY";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, startDate);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(new RankingDTO(rs.getString("goods_name"), rs.getInt("total_qty")));
				}
			}
		} catch (Exception e) {
			System.err.println("--- RankingDAO 取得エラー ---");
			System.err.println("理由: " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	public int getTotalOrderCount(String startDate) {
		// ★修正ポイント：日付比較をTO_TIMESTAMPで確実に行うようにしています
		String sql = "SELECT SUM(i.quantity) FROM order_items i " +
				"JOIN orders o ON i.order_id = o.order_id " +
				"WHERE o.order_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, startDate);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("--- RankingDAO 合計計算エラー ---");
			e.printStackTrace();
		}
		return 0;
	}
}