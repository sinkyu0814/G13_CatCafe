package controller;

import java.io.IOException;

import database.KitchenOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/KitchenOrderServlet")
public class KitchenOrderServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true); // 記録のためにsessionを作成
		if (session.getAttribute("isLoggedIn") == null) {
			// ★ 今のリクエストURLを保存（例: "AdminServlet"）
			String uri = request.getRequestURI();
			String query = request.getQueryString();
			String target = (query == null) ? uri : uri + "?" + query;

			session.setAttribute("targetURI", target);

			response.sendRedirect("LoginServlet");
			return;
		}

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

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
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
