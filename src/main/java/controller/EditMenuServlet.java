package controller;

import java.io.IOException;
import java.io.InputStream;

import database.MenuDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.dto.MenuDTO;

@MultipartConfig
@WebServlet("/EditMenuServlet")
public class EditMenuServlet extends HttpServlet {

	// 編集画面を表示する
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		MenuDAO dao = new MenuDAO();
		MenuDTO menu = dao.findById(id); // 既存のメソッドを利用

		request.setAttribute("menu", menu);
		request.getRequestDispatcher("/WEB-INF/jsp/editMenu.jsp").forward(request, response);
	}

	// 更新処理を実行する
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String category = request.getParameter("category");
			int price = Integer.parseInt(request.getParameter("price"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));

			Part filePart = request.getPart("image");
			InputStream imageStream = null;
			if (filePart != null && filePart.getSize() > 0) {
				imageStream = filePart.getInputStream();
			}

			MenuDAO dao = new MenuDAO();
			dao.updateMenu(id, name, price, quantity, category, imageStream);

			response.sendRedirect("AddMenuServlet"); // 一覧画面に戻る
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}