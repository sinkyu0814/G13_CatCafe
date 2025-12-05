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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

//@WebServlet("/AddMenuServlet")
/**
 * Servlet implementation class AddMenuServlet
 */
@MultipartConfig
public class AddMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// webapp/assets/images/ に保存する設定（先頭に / が必要）
	private static final String UPLOAD_DIR = "/assets/images/";

	public AddMenuServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		try {
			String name = request.getParameter("name");
			String category = request.getParameter("category");

			String priceStr = request.getParameter("price");
			String quantityStr = request.getParameter("quantity");

			// 必須チェック
			if (name == null || name.isEmpty() ||
					priceStr == null || priceStr.isEmpty() ||
					quantityStr == null || quantityStr.isEmpty()) {

				request.setAttribute("error", "必須項目を入力してください");
				request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp").forward(request, response);
				return;
			}

			int price = Integer.parseInt(priceStr.trim());
			int quantity = Integer.parseInt(quantityStr.trim());

			// 画像アップロード処理
			Part filePart = request.getPart("image");
			String fileName = null;

			if (filePart != null && filePart.getSize() > 0) {

				// 元のファイル名
				String originalFileName = PathUtil.getFileName(filePart);

				// タイムスタンプ付ファイル名
				String timestamp = LocalDateTime.now()
						.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				fileName = timestamp + "_" + originalFileName;

				// 保存先フォルダの実際のパス
				String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);

				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// ファイル保存
				File file = new File(uploadDir, fileName);
				try (InputStream input = filePart.getInputStream()) {
					Files.copy(input, file.toPath());
				}
			}

			// DB に保存（image にはファイル名だけ）
			MenuDAO dao = new MenuDAO();
			dao.addMenu(name, price, quantity, category, fileName);

			// 完了メッセージ
			request.setAttribute("success", "メニューを追加しました！");
			request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "価格と数量は数値で入力してください");
			request.getRequestDispatcher("/WEB-INF/jsp/addMenu.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
