package controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import database.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;
import viewmodel.CartItem;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();

		// -------------------------
		// 0. 言語設定とメッセージ準備
		// -------------------------
		Locale locale = (Locale) Config.get(session, Config.FMT_LOCALE);
		if (locale == null) {
			locale = Locale.JAPANESE;
		}
		ResourceBundle bundle = ResourceBundle.getBundle("properties.messages", locale);

		// -------------------------
		// 1. カート取得
		// -------------------------
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		// ★例外処理：カートが空の場合（多言語対応メッセージ）
		if (cart == null || cart.isEmpty()) {
			request.setAttribute("error", bundle.getString("error.cart_empty"));
			request.getRequestDispatcher("ListServlet").forward(request, response);
			return;
		}

		// -------------------------
		// 2. orderId 取得
		// -------------------------
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", bundle.getString("error.session_expired"));
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
			// 致命的なエラーメッセージ（必要に応じてここもbundle化可能です）
			throw new ServletException("注文確定処理に失敗しました / Order processing failed", e);
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