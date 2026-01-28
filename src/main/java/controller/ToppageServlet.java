package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

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

		// 1. 言語設定の処理
		String lang = request.getParameter("lang");
		Locale locale;
		if (lang != null && !lang.isEmpty()) {
			locale = new Locale(lang);
		} else {
			// セッションに既存のロケールがあればそれを使用、なければデフォルト
			locale = (Locale) Config.get(session, Config.FMT_LOCALE);
			if (locale == null)
				locale = Locale.JAPANESE;
		}
		// JSTLのfmtタグ用ロケールを設定
		Config.set(session, Config.FMT_LOCALE, locale);

		// 2. プロパティファイル(messages)を読み込むための準備
		// パッケージ名を含めて指定 (properties.messages)
		ResourceBundle bundle = ResourceBundle.getBundle("properties.messages", locale);

		String personsStr = request.getParameter("persons");

		// すでに注文IDがある場合は二重登録防止のためリダイレクト
		if (session.getAttribute("orderId") != null) {
			response.sendRedirect("ToppageServlet");
			return;
		}

		// --- バリデーションチェック（メッセージをプロパティから取得） ---

		// 未入力チェック
		if (personsStr == null || personsStr.isEmpty()) {
			request.setAttribute("error", bundle.getString("error.enter_persons"));
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		// 管理者用テーブル設定画面への隠しコマンド
		if ("2136".equals(personsStr)) {
			request.getRequestDispatcher("/WEB-INF/jsp/AdminTableSet.jsp").forward(request, response);
			return;
		}

		// テーブル番号設定チェック
		String fixedTableNo = (String) session.getAttribute("fixedTableNo");
		if (fixedTableNo == null || fixedTableNo.isEmpty()) {
			request.setAttribute("error", bundle.getString("error.call_staff"));
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		int persons, tableNo;
		try {
			persons = Integer.parseInt(personsStr);
			tableNo = Integer.parseInt(fixedTableNo);
		} catch (NumberFormatException e) {
			request.setAttribute("error", bundle.getString("error.invalid_number"));
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		// 人数範囲チェック
		if (persons < 1 || persons > 10) {
			request.setAttribute("error", bundle.getString("error.persons_range"));
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		// -------------------------
		// DB登録処理
		// -------------------------
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

		// 登録完了後は doGet を通して ListServlet へ
		response.sendRedirect("ToppageServlet");
	}
}