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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuOptionDTO;
import viewmodel.OrderItem;

/**
 * 会計確認画面（orderId 基準）
 * オプション料金を含めた詳細と合計金額を取得します。
 */
@WebServlet("/CheckServlet")
public class CheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// =========================
		// 1. orderId を取得
		// =========================
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

		// =========================
		// 2. DBから注文データ取得（オプション情報をJOIN）
		// =========================
		try (Connection conn = DBManager.getConnection()) {

			// オプションテーブル (order_item_options) も結合するSQLに修正
			String sql = """
					SELECT
					    o.order_id, o.table_no,
					    oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity,
					    m.image,
					    oio.option_name, oio.option_price
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					LEFT JOIN menus m ON oi.menu_id = m.menu_id
					LEFT JOIN order_item_options oio ON oi.order_item_id = oio.order_item_id
					WHERE o.order_id = ? AND o.status = 'NEW'
					ORDER BY oi.order_item_id ASC
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);

				try (ResultSet rs = ps.executeQuery()) {
					// 1つの商品に複数オプションがあるため、Mapで重複排除（履歴画面と同じロジック）
					Map<Integer, OrderItem> itemMap = new LinkedHashMap<>();

					while (rs.next()) {
						int itemId = rs.getInt("order_item_id");
						OrderItem item = itemMap.get(itemId);

						if (item == null) {
							item = new OrderItem();
							item.setOrderId(rs.getInt("order_id"));
							item.setOrderItemId(itemId);
							item.setMenuId(rs.getString("menu_id"));
							item.setName(rs.getString("goods_name"));
							item.setPrice(rs.getInt("price"));
							item.setQuantity(rs.getInt("quantity"));
							item.setImage(rs.getString("image"));
							item.setSelectedOptions(new ArrayList<>()); // リストを初期化

							tableNo = rs.getInt("table_no");
							itemMap.put(itemId, item);
						}

						// オプション情報があれば追加
						String optName = rs.getString("option_name");
						if (optName != null) {
							MenuOptionDTO opt = new MenuOptionDTO();
							opt.setOptionName(optName);
							opt.setOptionPrice(rs.getInt("option_price"));
							item.getSelectedOptions().add(opt);
						}
					}

					orderedItems = new ArrayList<>(itemMap.values());

					// 合計金額の計算（(商品単価 + 全オプション単価合計) × 数量）
					for (OrderItem oi : orderedItems) {
						int itemAndOptionsPrice = oi.getPrice() + oi.getOptionTotalPrice();
						totalAmount += itemAndOptionsPrice * oi.getQuantity();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("DB Access Error", e);
		}

		// =========================
		// 3. JSPへ渡す
		// =========================
		request.setAttribute("items", orderedItems); // JSP側の c:forEach var="item" items="${items}" に対応
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("tableNo", tableNo);
		request.setAttribute("orderId", orderId);

		// =========================
		// 4. 画面遷移
		// =========================
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/check.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}