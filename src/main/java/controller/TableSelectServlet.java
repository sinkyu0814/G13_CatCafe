package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
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
import model.dto.MenuDTO;
import model.service.MenuService;

@WebServlet("/TableSelectServlet")
public class TableSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		if (session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		String error = (String) session.getAttribute("tableSelectError");
		if (error != null) {
			request.setAttribute("error", error);
			session.removeAttribute("tableSelectError");
		}

		// テーブル番号をキーに、ステータスを保存するMap
		Map<Integer, String> tableStatusMap = new HashMap<>();
		// テーブル番号をキーに、人数を保存するMap
		Map<Integer, Integer> tablePersonsMap = new HashMap<>();

		try (Connection conn = DBManager.getConnection()) {
			// ★ status も取得。NEW（食事中）と CHECKOUT_REQUEST（会計待ち）を対象にする
			String sql = "SELECT table_no, persons, status FROM orders WHERE status IN ('NEW', 'CHECKOUT_REQUEST')";
			try (PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int tNo = rs.getInt("table_no");
					tablePersonsMap.put(tNo, rs.getInt("persons"));
					tableStatusMap.put(tNo, rs.getString("status"));
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("tablePersonsMap", tablePersonsMap);
		request.setAttribute("tableStatusMap", tableStatusMap); // ★追加

		MenuService service = new MenuService();
		List<String> catList = service.getCategoryList();
		request.setAttribute("categoryList", catList);

		List<MenuDTO> menuList = service.getMenuList("all");
		request.setAttribute("menuList", menuList);

		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/TableSelect.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}