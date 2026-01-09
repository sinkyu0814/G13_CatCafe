package controller;

import java.io.IOException;

import database.KitchenOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/KitchenOrderServlet")
public class KitchenOrderServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		KitchenOrderDAO dao = new KitchenOrderDAO();
		try {
			request.setAttribute("orders", dao.findActiveOrders());
			request.getRequestDispatcher("/WEB-INF/jsp/orderList.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String itemIdStr = request.getParameter("orderItemId");
		String newStatus = request.getParameter("status");

		if (itemIdStr != null && newStatus != null) {
			KitchenOrderDAO dao = new KitchenOrderDAO();
			try {
				dao.updateStatus(Integer.parseInt(itemIdStr), newStatus);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		response.sendRedirect("KitchenOrderServlet");
	}
}