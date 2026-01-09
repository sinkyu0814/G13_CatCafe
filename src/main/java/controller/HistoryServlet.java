package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*━━━━━━━━━━━━━━━━━━━━━
		 * 1. セッションから orderId を取得
		 *━━━━━━━━━━━━━━━━━━━━━*/
		HttpSession session = request.getSession();
		Integer orderId = (Integer) session.getAttribute("orderId");

		if (orderId == null) {
			throw new ServletException("注文が開始されていません（orderId がありません）");
		}

		List<OrderItem> historyList = new ArrayList<>();
		int totalAmount = 0;
		Integer tableNo = null;
		

		/*━━━━━━━━━━━━━━━━━━━━━
		 * 2. DBから注文履歴を取得
		 *━━━━━━━━━━━━━━━━━━━━━*/
		try (Connection conn = DBManager.getConnection()) {

			// order_date を SQL に合わせ、OrderItem クラスのフィールドに合わせる
			String sql = """
					SELECT
					    o.order_id, o.order_date, o.status, o.table_no,
					    oi.order_item_id, oi.menu_id, oi.goods_name, oi.price, oi.quantity,
					    m.image,
					    oio.option_name, oio.option_price
					FROM orders o
					JOIN order_items oi ON o.order_id = oi.order_id
					LEFT JOIN menus m ON oi.menu_id = m.menu_id
					LEFT JOIN order_item_options oio ON oi.order_item_id = oio.order_item_id
					WHERE o.order_id = ?
					ORDER BY oi.order_item_id ASC
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, orderId);

				try (ResultSet rs = ps.executeQuery()) {
					// 1つの商品に複数オプションがあるため、Mapで重複排除
					Map<Integer, OrderItem> itemMap = new LinkedHashMap<>();

					while (rs.next()) {
						int itemId = rs.getInt("order_item_id");
						OrderItem item = itemMap.get(itemId);

						if (item == null) {
							item = new OrderItem();
							item.setOrderId(rs.getInt("order_id"));
							item.setOrderTime(rs.getTimestamp("order_date")); // order_dateに修正
							item.setStatus(rs.getString("status"));
							item.setTableNo(rs.getInt("table_no"));
							item.setOrderItemId(itemId);
							item.setMenuId(rs.getString("menu_id"));
							item.setName(rs.getString("goods_name"));
							item.setPrice(rs.getInt("price"));
							item.setQuantity(rs.getInt("quantity"));
							item.setImage(rs.getString("image"));
							item.setSelectedOptions(new ArrayList<>()); // OrderItemにList<MenuOptionDTO> optionsが必要

							itemMap.put(itemId, item);

							if (tableNo == null) {
								tableNo = rs.getInt("table_no");
							}
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
					historyList = new ArrayList<>(itemMap.values());

					// 合計金額の計算（商品価格＋オプション価格の合計 × 数量）
					for (OrderItem oi : historyList) {
						int itemPriceSum = oi.getPrice();
						for (MenuOptionDTO o : oi.getSelectedOptions()) {
							itemPriceSum += o.getOptionPrice();
						}
						totalAmount += itemPriceSum * oi.getQuantity();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("DB Access Error", e);
		}

		/*━━━━━━━━━━━━━━━━━━━━━
		 * 3. 栄養データ（ダミー）
		 *━━━━━━━━━━━━━━━━━━━━━*/
		Map<String, String> nutritionData = new HashMap<>();
		nutritionData.put("calories", "1,200kcal");
		nutritionData.put("protein", "45g");

		/*━━━━━━━━━━━━━━━━━━━━━
		 * 4. JSPへデータを渡す
		 *━━━━━━━━━━━━━━━━━━━━━*/
		request.setAttribute("orderItems", historyList); // JSP側の c:forEach items="${orderItems}" に合わせる
		request.setAttribute("totalAmount", totalAmount);
		request.setAttribute("nutritionData", nutritionData);
		request.setAttribute("tableNo", tableNo);
		request.setAttribute("orderId", orderId);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/history.jsp");
		dispatcher.forward(request, response);
	}
}