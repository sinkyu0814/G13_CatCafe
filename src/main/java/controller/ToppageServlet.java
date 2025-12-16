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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
				.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		int persons = Integer.parseInt(request.getParameter("persons"));
		int tableNo = Integer.parseInt(request.getParameter("tableNo"));

		HttpSession session = request.getSession();
		session.setAttribute("persons", persons);
		session.setAttribute("tableNo", tableNo);

		// --- DBに注文（orders）を作成 ---
		try (Connection conn = DBManager.getConnection()) {

			// order_id 採番＋INSERT
			String sql = """
					    INSERT INTO orders (
					        order_id, order_time, status, table_no, persons, order_date
					    ) VALUES (
					        seq_orders.NEXTVAL, SYSTIMESTAMP, 'NEW', ?, ?, SYSDATE
					    )
					""";

			PreparedStatement ps = conn.prepareStatement(sql, new String[] { "order_id" });
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

		} catch (Exception e) {
			throw new ServletException(e);
		}

		// メニュー一覧へ
		response.sendRedirect("ListServlet");
	}
}
