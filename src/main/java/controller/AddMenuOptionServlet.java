package controller;

import java.io.IOException;

import database.MenuOptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AddMenuOptionServlet")
public class AddMenuOptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		try {
			int menuId = Integer.parseInt(request.getParameter("menuId"));
			String name = request.getParameter("optionName");
			int price = Integer.parseInt(request.getParameter("optionPrice"));

			MenuOptionDAO dao = new MenuOptionDAO();
			dao.insert(menuId, name, price);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 追加が終わったら一覧画面へ戻る
		response.sendRedirect("AddMenuServlet");
	}
}