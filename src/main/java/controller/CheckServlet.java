package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import database.DBManager;
import database.KitchenOrderDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuOptionDTO;
import viewmodel.OrderItem;

@WebServlet("/CheckServlet")
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", "注文情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/check.jsp").forward(request, response);
			return;
		}

		List<OrderItem> orderedItems = new ArrayList<>();
		int totalAmount = 0;
		int tableNo = 0;

		try (Connection conn = DBManager.getConnection()) {
			// ★ 修正：m.image は BLOB なので取得対象から外しました
			String sql = """
					SELECT
					    o.order_id, o.table_no,
					    oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity,
					    oio.option_name, oio.option_price
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					LEFT JOIN order_item_options oio ON oi.order_item_id = oio.order_item_id
					WHERE o.order_id = ? AND o.status = 'NEW'
					ORDER BY oi.order_item_id ASC
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);
				try (ResultSet rs = ps.executeQuery()) {
					Map<Integer, OrderItem> itemMap = new LinkedHashMap<>();
					while (rs.next()) {
						int itemId = rs.getInt("order_item_id");
						OrderItem item = itemMap.get(itemId);
						if (item == null) {
							item = new OrderItem();
							item.setOrderId(rs.getInt("order_id"));
							item.setOrderItemId(itemId);

							// ★ 重要：GetImageServlet?id=${item.menuId} で使うため
							item.setMenuId(rs.getString("menu_id"));

							item.setName(rs.getString("goods_name"));
							item.setPrice(rs.getInt("price"));
							item.setQuantity(rs.getInt("quantity"));

							// ★ 修正：rs.getString("image") によるエラーを回避するため削除
							item.setImage(null);

							item.setSelectedOptions(new ArrayList<>());
							tableNo = rs.getInt("table_no");
							itemMap.put(itemId, item);
						}
						String optName = rs.getString("option_name");
						if (optName != null) {
							MenuOptionDTO opt = new MenuOptionDTO();
							opt.setOptionName(optName);
							opt.setOptionPrice(rs.getInt("option_price"));
							item.getSelectedOptions().add(opt);
						}
					}
					orderedItems = new ArrayList<>(itemMap.values());
					for (OrderItem oi : orderedItems) {
						totalAmount += (oi.getPrice() + oi.getOptionTotalPrice()) * oi.getQuantity();
					}
				}
			}

			// 未提供の商品があるかチェック
			KitchenOrderDAO kitchenDao = new KitchenOrderDAO();
			boolean hasUnfinished = kitchenDao.hasUnfinishedItems((long) orderId);
			request.setAttribute("hasUnfinished", hasUnfinished);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("DB Access Error", e);
		}

		request.setAttribute("items", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo);
		request.setAttribute("orderId", orderId);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/check.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}