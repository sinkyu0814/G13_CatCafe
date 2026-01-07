package controller;

import java.io.IOException;
import java.util.List;

import database.MenuOptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.MenuDTO;
import model.dto.MenuOptionDTO;
import model.service.MenuService;

@WebServlet("/ConfirmServlet")
public class ConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		MenuService service = new MenuService();
		MenuDTO menu = service.getMenuById(id);

		MenuOptionDAO optionDao = new MenuOptionDAO();
		List<MenuOptionDTO> options;
		try {
			options = optionDao.findByMenuId(id);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("menu", menu);
		request.setAttribute("options", options);

		request.getRequestDispatcher("/WEB-INF/jsp/Confirm.jsp")
				.forward(request, response);
	}
}
