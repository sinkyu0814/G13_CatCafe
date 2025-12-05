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

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConfirmOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		doGet(request, response);
		HttpSession session = request.getSession();
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart == null || cart.isEmpty()) {
			request.setAttribute("error", "カートが空です");
			request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
			return;
		}

		// テーブル番号（固定 or 入力に応じて変更OK）
		String tableNo = "01";

		database.CartDAO dao = new CartDAO();
		long orderId;

		try {
			orderId = dao.insertOrder(cart, tableNo);
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// カートをクリア
		session.removeAttribute("cart");

		// 完了画面に表示するために orderId を保存
		request.setAttribute("orderId", orderId);
		request.setAttribute("orderItems", cart);

		request.getRequestDispatcher("/WEB-INF/jsp/orderComplete.jsp").forward(request, response);
	}
}
