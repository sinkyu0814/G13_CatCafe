package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import database.AccountingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.service.AccountingService;
import viewmodel.OrderItem;

@WebServlet("/AccountingServlet")
public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// DAOとServiceをインスタンス化
	private final AccountingDAO accountingDAO = new AccountingDAO();
	private final AccountingService accountingService = new AccountingService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		Integer orderId = (Integer) session.getAttribute("orderId");
		if (orderId == null) {
			request.setAttribute("error", "注文情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
			return;
		}

		try {
			// 1. DAOにデータの取得を依頼
			Map<String, Object> details = accountingDAO.getOrderDetails(orderId);
			List<OrderItem> list = (List<OrderItem>) details.get("list");
			int tableNo = (int) details.get("tableNo");

			// 2. Serviceに計算を依頼
			int totalAmount = accountingService.calculateTotal(list);

			// 3. 結果をリクエストスコープにセット
			request.setAttribute("orderList", list);
			request.setAttribute("totalAmount", totalAmount);
			request.setAttribute("tableNo", tableNo);
			request.setAttribute("orderId", orderId);

			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		HttpSession session = request.getSession(false);

		// ログインチェック
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		try {
			// ケース1: 会計開始（テーブル選択からの遷移）
			if ("startAccounting".equals(action)) {
				handleStartAccounting(request, response, session);
				return;
			}

			// ケース2: 会計確定（支払い完了）
			handleCompleteAccounting(request, response, session);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * 会計開始処理
	 */
	private void handleStartAccounting(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		String tableStr = request.getParameter("tableNumber");
		if (tableStr == null || tableStr.isEmpty()) {
			response.sendRedirect("TableSelectServlet");
			return;
		}

		int tableNo = Integer.parseInt(tableStr.replace("番", ""));

		// DAOで注文IDを検索
		int orderId = accountingDAO.findActiveOrderIdByTable(tableNo);

		if (orderId != -1) {
			session.setAttribute("orderId", orderId);
			session.setAttribute("tableNo", tableNo);
			response.sendRedirect("AccountingServlet");
		} else {
			session.setAttribute("tableSelectError", tableNo + "番テーブルは現在空席です。");
			response.sendRedirect("TableSelectServlet");
		}
	}

	/**
	 * 会計確定処理
	 */
	private void handleCompleteAccounting(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {

		Integer orderId = (Integer) session.getAttribute("orderId");
		Integer tableNo = (Integer) session.getAttribute("tableNo");

		int totalAmount = Integer.parseInt(request.getParameter("totalAmount"));
		int deposit = Integer.parseInt(request.getParameter("deposit"));

		// Serviceでお釣りを計算
		int change = accountingService.calculateChange(deposit, totalAmount);

		// DAOでステータスを更新
		accountingDAO.updateStatusToPaid(orderId);

		// 結果をセットして完了画面へ
		request.setAttribute("tableNo", tableNo);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("deposit", deposit);
		request.setAttribute("change", change);

		session.removeAttribute("orderId");
		session.removeAttribute("tableNo");

		request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp").forward(request, response);
	}
}