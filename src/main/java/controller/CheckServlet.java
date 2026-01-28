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

	// 表示用データを取得する共通メソッド
	private void prepareOrderData(HttpServletRequest request, int orderId) throws Exception {
		List<OrderItem> orderedItems = new ArrayList<>();
		int totalAmount = 0;
		int tableNo = 0;

		try (Connection conn = DBManager.getConnection()) {
			String sql = """
					SELECT o.table_no, o.status,
					       oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity,
					       oio.option_name, oio.option_price
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					LEFT JOIN order_item_options oio ON oi.order_item_id = oio.order_item_id
					WHERE o.order_id = ?
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
							item.setOrderItemId(itemId);
							item.setMenuId(rs.getString("menu_id"));
							item.setName(rs.getString("goods_name"));
							item.setPrice(rs.getInt("price"));
							item.setQuantity(rs.getInt("quantity"));
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
		}
		request.setAttribute("items", orderedItems);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			response.sendRedirect("MenuServlet"); // 注文がなければメニューへ
			return;
		}

		try {
			prepareOrderData(request, orderId);
			// ★ 通常の確認画面を表示
			request.getRequestDispatcher("/WEB-INF/jsp/check.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId != null) {
			try (Connection conn = DBManager.getConnection()) {
				// ★ ステータスを「会計依頼中」に更新
				String sql = "UPDATE orders SET status = 'CHECKOUT_REQUEST' WHERE order_id = ?";
				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setInt(1, orderId);
					ps.executeUpdate();
				}

				// 完了画面に必要なデータを再セット
				prepareOrderData(request, orderId);

				// ★ ここで遷移先を「checkout.jsp」にする！
				request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}
	}
}