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

	public TableSelectServlet() {
		super();
	}

	// =========================
	// 席選択画面表示
	// =========================
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer persons = (Integer) session.getAttribute("persons");
		request.setAttribute("persons", persons);

		// -------------------------
		// テーブル番号 × 人数取得
		// -------------------------
		Map<Integer, Integer> tablePersonsMap = new HashMap<>();

		try (Connection conn = DBManager.getConnection()) {

			String sql = """
						SELECT table_no, persons
						FROM orders
						WHERE status = 'NEW'
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int tableNo = rs.getInt("table_no");
					int p = rs.getInt("persons"); // NULL → 0
					tablePersonsMap.put(tableNo, p);
				}
			}

		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("tablePersonsMap", tablePersonsMap);

		// -------------------------
		// メニュー表示処理（既存）
		// -------------------------
		String category = request.getParameter("category");
		if (category == null) {
			category = "all";
		}

		MenuService service = new MenuService();

		List<String> catList = service.getCategoryList();
		request.setAttribute("categoryList", catList);

		List<MenuDTO> menuList = service.getMenuList(category);
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
