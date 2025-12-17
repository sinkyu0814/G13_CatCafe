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
 * 会計確認画面（orderId 基準）
 */
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// =========================
		// 1. orderId を取得
		// =========================
		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", "注文情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/check.jsp")
					.forward(request, response);
			return;
		}

		List<OrderItem> orderedItems = new ArrayList<>();
		int totalAmount = 0;
		int tableNo = 0;

		// =========================
		// 2. DBから注文データ取得
		// =========================
		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					SELECT
					    o.order_id,
					    o.table_no,
					    oi.order_item_id,
					    oi.menu_id,
					    oi.goods_name,
					    oi.price,
					    oi.quantity,
					    m.image
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					LEFT JOIN menus m ON oi.menu_id = m.menu_id
					WHERE o.order_id = ? AND o.status = 'NEW'
					ORDER BY oi.order_item_id ASC
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						OrderItem item = new OrderItem();

						item.setOrderId(rs.getInt("order_id"));
						item.setOrderItemId(rs.getInt("order_item_id"));
						item.setMenuId(rs.getString("menu_id"));
						item.setName(rs.getString("goods_name"));
						item.setPrice(rs.getInt("price"));
						item.setQuantity(rs.getInt("quantity"));
						item.setImage(rs.getString("image"));

						tableNo = rs.getInt("table_no"); // 表示用

						orderedItems.add(item);
						totalAmount += item.getSubtotal();
					}
				}
			}

		} catch (Exception e) {
			throw new ServletException("DB Access Error", e);
		}

		// =========================
		// 3. JSPへ渡す
		// =========================
		request.setAttribute("items", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo); // 表示用
		request.setAttribute("orderId", orderId);

		// =========================
		// 4. 画面遷移
		// =========================
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/check.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
