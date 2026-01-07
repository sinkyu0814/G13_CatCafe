package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebServlet("/ToppageServlet")
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

		// 未入力チェック
		if (personsStr == null || personsStr.isEmpty()
				|| tableNoStr == null || tableNoStr.isEmpty()) {

			request.setAttribute("error", "人数とテーブル番号を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// 数値チェック
		try {
			persons = Integer.parseInt(personsStr);
			tableNo = Integer.parseInt(tableNoStr);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "正しい数値を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// 範囲チェック
		if (tableNo < 1 || tableNo > 25) {
			request.setAttribute("error", "テーブル番号は1～25番です");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		if (persons < 1 || persons > 10) {
			request.setAttribute("error", "人数は1～10人です");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("persons", persons);
		session.setAttribute("tableNo", tableNo);

		// orders 作成
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

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						session.setAttribute("orderId", rs.getInt(1));
					}
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		response.sendRedirect("ListServlet");
	}
}
