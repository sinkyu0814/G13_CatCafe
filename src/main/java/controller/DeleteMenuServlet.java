package controller;

import java.io.IOException;

import database.MenuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String menuIdStr = request.getParameter("menuId");

		if (menuIdStr != null) {
			int menuId = Integer.parseInt(menuIdStr);
			MenuDAO dao = new MenuDAO();
			try {
				dao.deleteMenu(menuId);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		// ★ 同じ画面へ戻す
		response.sendRedirect("AddMenuServlet");
	}
}
