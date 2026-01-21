package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/FinishSessionServlet")
public class FinishSessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		if (session != null) {
			// ★ 重要：fixedTableNo（店員設定）以外をすべて消去する
			session.removeAttribute("orderId");
			session.removeAttribute("isPaid");
			session.removeAttribute("persons");
			session.removeAttribute("tableNo");
		}

		// これで初めて、ToppageServletのガードが外れて次の「人数入力」ができるようになる
		response.sendRedirect("ToppageServlet");
	}
}