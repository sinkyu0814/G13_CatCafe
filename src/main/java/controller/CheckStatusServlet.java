package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CheckStatusServlet")
public class CheckStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		boolean isCleared = true;

		// セッションがあり、まだorderIdが残っているなら「まだ会計中」とみなす
		if (session != null && session.getAttribute("orderId") != null) {
			isCleared = false;
		}

		// JSON形式 {"isCleared": true/false} で結果を返す
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print("{\"isCleared\": " + isCleared + "}");
		out.flush();
	}
}