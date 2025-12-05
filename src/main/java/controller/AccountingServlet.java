package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
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
			// --- 会計終了処理 ---

			// 預り金と合計金額をリクエストパラメータから取得
			// JSP側で hidden field として送信されていることを前提とします
			int totalAmount = Integer.parseInt(request.getParameter("totalAmount"));
			int deposit = Integer.parseInt(request.getParameter("deposit"));
			int change = deposit - totalAmount; // おつりの計算

			// 実際にはDB更新を行い、テーブルを空席に戻す処理などが入る
			System.out.println(tableNumber + "テーブルの会計を完了しました。");

			// 【追加】会計完了画面で表示するために値をリクエストスコープにセット
			request.setAttribute("totalAmount", totalAmount);
			request.setAttribute("deposit", deposit);
			request.setAttribute("change", change);

			// 会計完了画面へフォワード
			request.getRequestDispatcher("/WEB-INF/jsp/AccountingComplete.jsp").forward(request, response);

		} else {
			// --- 会計画面表示処理 (actionがstartAccountingまたはnullの場合) ---
			if (tableNumber != null && !tableNumber.isEmpty()) {
				request.setAttribute("selectedTable", tableNumber);

				// **【仮の注文データ】** (実際はDBから取得)
				List<OrderItem> orderList = getDummyOrderItems();
				int totalAmount = calculateTotal(orderList);

				request.setAttribute("orderList", orderList);
				request.setAttribute("totalAmount", totalAmount);

				// 【パス修正】: WEB-INF/jsp/ を追加
				request.getRequestDispatcher("/WEB-INF/jsp/Accounting.jsp").forward(request, response);

			} else {
				// テーブル選択なしの場合は、TableSelectServletへリダイレクト
				response.sendRedirect("TableSelectServlet");
			}
		}
	}

	// 仮の注文データ生成メソッド
	private List<OrderItem> getDummyOrderItems() {
		List<OrderItem> list = new ArrayList<>();
		list.add(new OrderItem("生ビール", 500));
		list.add(new OrderItem("枝豆", 350));
		list.add(new OrderItem("からあげ", 680));
		list.add(new OrderItem("ウーロン茶", 250));
		return list;
	}

	// 合計金額計算メソッド
	private int calculateTotal(List<OrderItem> list) {
		int total = 0;
		for (OrderItem item : list) {
			total += item.getPrice();
		}
		return total;
	}
}
