package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.CartItem;

/**
 * Servlet implementation class CheckServlet
 */
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckServlet() {
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
		HttpSession session = request.getSession();

		// --- 1. カート取得 ---
		@SuppressWarnings("unchecked")
		List<CartItem> orderedItems = (List<CartItem>) session.getAttribute("cart");

		// --- 2. 合計金額計算 ---
		int totalAmount = 0;
		if (orderedItems != null) {
			for (CartItem item : orderedItems) {
				totalAmount += item.getTotalPrice();
			}
		}

		// --- 3. リクエストへセット ---
		request.setAttribute("items", orderedItems);
		request.setAttribute("totalAmount", totalAmount);

		// --- 4. JSPへフォワード ---
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/check.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
