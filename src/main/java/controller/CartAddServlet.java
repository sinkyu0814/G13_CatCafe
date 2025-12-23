package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.MenuOptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuDTO;
import model.dto.MenuOptionDTO;
import model.service.MenuService;
import viewmodel.CartItem;

//@WebServlet("/CartAddServlet")
/**
 * Servlet implementation class CartAddServlet
 */
public class CartAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CartAddServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		int id = Integer.parseInt(request.getParameter("id"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));

		// 商品取得
		model.service.MenuService service = new MenuService();
		MenuDTO menu = service.getMenuById(id);

		// セッションからカートを取得
		HttpSession session = request.getSession();
		ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
		}

		// 同じ商品があるなら数量を増やす
		boolean found = false;
		for (CartItem item : cart) {
			if (item.getGoodsId() == id) {
				item.setQuantity(item.getQuantity() + quantity);
				found = true;
				break;
			}
		}

		// 新規追加
		if (!found) {
			cart.add(new CartItem(
					menu.getId(),
					menu.getName(),
					menu.getPrice(),
					quantity));
		}

		String[] optionIds = request.getParameterValues("optionIds");

		MenuOptionDAO optionDao = new MenuOptionDAO();
		List<MenuOptionDTO> selectedOptions = null;
		try {
			selectedOptions = optionDao.findByIds(optionIds);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		CartItem item = new CartItem();
		item.setGoodsId(menu.getId());
		item.setGoodsName(menu.getName());
		item.setPrice(menu.getPrice());
		item.setQuantity(quantity);
		item.setSelectedOptions(selectedOptions);

		session.setAttribute("cart", cart);

		response.sendRedirect("ConfirmServlet");
	}
}
