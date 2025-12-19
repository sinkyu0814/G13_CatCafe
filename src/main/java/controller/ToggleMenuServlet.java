package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MenuService;

public class ToggleMenuServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
