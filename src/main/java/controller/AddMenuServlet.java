package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import database.MenuDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Servlet implementation class AddMenuServlet
 */
public class AddMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "menu_images";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddMenuServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		try {
			String name = request.getParameter("name");
			String category = request.getParameter("category");

			String priceStr = request.getParameter("price");
			String quantityStr = request.getParameter("quantity");

			if (name == null || name.isEmpty() ||
					priceStr == null || priceStr.isEmpty() ||
					quantityStr == null || quantityStr.isEmpty()) {

				request.setAttribute("error", "必須項目を入力してください");
				request.getRequestDispatcher("addMenu.jsp").forward(request, response);
				return;
			}

			int price = Integer.parseInt(priceStr.trim());
			int quantity = Integer.parseInt(quantityStr.trim());

			// 画像処理
			Part filePart = request.getPart("image");
			String fileName = null;
			if (filePart != null && filePart.getSize() > 0) {
				String originalFileName = PathUtil.getFileName(filePart);

				String timestamp = LocalDateTime.now()
						.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

				fileName = timestamp + "_" + originalFileName;

				String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists())
					uploadDir.mkdirs(); // ←複数階層対応

				File file = new File(uploadDir, fileName);
				try (InputStream input = filePart.getInputStream()) {
					Files.copy(input, file.toPath());
				}
			}

			// DB保存
			MenuDAO dao = new MenuDAO();
			dao.addMenu(name, price, quantity, category, fileName);

			// ★ ここで addMenu.jsp に戻す ★
			request.setAttribute("message", "メニューを追加しました！");
			request.getRequestDispatcher("addMenu.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "価格と数量は数値で入力してください");
			request.getRequestDispatcher("addMenu.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
