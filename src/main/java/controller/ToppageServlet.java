package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ToppageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
				.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String personsStr = request.getParameter("persons");
		String tableNoStr = request.getParameter("tableNo");

		int persons;
		int tableNo;

		// -------------------------
		// ① 未入力チェック
		// -------------------------
		if (personsStr == null || personsStr.isEmpty()
				|| tableNoStr == null || tableNoStr.isEmpty()) {

			request.setAttribute("error", "1～25番を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// -------------------------
		// ② 数値チェック
		// -------------------------
		try {
			persons = Integer.parseInt(personsStr);
			tableNo = Integer.parseInt(tableNoStr);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "1～25番を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// -------------------------
		// ③ 範囲チェック
		// -------------------------
		if (tableNo < 1 || tableNo > 25 || persons <= 0) {
			request.setAttribute("error", "1～25番を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}
		if (persons < 1 || persons > 10 || persons <= 0) {
			request.setAttribute("error", "1～10人を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// -------------------------
		// ④ 正常処理
		// -------------------------
		HttpSession session = request.getSession();
		session.setAttribute("persons", persons);
		session.setAttribute("tableNo", tableNo);

		// --- DBに注文（orders）を作成 ---
		try (Connection conn = DBManager.getConnection()) {

			String sql = """
						INSERT INTO orders (
							order_id, order_time, status, table_no, persons, order_date
						) VALUES (
							seq_orders.NEXTVAL, SYSTIMESTAMP, 'NEW', ?, ?, SYSDATE
						)
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql, new String[] { "order_id" })) {

				ps.setInt(1, tableNo);
				ps.setInt(2, persons);
				ps.executeUpdate();

				// 採番された order_id を取得
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						int orderId = rs.getInt(1);
						session.setAttribute("orderId", orderId);
					}
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		// メニュー一覧へ
		response.sendRedirect("ListServlet");
	}
}
