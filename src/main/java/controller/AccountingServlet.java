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

/**
 * Servlet implementation class AccountingServlet
 */
public class AccountingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tableNumber = request.getParameter("tableNumber");
		if (tableNumber == null || tableNumber.trim().isEmpty()) {
			// テーブル未選択ならテーブル選択画面へリダイレクト（存在するなら）
			response.sendRedirect("TableSelectServlet");
			return;
		}

		try (Connection conn = DBManager.getConnection()) {
			// 最新の NEW 注文を取得
			Integer orderId = findLatestNewOrderId(conn, tableNumber);
			if (orderId == null) {
				request.setAttribute("message", "指定テーブルに未処理の注文はありません。");
				request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
				return;
			}

			// 明細を取得して合計を計算
			List<OrderItemDTO> items = findOrderItems(conn, orderId);
			int totalAmount = items.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();

			request.setAttribute("selectedTable", tableNumber);
			request.setAttribute("orderList", items);
			request.setAttribute("totalAmount", totalAmount);
			request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String tableNumber = request.getParameter("tableNumber");

		if ("finishAccounting".equals(action)) {
			// 必須パラメータの取得（JSPで hidden などから送られている想定）
			String totalAmountStr = request.getParameter("totalAmount");
			String depositStr = request.getParameter("deposit");

			if (tableNumber == null || totalAmountStr == null || depositStr == null) {
				request.setAttribute("error", "必要な情報が不足しています。");
				request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
				return;
			}

			int totalAmount;
			int deposit;
			try {
				totalAmount = Integer.parseInt(totalAmountStr);
				deposit = Integer.parseInt(depositStr);
			} catch (NumberFormatException nfe) {
				request.setAttribute("error", "金額は数値で入力してください。");
				request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
				return;
			}

			int change = deposit - totalAmount;

			try (Connection conn = DBManager.getConnection()) {
				conn.setAutoCommit(false);
				try {
					Integer orderId = findLatestNewOrderId(conn, tableNumber);
					if (orderId == null) {
						conn.rollback();
						request.setAttribute("error", "該当する未処理の注文が見つかりません。");
						request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);
						return;
					}

					// orders に total_amount と status をセット（PAID）
					updateOrderAsPaid(conn, orderId, totalAmount);

					// 必要ならここで会計履歴テーブルへのINSERTなども行う

					conn.commit();

					// レスポンス用に値をセットして完了画面へ
					request.setAttribute("totalAmount", totalAmount);
					request.setAttribute("deposit", deposit);
					request.setAttribute("change", change);
					request.setAttribute("tableNumber", tableNumber);
					request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp").forward(request, response);

				} catch (SQLException ex) {
					conn.rollback();
					throw ex;
				} finally {
					conn.setAutoCommit(true);
				}

			} catch (Exception e) {
				throw new ServletException(e);
			}

		} else {
			// action 未指定は GET 相当の処理へリダイレクト
			doGet(request, response);
		}
	}

	// -------------------------
	// DB ヘルパーメソッド群
	// -------------------------

	/**
	 * 指定テーブルの最新の status = 'NEW' な order_id を返す。なければ null。
	 * ※ orders.table_no が NUMBER 型なら setInt、VARCHAR 型なら setString に変更してください
	 */
	private Integer findLatestNewOrderId(Connection conn, String tableNo) throws SQLException {
		// Oracle 向けにサブクエリ＋ROWNUM で最新1行を抜く（互換性が高い）
		String sql = "SELECT order_id FROM ("
				+ "  SELECT order_id FROM orders WHERE table_no = ? AND status = 'NEW' ORDER BY order_time DESC"
				+ ") WHERE ROWNUM = 1";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			// table_no が数値カラムなら parse して setInt。文字列なら setString。
			// 今回は数値型（NUMBER）を使っている想定：
			try {
				ps.setInt(1, Integer.parseInt(tableNo));
			} catch (NumberFormatException e) {
				// 数値に変換できないなら文字列としてセット（柔軟に対応）
				ps.setString(1, tableNo);
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt("order_id");
			}
		}
		return null;
	}

	/**
	 * 指定 order_id の order_items を取得する（DTO を返す）
	 * 注意：order_items のカラム名に合わせて SQL を調整してください
	 */
	private List<OrderItemDTO> findOrderItems(Connection conn, int orderId) throws SQLException {
		List<OrderItemDTO> list = new ArrayList<>();

		// ここでは order_items に (order_item_id, order_id, menu_id, goods_name, price, quantity) がある想定
		String sql = "SELECT order_item_id, menu_id, goods_name, price, quantity "
				+ "FROM order_items WHERE order_id = ? ORDER BY order_item_id";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, orderId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					OrderItemDTO it = new OrderItemDTO();
					it.setOrderItemId(rs.getInt("order_item_id"));
					it.setMenuId(rs.getString("menu_id"));
					it.setGoodsName(rs.getString("goods_name"));
					it.setPrice(rs.getInt("price"));
					it.setQuantity(rs.getInt("quantity"));
					list.add(it);
				}
			}
		}

		return list;
	}

	/**
	 * orders テーブルの指定 order_id を合計金額で更新し、status を 'PAID' にする
	 */
	private void updateOrderAsPaid(Connection conn, int orderId, int totalAmount) throws SQLException {
		String sql = "UPDATE orders SET total_amount = ?, status = 'PAID' WHERE order_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, totalAmount);
			ps.setInt(2, orderId);
			int updated = ps.executeUpdate();
			if (updated != 1) {
				throw new SQLException("orders の更新に失敗しました (order_id=" + orderId + ")");
			}
		}
	}
}