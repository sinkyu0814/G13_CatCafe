package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// =========================
	// 会計完了画面表示
	// =========================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		Integer orderId = null;
		Integer tableNo = null;

		// --- 1. orderId を取得 ---
		if (session != null) {
			orderId = (Integer) session.getAttribute("orderId");
		}

		if (orderId == null) {
			request.setAttribute("error", "会計情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp")
					.forward(request, response);
			return;
		}

		// --- 2. orderId からテーブル番号を取得 ---
		try (Connection conn = DBManager.getConnection()) {

			String sql = """
					SELECT table_no
					FROM orders
					WHERE order_id = ?
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						tableNo = rs.getInt("table_no");
					}
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		// --- 3. 表示用データをリクエストへ ---
		request.setAttribute("orderId", orderId);
		request.setAttribute("tableNo", tableNo);

		// --- 4. JSPへ ---
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
