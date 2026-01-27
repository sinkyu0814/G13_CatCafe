package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import database.MenuDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.dto.MenuDTO;
import model.service.MenuService;

@MultipartConfig
@WebServlet("/AddMenuServlet")
public class AddMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}

		MenuService service = new MenuService();
		List<MenuDTO> menuList = service.getMenuList("all");
		request.setAttribute("menuList", menuList);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp");
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
		request.setCharacterEncoding("UTF-8");

		try {
			String name = request.getParameter("name");
			String category = request.getParameter("category");
			String priceStr = request.getParameter("price");
			String quantityStr = request.getParameter("quantity");

			if (name == null || name.isEmpty() || priceStr == null || priceStr.isEmpty()
					|| quantityStr == null || quantityStr.isEmpty()) {
				request.setAttribute("error", "必須項目を入力してください");
				doGet(request, response);
				return;
			}

			int price = Integer.parseInt(priceStr);
			int quantity = Integer.parseInt(quantityStr);

			// --- 画像処理：DB保存版 ---
			Part filePart = request.getPart("image");
			InputStream imageStream = null;

			if (filePart != null && filePart.getSize() > 0) {
				// ファイルとして保存せず、ストリームとして取得するだけ
				imageStream = filePart.getInputStream();
			}

			MenuDAO dao = new MenuDAO();
			// DAOのメソッドにInputStreamを渡す（DAO側も修正が必要）
			dao.addMenu(name, price, quantity, category, imageStream);

			request.setAttribute("success", "メニューをDBに登録しました");

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "登録に失敗しました");
		}

		doGet(request, response);
	}
}