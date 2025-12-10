package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
/**
 * Servlet implementation class CheckoutServlet
 */
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckoutServlet() {
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
		// セッションを取得 (存在しない場合は null が返る)
		HttpSession session = request.getSession(false);

		String tableNo = null;

		// 1. セッションが存在する場合に処理を行う
		if (session != null) {

			// テーブル番号を取得
			Object tableNoObj = session.getAttribute("tableNo");
			if (tableNoObj != null) {
				tableNo = tableNoObj.toString();
			}

			// ★ 2. 注文プロセスが完了したため、カート情報をセッションから削除する ★
			//     次の注文時に前の注文が残らないようにするため
			session.removeAttribute("cart");
		}

		// 3. 取得したテーブル番号をリクエストスコープに保存する
		request.setAttribute("tableNo", tableNo);

		// 4. 画面表示を行うJSPへフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
