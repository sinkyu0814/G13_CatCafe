package controller;

import java.io.IOException;
import java.util.List;

import database.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.CartItem;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		// -------------------------
		// 1. カート取得
		// -------------------------
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		// ★例外処理：カートが空の場合、ListServletへ戻す
		if (cart == null || cart.isEmpty()) {
			request.setAttribute("error", "カートが空です。注文する商品を選んでください。");
			// ListServletのdoGetを通るようにフォワード（メニュー再取得のため）
			request.getRequestDispatcher("ListServlet").forward(request, response);
			return;
		}

		// -------------------------
		// 2. orderId 取得
		// -------------------------
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", "セッションの有効期限が切れました。最初からやり直してください。");
			request.getRequestDispatcher("/WEB-INF/jsp/FirstWindow.jsp").forward(request, response);
			return;
		}

		// -------------------------
		// 3. 明細を DB に登録
		// -------------------------
		CartDAO dao = new CartDAO();
		try {
			dao.insertOrderItems(orderId, cart);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("注文確定処理に失敗しました", e);
		}

		// -------------------------
		// 4. 後処理（カートを空にする）
		// -------------------------
		session.removeAttribute("cart");

		request.setAttribute("orderId", orderId);
		request.setAttribute("orderItems", cart);

		request.getRequestDispatcher("/WEB-INF/jsp/orderComplete.jsp").forward(request, response);
	}
}