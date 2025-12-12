package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuDTO;
import model.service.MenuService;

//@WebServlet("/TableSelectServlet")
public class TableSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TableSelectServlet() {
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
		HttpSession session = request.getSession();
		Integer persons = (Integer) session.getAttribute("persons");
		request.setAttribute("persons", persons);

		// category パラメータ取得
		String category = request.getParameter("category");
		if (category == null)
			category = "all";

		MenuService service = new MenuService();

		// カテゴリ一覧
		List<String> catList = service.getCategoryList();
		request.setAttribute("categoryList", catList);

		// メニュー一覧
		List<MenuDTO> menuList = service.getMenuList(category);
		request.setAttribute("menuList", menuList);

		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/TableSelect.jsp");
		rd.forward(request, response);
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