package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminTableSetServlet")
public class AdminTableSetServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String fixedTableNo = request.getParameter("fixedTableNo");

		// セッションに固定テーブル番号を保存（ブラウザを閉じるまで有効）
		HttpSession session = request.getSession();
		session.setAttribute("fixedTableNo", fixedTableNo);

		// 人数入力画面（初期画面）へリダイレクト
		response.sendRedirect("ToppageServlet");
	}
}