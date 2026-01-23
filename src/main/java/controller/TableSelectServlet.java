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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		if (session.getAttribute("isLoggedIn") == null) {
			String uri = request.getRequestURI();
			String query = request.getQueryString();
			String target = (query == null) ? uri : uri + "?" + query;
			session.setAttribute("targetURI", target);
			response.sendRedirect("LoginServlet");
			return;
		}

		// ★ 修正点：AccountingServletから送られてきたエラーメッセージを取得
		String error = (String) session.getAttribute("tableSelectError");
		if (error != null) {
			request.setAttribute("error", error);
			session.removeAttribute("tableSelectError");
		}

		Map<Integer, Integer> tablePersonsMap = new HashMap<>();
		try (Connection conn = DBManager.getConnection()) {
			String sql = "SELECT table_no, persons FROM orders WHERE status = 'NEW'";
			try (PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tablePersonsMap.put(rs.getInt("table_no"), rs.getInt("persons"));
				}
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("tablePersonsMap", tablePersonsMap);

		String category = request.getParameter("category");
		if (category == null)
			category = "all";

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
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		doGet(request, response);
	}
}