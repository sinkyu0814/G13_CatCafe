package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.service.MenuService;

@WebServlet("/ToggleMenuServlet")
public class ToggleMenuServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		int id = Integer.parseInt(request.getParameter("menuId"));
		int visible = Integer.parseInt(request.getParameter("visible"));

		try {
			new MenuService().updateVisible(id, visible);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		response.sendRedirect("AddMenuServlet");
	}
}
