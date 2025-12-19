package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.OrderItem;

public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// =========================
	// 会計画面表示（GET）
	// =========================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", "注文情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
					.forward(request, response);
			return;
		}

		List<OrderItem> list = new ArrayList<>();
		int totalAmount = 0;
		int tableNo = 0;

		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					SELECT o.table_no,
						   oi.goods_name,
						   oi.price,
						   oi.quantity
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					WHERE o.order_id = ? AND o.status = 'NEW'
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {

						tableNo = rs.getInt("table_no");

						OrderItem item = new OrderItem();
						item.setName(rs.getString("goods_name"));
						item.setPrice(rs.getInt("price"));
						item.setQuantity(rs.getInt("quantity"));

						list.add(item);
						totalAmount += item.getSubtotal();
					}
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("orderList", list);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo);

		request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
				.forward(request, response);
	}

	// =========================
	// 会計確定（POST）
	// =========================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		// =========================
		// 会計開始（TableSelect → Accounting）
		// =========================
		if ("startAccounting".equals(action)) {

			String tableStr = request.getParameter("tableNumber");
			if (tableStr == null) {
				throw new ServletException("テーブル番号が送信されていません");
			}

			int tableNo = Integer.parseInt(tableStr.replace("番", ""));

			HttpSession session = request.getSession();

			// tableNo から NEW の orderId を取得
			try (Connection conn = DBManager.getConnection()) {

				String sql = """
						    SELECT order_id
						    FROM orders
						    WHERE table_no = ? AND status = 'NEW'
						""";

				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setInt(1, tableNo);

					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							session.setAttribute("orderId", rs.getInt("order_id"));
							session.setAttribute("tableNo", tableNo);
						} else {
							throw new ServletException("未会計の注文がありません");
						}
					}
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}

			// ★ 会計画面表示へ（GET）
			response.sendRedirect("AccountingServlet");
			return;
		}

		// =========================
		// 会計確定（Accounting.jsp → Complete）
		// =========================

		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");
		Integer tableNo = (Integer) session.getAttribute("tableNo");

		if (orderId == null) {
			throw new ServletException("会計対象の注文が見つかりません");
		}

		int totalAmount = Integer.parseInt(request.getParameter("totalAmount"));
		int deposit = Integer.parseInt(request.getParameter("deposit"));
		int change = deposit - totalAmount;

		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					    UPDATE orders
					    SET status = 'PAID'
					    WHERE order_id = ?
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("tableNo", tableNo);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("deposit", deposit);
		request.setAttribute("change", change);

		session.removeAttribute("orderId");
		session.removeAttribute("tableNo");
		session.removeAttribute("persons");

		request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp")
				.forward(request, response);
	}

}
