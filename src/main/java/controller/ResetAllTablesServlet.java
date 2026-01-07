package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import database.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // 追加
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ResetAllTablesServlet") // ★ これを忘れると別のサーブレットに飛ぶことがあります
public class ResetAllTablesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 未会計（NEW）の注文をすべて会計済み（PAID）にする
		String sql = "UPDATE orders SET status = 'PAID' WHERE status = 'NEW'";

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("リセット失敗", e);
		}

		// 席選択画面のサーブレットへ戻る
		response.sendRedirect("TableSelectServlet");
	}
}