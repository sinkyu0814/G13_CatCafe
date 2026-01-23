package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.MenuOptionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuDTO;
import model.dto.MenuOptionDTO;
import model.service.MenuService;
import viewmodel.CartItem;

@WebServlet("/CartAddServlet")
public class CartAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CartAddServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// ★ カテゴリ情報を取得
		String category = request.getParameter("category");

		int id = Integer.parseInt(request.getParameter("id"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String[] optionIds = request.getParameterValues("optionIds");

		// 商品取得
		MenuService service = new MenuService();
		MenuDTO menu = service.getMenuById(id);

		List<MenuOptionDTO> selectedOptions = new ArrayList<>();
		if (optionIds != null && optionIds.length > 0) {
			MenuOptionDAO optionDao = new MenuOptionDAO();
			try {
				selectedOptions = optionDao.findByIds(optionIds);
			} catch (Exception e) {
				throw new ServletException("オプション取得エラー", e);
			}
		}

		// セッションからカートを取得
		HttpSession session = request.getSession();
		ArrayList<CartItem> cart = (ArrayList<CartItem>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
		}

		// 同じ商品があるなら数量を増やす
		boolean found = false;
		for (CartItem item : cart) {
			if (item.getGoodsId() == id && isSameOptions(item.getSelectedOptions(), selectedOptions)) {
				item.setQuantity(item.getQuantity() + quantity);
				found = true;
				break;
			}
		}

		// 一致するものがなければ新規追加
		if (!found) {
			CartItem newItem = new CartItem(menu.getId(), menu.getName(), menu.getPrice(), quantity);
			newItem.setSelectedOptions(selectedOptions);
			cart.add(newItem);
		}

		session.setAttribute("cart", cart);

		// ★ 修正：リダイレクトURLにカテゴリを付与する
		if (category != null && !category.isEmpty()) {
			response.sendRedirect("ListServlet?category=" + category);
		} else {
			response.sendRedirect("ListServlet");
		}
	}

	private boolean isSameOptions(List<MenuOptionDTO> list1, List<MenuOptionDTO> list2) {
		if (list1 == null && list2 == null)
			return true;
		if (list1 == null || list2 == null)
			return false;
		if (list1.size() != list2.size())
			return false;

		List<Integer> ids1 = list1.stream().map(MenuOptionDTO::getOptionId).sorted().toList();
		List<Integer> ids2 = list2.stream().map(MenuOptionDTO::getOptionId).sorted().toList();

		return ids1.equals(ids2);
	}
}