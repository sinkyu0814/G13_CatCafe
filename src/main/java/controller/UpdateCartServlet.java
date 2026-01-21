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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");

		try {
			int index = Integer.parseInt(request.getParameter("index"));
			int change = Integer.parseInt(request.getParameter("change"));

			if (cart != null && index >= 0 && index < cart.size()) {
				CartItem item = cart.get(index);
				int newQty = item.getQuantity() + change;

				if (newQty <= 0) {
					cart.remove(index);
				} else {
					item.setQuantity(newQty);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 元のカテゴリを維持してリダイレクト
		String category = request.getParameter("category");
		if (category == null || category.isEmpty())
			category = "Recomend";
		response.sendRedirect("ListServlet?category=" + category);
	}
}