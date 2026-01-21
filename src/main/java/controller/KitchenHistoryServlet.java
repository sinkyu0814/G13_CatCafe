package controller;

import java.io.IOException;

import database.KitchenOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/KitchenHistoryServlet")
public class KitchenHistoryServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		KitchenOrderDAO dao = new KitchenOrderDAO();
		try {
			request.setAttribute("history", dao.findDeliveredOrders());
			request.getRequestDispatcher("/WEB-INF/jsp/kitchenHistory.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		String action = request.getParameter("action");
		KitchenOrderDAO dao = new KitchenOrderDAO();

		try {
			if ("deleteAll".equals(action)) {
				dao.deleteAllDeliveredItems();
			} else {
				int itemId = Integer.parseInt(request.getParameter("orderItemId"));
				if ("back".equals(action)) {
					dao.updateStatus(itemId, "COOKED");
				} else if ("delete".equals(action)) {
					dao.deleteOrderItem(itemId);
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
		response.sendRedirect("KitchenHistoryServlet");
	}
}