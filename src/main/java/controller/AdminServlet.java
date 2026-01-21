package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true); // 記録のためにsessionを作成
		if (session.getAttribute("isLoggedIn") == null) {
			// ★ 今のリクエストURLを保存（例: "AdminServlet"）
			String uri = request.getRequestURI();
			String query = request.getQueryString();
			String target = (query == null) ? uri : uri + "?" + query;

			session.setAttribute("targetURI", target);

			response.sendRedirect("LoginServlet");
			return;
		}
		// 管理画面ダッシュボード（JSP）へ移動
		request.getRequestDispatcher("/WEB-INF/jsp/AdminDashboard.jsp").forward(request, response);
	}
}