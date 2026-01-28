package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

@WebServlet("/ToppageServlet")
public class ToppageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("orderId") != null) {
				if (session.getAttribute("isPaid") != null) {
					request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp").forward(request, response);
				} else {
					response.sendRedirect("ListServlet");
				}
				return;
			}
		}
		request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		// ★ 言語設定の処理を追加
		String lang = request.getParameter("lang");
		if (lang != null && !lang.isEmpty()) {
			Locale locale = new Locale(lang);
			// JSTLのfmtタグが使用するロケールをセッションに固定する
			Config.set(session, Config.FMT_LOCALE, locale);
		}

		String personsStr = request.getParameter("persons");

		if (session.getAttribute("orderId") != null) {
			response.sendRedirect("ToppageServlet");
			return;
		}

		if (personsStr == null || personsStr.isEmpty()) {
			request.setAttribute("error", "人数を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		if ("2136".equals(personsStr)) {
			request.getRequestDispatcher("/WEB-INF/jsp/AdminTableSet.jsp").forward(request, response);
			return;
		}

		String fixedTableNo = (String) session.getAttribute("fixedTableNo");
		if (fixedTableNo == null || fixedTableNo.isEmpty()) {
			request.setAttribute("error", "店員を呼んでください（テーブル未設定）");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		int persons, tableNo;
		try {
			persons = Integer.parseInt(personsStr);
			tableNo = Integer.parseInt(fixedTableNo);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "正しい数値を入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		if (persons < 1 || persons > 10) {
			request.setAttribute("error", "人数は1～10人です");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		session.setAttribute("persons", persons);
		session.setAttribute("tableNo", tableNo);

		try (Connection conn = DBManager.getConnection()) {
			String sql = "INSERT INTO orders (order_id, order_time, status, table_no, persons, order_date) VALUES (seq_orders.NEXTVAL, SYSTIMESTAMP, 'NEW', ?, ?, SYSDATE)";
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
		response.sendRedirect("ToppageServlet");
	}
}