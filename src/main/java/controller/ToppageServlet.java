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
		HttpSession session = request.getSession();

		int persons;
		int tableNo;

		// 1. 未入力チェック
		if (personsStr == null || personsStr.isEmpty()) {
			request.setAttribute("error", "人数を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// 2. ★ 特殊コード判定（人数入力欄に 2136 と打たれた場合）
		if ("2136".equals(personsStr)) {
			request.getRequestDispatcher("/WEB-INF/jsp/AdminTableSet.jsp")
					.forward(request, response);
			return;
		}

		// 3. ★ セッションから固定テーブル番号を取得
		String fixedTableNo = (String) session.getAttribute("fixedTableNo");
		if (fixedTableNo == null || fixedTableNo.isEmpty()) {
			request.setAttribute("error", "店員を呼んでください（テーブル未設定）");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp")
					.forward(request, response);
			return;
		}

		// 4. 数値チェックと範囲チェック
		try {
			persons = Integer.parseInt(personsStr);
			tableNo = Integer.parseInt(fixedTableNo); // セッションの値を数値に変換
		} catch (NumberFormatException e) {
			request.setAttribute("error", "正しい数値を入力してください");
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

		// セッションに情報をセット
		session.setAttribute("persons", persons);
		session.setAttribute("tableNo", tableNo);

		// 5. orders レコード作成
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

		// 注文一覧画面へリダイレクト
		response.sendRedirect("ListServlet");
	}
}