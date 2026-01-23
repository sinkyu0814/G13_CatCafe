package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.MenuDTO;
import model.service.MenuService;

@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ListServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("persons") == null) {
			response.sendRedirect("ToppageServlet");
			return;
		}

		Integer persons = (Integer) session.getAttribute("persons");
		request.setAttribute("persons", persons);

		String category = request.getParameter("category");
		if (category == null)
			category = "Recomend";

		model.service.MenuService service = new MenuService();

		// カテゴリ一覧・メニュー一覧の取得
		List<String> categories = service.getCategoryList();
		request.setAttribute("categoryList", categories);

		List<MenuDTO> menuList = service.getMenuList(category);
		request.setAttribute("menuList", menuList);

		// ★ConfirmOrderServletからのエラー（request属性）はそのまま JSP へ引き継がれる
		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/list.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// POST（ConfirmOrderServletからのフォワード）時もdoGetと同じ処理を行う
		doGet(request, response);
	}
}