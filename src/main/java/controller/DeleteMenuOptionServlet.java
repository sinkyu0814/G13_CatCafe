package controller;

import java.io.IOException;

import database.MenuOptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteMenuOptionServlet")
public class DeleteMenuOptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		// JSPの <input name="optionId"> から値を取得
		String idStr = request.getParameter("optionId");

		if (idStr != null && !idStr.isEmpty()) {
			int optionId = Integer.parseInt(idStr);
			MenuOptionDAO dao = new MenuOptionDAO();
			try {
				dao.delete(optionId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// メニュー一覧画面へ戻る
		response.sendRedirect("AddMenuServlet");
	}
}
