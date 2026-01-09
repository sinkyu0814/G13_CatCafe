package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import database.MenuDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.dto.MenuDTO;
import model.service.MenuService;

@MultipartConfig
@WebServlet("/AddMenuServlet")
public class AddMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "/assets/images/";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ★ メニュー一覧取得
		MenuService service = new MenuService();
		List<MenuDTO> menuList = service.getMenuList("all");
		request.setAttribute("menuList", menuList);

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

			if (name == null || name.isEmpty()
					|| priceStr == null || priceStr.isEmpty()
					|| quantityStr == null || quantityStr.isEmpty()) {

				request.setAttribute("error", "必須項目を入力してください");
				doGet(request, response);
				return;
			}

			int price = Integer.parseInt(priceStr);
			int quantity = Integer.parseInt(quantityStr);

			// --- 画像処理 ---
			Part filePart = request.getPart("image");
			String fileName = null;

			if (filePart != null && filePart.getSize() > 0) {
				String original = filePart.getSubmittedFileName();
				String timestamp = LocalDateTime.now()
						.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				fileName = timestamp + "_" + original;

				String uploadPath = getServletContext().getRealPath(UPLOAD_DIR);
				File dir = new File(uploadPath);
				if (!dir.exists())
					dir.mkdirs();

				try (InputStream in = filePart.getInputStream()) {
					Files.copy(in, new File(dir, fileName).toPath());
				}
			}

			MenuDAO dao = new MenuDAO();
			dao.addMenu(name, price, quantity, category, fileName);

			request.setAttribute("success", "メニューを追加しました");

		} catch (Exception e) {
			request.setAttribute("error", "登録に失敗しました");
		}

		doGet(request, response); // ★ 必ず一覧再表示
	}
}
