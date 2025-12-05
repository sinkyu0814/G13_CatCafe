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

		// --- 1. データ取得（セッションからカートを取得） ---
		// カート情報（List<MenuDTO>）がセッションの "cart" 属性に保存されていると仮定
		@SuppressWarnings("unchecked")
		List<MenuDTO> orderedItems = (List<MenuDTO>) session.getAttribute("cart");


		// --- 2. データ処理（Serviceによる合計金額の計算） ---
		int totalAmount = menuService.calculateTotal(orderedItems);

		// --- 3. データをリクエストスコープに格納 ---
		request.setAttribute("items", orderedItems); // JSPで使う変数名を 'items' に統一
		request.setAttribute("totalAmount", totalAmount);

		// --- 4. JSPファイルに処理を転送 ---
		// 転送先のJSPファイルパスを指定 (例: /WEB-INF/jsp/check.jsp)
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
