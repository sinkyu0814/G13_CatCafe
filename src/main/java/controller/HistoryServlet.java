package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuDTO;

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

		// --- 1. データ取得（セッションからカートを取得） ---
		// カート情報（List<MenuDTO>）がセッションの "cart" 属性に保存されていると仮定
		@SuppressWarnings("unchecked")
		List<MenuDTO> orderedItems = (List<MenuDTO>) session.getAttribute("cart");

		// カートが空の場合は、空のリストを作成
		if (orderedItems == null) {
			// Serviceはnullを受け取れないため、空リストで初期化
			orderedItems = java.util.Collections.emptyList();
		}

		// --- 2. データ処理（Serviceによる計算） ---
		int totalAmount = menuService.calculateTotal(orderedItems);

		// ※ 栄養素合計はここでは単純なダミーMapで代用
		java.util.Map<String, String> nutritionData = new java.util.HashMap<>();
		nutritionData.put("calories", "1,200kcal");
		nutritionData.put("protein", "45g");

		// --- 3. データをリクエストスコープに格納 ---
		request.setAttribute("orderItems", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("nutritionData", nutritionData); // 栄養素データ

		// --- 4. JSPファイルに処理を転送 ---
		// 転送先のJSPファイルパスを指定 (例: /WEB-INF/jsp/history.jsp)
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
