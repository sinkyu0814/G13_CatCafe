package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DBManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.OrderItem;

/**
 * Servlet implementation class CheckServlet
 */
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CheckServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// --- 1. テーブル番号の取得 ---
		HttpSession session = request.getSession();
		String tableNumberStr = request.getParameter("tableNumber");

		// リクエストになければセッションから取得（History画面から戻ってきた時など）
		if (tableNumberStr == null) {
			tableNumberStr = (String) session.getAttribute("tableNumber");
		}

		// それでもなければデフォルト "1" (エラー処理は要件に合わせて)
		if (tableNumberStr == null || tableNumberStr.isEmpty()) {
			tableNumberStr = "1";
		}

		List<OrderItem> orderedItems = new ArrayList<>();
		int totalAmount = 0;

		// --- 2. DBから注文データを取得 ---
		try (Connection conn = DBManager.getConnection()) {
			// orders, order_items, menus を結合
			// status = 'NEW' (未会計) の注文のみ対象
			String sql = "SELECT "
					+ "  o.order_id, o.order_time, o.status, o.table_no, "
					+ "  oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity, "
					+ "  m.image "
					+ "FROM orders o "
					+ "JOIN order_items oi ON o.order_id = oi.order_id "
					+ "LEFT JOIN menus m ON oi.menu_id = m.menu_id "
					+ "WHERE o.table_no = ? AND o.status = 'NEW' "
					+ "ORDER BY oi.order_item_id ASC";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				// テーブル番号をセット
				ps.setInt(1, Integer.parseInt(tableNumberStr));

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						OrderItem item = new OrderItem();

						// OrderItemクラス (DB対応版) にセット
						item.setOrderItemId(rs.getInt("order_item_id"));
						item.setOrderId(rs.getInt("order_id"));
						item.setMenuId(rs.getString("menu_id"));
						item.setName(rs.getString("goods_name")); // JSPの ${item.name} に対応
						item.setPrice(rs.getInt("price"));
						item.setQuantity(rs.getInt("quantity"));
						item.setImage(rs.getString("image")); // 画像ファイル名

						item.setTableNo(rs.getInt("table_no"));

						orderedItems.add(item);

						// 合計金額を加算 (価格 * 個数)
						totalAmount += item.getSubtotal();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("DB Access Error", e);
		}

		// --- 3. リクエストへセット ---
		request.setAttribute("items", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNumber", tableNumberStr); // 画面遷移用にテーブル番号も渡す

		// --- 4. JSPへフォワード ---
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/check.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}