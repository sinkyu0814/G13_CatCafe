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
import viewmodel.OrderItem;

//@WebServlet("/AccountingServlet")
public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// =========================
	// 会計画面表示
	// =========================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tableNoStr = request.getParameter("tableNo");

		// 未入力対策
		if (tableNoStr == null || tableNoStr.isEmpty()) {
			request.setAttribute("error", "テーブル番号を指定してください");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
					.forward(request, response);
			return;
		}

		int tableNo = Integer.parseInt(tableNoStr);

		List<OrderItem> list = new ArrayList<>();
		int total = 0;

		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					SELECT oi.goods_name, oi.price, oi.quantity
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					WHERE o.table_no = ? AND o.status = 'NEW'
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, tableNo);

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						OrderItem item = new OrderItem();
						item.setName(rs.getString("goods_name"));
						item.setPrice(rs.getInt("price"));
						item.setQuantity(rs.getInt("quantity"));

						list.add(item);
						total += item.getSubtotal();
					}
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("orderList", list);
		request.setAttribute("totalAmount", total);
		request.setAttribute("tableNo", tableNo);

		request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
				.forward(request, response);
	}

	// =========================
	// 会計確定
	// =========================
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tableNoStr = request.getParameter("tableNo");

		// ---- null / 未入力チェック ----
		if (tableNoStr == null || tableNoStr.isEmpty()) {
			request.setAttribute("error", "テーブル番号が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
					.forward(request, response);
			return;
		}

		int tableNo;
		try {
			tableNo = Integer.parseInt(tableNoStr);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "テーブル番号が不正です");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp")
					.forward(request, response);
			return;
		}

		// ---- 会計処理 ----
		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					UPDATE orders
					SET status = 'PAID'
					WHERE table_no = ? AND status = 'NEW'
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, tableNo);
				ps.executeUpdate();
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		// 完了画面
		request.setAttribute("tableNo", tableNo);
		request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp")
				.forward(request, response);
	}
}
