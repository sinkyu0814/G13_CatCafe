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
 * Servlet implementation class HistoryServlet
 */
public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HistoryServlet() {
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

		if (orderedItems == null) {
			orderedItems = java.util.Collections.emptyList();
		}

		// --- 2. 合計金額計算 ---
		int totalAmount = 0;
		for (CartItem item : orderedItems) {
			totalAmount += item.getTotalPrice();
		}

		// --- 3. ダミー栄養データ ---
		java.util.Map<String, String> nutritionData = new java.util.HashMap<>();
		nutritionData.put("calories", "1,200kcal");
		nutritionData.put("protein", "45g");

		// --- 4. リクエストへセット ---
		request.setAttribute("orderItems", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("nutritionData", nutritionData);

		// --- 5. JSPへフォワード ---
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/history.jsp");
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
