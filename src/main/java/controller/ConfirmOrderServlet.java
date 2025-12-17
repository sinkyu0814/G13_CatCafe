package controller;

import java.io.IOException;
import java.util.List;

import database.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.CartItem;

public class ConfirmOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		// -------------------------
		// カート取得
		// -------------------------
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
		if (cart == null || cart.isEmpty()) {
			request.setAttribute("error", "カートが空です");
			request.getRequestDispatcher("/WEB-INF/jsp/list.jsp")
					.forward(request, response);
			return;
		}

		// -------------------------
		// orderId 取得（注文開始時に作成済み）
		// -------------------------
		Integer orderId = (Integer) session.getAttribute("orderId");
		if (orderId == null) {
			throw new ServletException("orderId がセッションに存在しません");
		}

		// -------------------------
		// 明細を DB に登録
		// -------------------------
		CartDAO dao = new CartDAO();
		try {
			dao.insertOrderItems(orderId, cart);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// -------------------------
		// 後処理
		// -------------------------
		session.removeAttribute("cart");

		request.setAttribute("orderId", orderId);
		request.setAttribute("orderItems", cart);

		request.getRequestDispatcher("/WEB-INF/jsp/orderComplete.jsp")
				.forward(request, response);
	}
}
