package controller;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import viewmodel.CartItem;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. セッションの取得（ログインチェックではなく、注文セッションがあるかを確認）
		HttpSession session = request.getSession(false);

		// セッションが切れている、または人数入力（注文開始）がされていない場合はトップへ
		if (session == null || session.getAttribute("persons") == null) {
			response.sendRedirect("ToppageServlet");
			return;
		}

		// 2. カート情報の取得
		@SuppressWarnings("unchecked")
		ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");

		// 3. 数量変更ロジック
		try {
			String indexStr = request.getParameter("index");
			String changeStr = request.getParameter("change");

			if (indexStr != null && changeStr != null) {
				int index = Integer.parseInt(indexStr);
				int change = Integer.parseInt(changeStr);

				if (cart != null && index >= 0 && index < cart.size()) {
					CartItem item = cart.get(index);
					int newQty = item.getQuantity() + change;

					if (newQty <= 0) {
						cart.remove(index);
					} else {
						item.setQuantity(newQty);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// エラーが起きても止まらずに一覧へ戻す
		}

		// 4. カテゴリを維持してリダイレクト
		String category = request.getParameter("category");
		if (category == null || category.isEmpty()) {
			category = "Recomend";
		}

		response.sendRedirect("ListServlet?category=" + category);
	}

	// GETでアクセスされた場合も同様の処理、またはエラー回避
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}