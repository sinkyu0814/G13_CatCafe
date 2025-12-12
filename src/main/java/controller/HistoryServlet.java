package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DBManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.OrderItem;

public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HistoryServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. テーブル番号の取得
		// セッションまたはリクエストパラメータから取得する想定
		HttpSession session = request.getSession();

		// 仮：セッションに "tableNumber" が保存されていると仮定
		// 保存されていない場合は、テスト用にリクエストパラメータを見る、あるいは "1" などを入れる
		String tableNumberStr = (String) session.getAttribute("tableNumber");
		if (tableNumberStr == null) {
			tableNumberStr = request.getParameter("tableNumber");
		}

		// それでもなければエラー回避のため仮の番号（本来はエラー画面へ）
		if (tableNumberStr == null) {
			tableNumberStr = "1";
		}

		List<OrderItem> historyList = new ArrayList<>();
		int totalAmount = 0;

		// 2. DBから注文履歴を取得
		try (Connection conn = DBManager.getConnection()) {
			// menusテーブルも結合して画像(image)も取得する
			String sql = "SELECT "
					+ "  o.order_id, o.order_time, o.status, o.table_no, "
					+ "  oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity, "
					+ "  m.image "
					+ "FROM orders o "
					+ "JOIN order_items oi ON o.order_id = oi.order_id "
					+ "LEFT JOIN menus m ON oi.menu_id = m.menu_id " // メニューIDで結合
					+ "WHERE o.table_no = ? AND o.status = 'NEW' " // 未会計のものだけ表示
					+ "ORDER BY o.order_time DESC, oi.order_item_id ASC";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, Integer.parseInt(tableNumberStr));

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						OrderItem item = new OrderItem();
						item.setOrderId(rs.getInt("order_id"));
						item.setOrderTime(rs.getTimestamp("order_time"));
						item.setStatus(rs.getString("status"));
						item.setTableNo(rs.getInt("table_no"));

						item.setOrderItemId(rs.getInt("order_item_id"));
						item.setMenuId(rs.getString("menu_id"));
						item.setName(rs.getString("goods_name")); // JSPに合わせてsetName
						item.setPrice(rs.getInt("price"));
						item.setQuantity(rs.getInt("quantity"));
						item.setImage(rs.getString("image")); // 画像ファイル名

						historyList.add(item);
						totalAmount += item.getSubtotal();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("DB Access Error", e);
		}

		// 3. 栄養データ（ダミー）
		Map<String, String> nutritionData = new HashMap<>();
		nutritionData.put("calories", "1,200kcal");
		nutritionData.put("protein", "45g");

		// 4. JSPへデータを渡す
		request.setAttribute("orderItems", historyList);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("nutritionData", nutritionData);

		// JSP側でテーブル番号が必要な場合があるためセットしておく
		request.setAttribute("tableNumber", tableNumberStr);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/history.jsp");
		dispatcher.forward(request, response);
	}
}