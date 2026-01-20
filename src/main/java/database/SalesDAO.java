package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dto.SalesDTO;

public class SalesDAO {

	public List<SalesDTO> getSalesData(String type, String year, String month) {
		Map<String, SalesDTO> dbDataMap = new HashMap<>();

		// 売上計算（商品代＋オプション代）
		String priceCalc = "SUM((i.price * i.quantity) + " +
				"NVL((SELECT SUM(oio.option_price) FROM ORDER_ITEM_OPTIONS oio " +
				"WHERE oio.order_item_id = i.order_item_id), 0))";

		// 【修正完了】DBのカラム名 persons を使用
		String visitorCalc = "SUM(o.persons)";

		String sql = "";
		if ("year".equals(type)) {
			sql = "SELECT TO_CHAR(o.order_time, 'YYYY') AS d, " + priceCalc + " AS s, " + visitorCalc + " AS v " +
					"FROM orders o JOIN order_items i ON o.order_id = i.order_id GROUP BY TO_CHAR(o.order_time, 'YYYY') ORDER BY d DESC";
		} else if ("month".equals(type)) {
			sql = "SELECT TO_CHAR(o.order_time, 'MM') AS d, " + priceCalc + " AS s, " + visitorCalc + " AS v " +
					"FROM orders o JOIN order_items i ON o.order_id = i.order_id WHERE TO_CHAR(o.order_time, 'YYYY') = ? GROUP BY TO_CHAR(o.order_time, 'MM')";
		} else {
			sql = "SELECT TO_CHAR(o.order_time, 'DD') AS d, " + priceCalc + " AS s, " + visitorCalc + " AS v " +
					"FROM orders o JOIN order_items i ON o.order_id = i.order_id WHERE TO_CHAR(o.order_time, 'YYYY') = ? AND TO_CHAR(o.order_time, 'MM') = ? GROUP BY TO_CHAR(o.order_time, 'DD')";
		}

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			if ("month".equals(type)) {
				pstmt.setString(1, year);
			} else if ("day".equals(type)) {
				pstmt.setString(1, year);
				pstmt.setString(2, month);
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String d = rs.getString("d");
					dbDataMap.put(d, new SalesDTO(d, rs.getInt("s"), rs.getInt("v")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SalesDTO> fullList = new ArrayList<>();
		if ("year".equals(type)) {
			List<String> years = new ArrayList<>(dbDataMap.keySet());
			Collections.sort(years, Collections.reverseOrder());
			for (String y : years) {
				SalesDTO dto = dbDataMap.get(y);
				fullList.add(new SalesDTO(y + "年", dto.getTotalAmount(), dto.getVisitorCount()));
			}
		} else if ("month".equals(type)) {
			for (int i = 1; i <= 12; i++) {
				String key = String.format("%02d", i);
				SalesDTO dto = dbDataMap.getOrDefault(key, new SalesDTO(key, 0, 0));
				fullList.add(new SalesDTO(i + "月", dto.getTotalAmount(), dto.getVisitorCount()));
			}
		} else {
			try {
				int yNum = Integer.parseInt(year);
				int mNum = Integer.parseInt(month);
				int lastDay = YearMonth.of(yNum, mNum).lengthOfMonth();
				for (int i = 1; i <= lastDay; i++) {
					String key = String.format("%02d", i);
					int dow = LocalDate.of(yNum, mNum, i).getDayOfWeek().getValue();
					SalesDTO dto = dbDataMap.getOrDefault(key, new SalesDTO(key, 0, 0));
					fullList.add(new SalesDTO(i + "日", dto.getTotalAmount(), dto.getVisitorCount(), dow));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fullList;
	}
}