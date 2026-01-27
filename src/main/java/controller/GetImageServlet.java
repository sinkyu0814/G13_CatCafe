package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetImageServlet")
public class GetImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("id");
		if (idStr == null || idStr.isEmpty())
			return;

		// SQLは menus テーブルの image (BLOB) を取得するもの
		String sql = "SELECT image FROM menus WHERE menu_id = ?";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, Integer.parseInt(idStr));

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// BLOBデータを読み込む
					InputStream is = rs.getBinaryStream("image");

					if (is != null) {
						// ブラウザに「これは画像ですよ」と教える
						response.setContentType("image/jpeg");

						try (OutputStream os = response.getOutputStream()) {
							byte[] buffer = new byte[8192];
							int bytesRead;
							while ((bytesRead = is.read(buffer)) != -1) {
								os.write(buffer, 0, bytesRead);
							}
						}
					} else {
						// 画像がNULLの場合のデバッグ用（任意）
						System.out.println("ID:" + idStr + " の画像データは空(NULL)です。");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}