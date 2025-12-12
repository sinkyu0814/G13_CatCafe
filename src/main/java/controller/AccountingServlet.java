package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import viewmodel.OrderItem;

public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AccountingServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// パラメータ取得
		String tableNumber = request.getParameter("tableNumber");

		// nullならリダイレクト等の処理（ここでは省略）
		if (tableNumber == null)
			tableNumber = "1"; // 仮

		try (Connection conn = DBManager.getConnection()) {
			// 最新の NEW 注文IDを取得
			Integer orderId = findLatestNewOrderId(conn, tableNumber);

			if (orderId == null) {
				request.setAttribute("message", "未会計の注文はありません。");
				request.setAttribute("orderList", new ArrayList<OrderItem>());
				request.setAttribute("totalAmount", 0);
				request.setAttribute("selectedTable", tableNumber);
				request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
				return;
			}

			// 明細を取得
			List<OrderItem> items = findOrderItems(conn, orderId);
			int totalAmount = items.stream().mapToInt(OrderItem::getSubtotal).sum();

			request.setAttribute("selectedTable", tableNumber);
			request.setAttribute("orderList", items);
			request.setAttribute("totalAmount", totalAmount);
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String tableNumber = request.getParameter("tableNumber");

		if ("finishAccounting".equals(action)) {
			String totalAmountStr = request.getParameter("totalAmount");
			String depositStr = request.getParameter("deposit");

			int totalAmount = Integer.parseInt(totalAmountStr);
			int deposit = Integer.parseInt(depositStr);
			int change = deposit - totalAmount;

			try (Connection conn = DBManager.getConnection()) {
				conn.setAutoCommit(false);
				try {
					Integer orderId = findLatestNewOrderId(conn, tableNumber);
					if (orderId != null) {
						updateOrderAsPaid(conn, orderId, totalAmount);
					}
					conn.commit();

					// 完了画面へ
					request.setAttribute("totalAmount", totalAmount);
					request.setAttribute("deposit", deposit);
					request.setAttribute("change", change);
					request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp").forward(request, response);

				} catch (SQLException ex) {
					conn.rollback();
					throw ex;
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
			doGet(request, response);
		}
	}

	// --- DB Helper Methods ---

	private Integer findLatestNewOrderId(Connection conn, String tableNo) throws SQLException {
		// 文字列でも数値でも動くように整形
		int tableNumInt;
		try {
			tableNumInt = Integer.parseInt(tableNo);
		} catch (Exception e) {
			return null;
		}

		String sql = "SELECT order_id FROM ("
				+ " SELECT order_id FROM orders WHERE table_no = ? AND status = 'NEW' ORDER BY order_time DESC"
				+ ") WHERE ROWNUM = 1";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, tableNumInt);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt("order_id");
			}
		}
		return null;
	}

	private List<OrderItem> findOrderItems(Connection conn, int orderId) throws SQLException {
		List<OrderItem> list = new ArrayList<>();
		String sql = "SELECT order_item_id, goods_name, price, quantity FROM order_items WHERE order_id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					OrderItem it = new OrderItem();
					it.setOrderItemId(rs.getInt("order_item_id"));
					it.setName(rs.getString("goods_name")); // setNameを使用
					it.setPrice(rs.getInt("price"));
					it.setQuantity(rs.getInt("quantity"));
					list.add(it);
				}
			}
		}
		return list;
	}

	private void updateOrderAsPaid(Connection conn, int orderId, int totalAmount) throws SQLException {
		String sql = "UPDATE orders SET total_amount = ?, status = 'PAID' WHERE order_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, totalAmount);
			ps.setInt(2, orderId);
			ps.executeUpdate();
		}
	}
}