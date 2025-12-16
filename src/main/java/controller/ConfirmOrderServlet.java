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
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart == null || cart.isEmpty()) {
			request.setAttribute("error", "カートが空です");
			request.getRequestDispatcher("/WEB-INF/jsp/list.jsp")
					.forward(request, response);
			return;
		}

		// 🔑 最初に入力したテーブル番号（ToppageServlet で保存済み）
		Integer tableNo = (Integer) session.getAttribute("tableNo");
		if (tableNo == null) {
			throw new ServletException("テーブル番号がセッションに存在しません");
		}

		CartDAO dao = new CartDAO();
		long orderId;

		try {
			// ⭐ CartDAOの完成メソッドを使う
			orderId = dao.insertOrder(cart, String.valueOf(tableNo));
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// カートをクリア
		session.removeAttribute("cart");

		// 完了画面用
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderItems", cart);

		request.getRequestDispatcher("/WEB-INF/jsp/orderComplete.jsp")
				.forward(request, response);
	}
}
