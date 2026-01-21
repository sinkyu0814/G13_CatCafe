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

@WebServlet("/AccountingServlet")
public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			request.setAttribute("error", "注文情報が取得できません");
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
			return;
		}

		List<OrderItem> list = new ArrayList<>();
		int totalAmount = 0;
		int tableNo = 0;

		try (Connection conn = DBManager.getConnection()) {
			String sql = """
					SELECT o.table_no,
					       oi.order_item_id,
					       oi.goods_name,
					       oi.price,
					       oi.quantity,
					       oio.option_name,
					       oio.option_price
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
						tableNo = rs.getInt("table_no");

						OrderItem item = itemMap.get(itemId);
						if (item == null) {
							item = new OrderItem();
							item.setOrderItemId(itemId);
							item.setName(rs.getString("goods_name"));
							item.setPrice(rs.getInt("price"));
							item.setQuantity(rs.getInt("quantity"));
							item.setSelectedOptions(new ArrayList<>());
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
					list = new ArrayList<>(itemMap.values());
					for (OrderItem oi : list) {
						totalAmount += (oi.getPrice() + oi.getOptionTotalPrice()) * oi.getQuantity();
					}
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("orderList", list);
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo);
		request.setAttribute("orderId", orderId);
		request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if ("startAccounting".equals(action)) {
			String tableStr = request.getParameter("tableNumber");
			if (tableStr == null)
				throw new ServletException("テーブル番号なし");
			int tableNo = Integer.parseInt(tableStr.replace("番", ""));

			try (Connection conn = DBManager.getConnection()) {
				String sql = "SELECT order_id FROM orders WHERE table_no = ? AND status = 'NEW'";
				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setInt(1, tableNo);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							session.setAttribute("orderId", rs.getInt("order_id"));
							session.setAttribute("tableNo", tableNo);
						} else {
							throw new ServletException("未会計の注文なし");
						}
					}
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
			response.sendRedirect("AccountingServlet");
			return;
		}

		Integer orderId = (Integer) session.getAttribute("orderId");
		if (orderId == null)
			throw new ServletException("会計対象なし");

		// 会計確定処理
		try (Connection conn = DBManager.getConnection()) {
			String sql = "UPDATE orders SET status = 'PAID' WHERE order_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		// ★ 会計確定に伴い、お客様の注文情報を消去（これでCheckout.jspの自動遷移が発動する）
		session.removeAttribute("orderId");
		session.removeAttribute("isPaid");
		session.removeAttribute("persons");
		session.removeAttribute("tableNo");

		// 店員側はテーブル一覧へ戻る
		response.sendRedirect("TableSelectServlet");
	}
}