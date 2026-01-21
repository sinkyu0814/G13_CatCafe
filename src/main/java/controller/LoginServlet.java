package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Login.jspを表示
		request.getRequestDispatcher("/WEB-INF/jsp/Login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user = request.getParameter("username");
		String pass = request.getParameter("password");

		// 認証チェック
		if ("G13".equals(user) && "2136".equals(pass)) {
			HttpSession session = request.getSession();
			session.setAttribute("isLoggedIn", true);

			// セッションに保存された「本来行きたかったURL」を取り出す
			String target = (String) session.getAttribute("targetURI");
			session.removeAttribute("targetURI"); // 一度使ったら消去

			if (target != null && !target.isEmpty()) {
				// A. 特定の画面（レジや管理画面）を直接開こうとしていた場合
				response.sendRedirect(target);
			} else {
				// B. 直接ログイン画面に来てログインした場合は index.html へ
				response.sendRedirect("index.html");
			}
		} else {
			// 認証失敗
			request.setAttribute("error", "ユーザー名またはパスワードが違います");
			request.getRequestDispatcher("/WEB-INF/jsp/Login.jsp").forward(request, response);
		}
	}
}