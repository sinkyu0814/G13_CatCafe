package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import database.RankingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.RankingDTO;

@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("isLoggedIn") == null) {
			response.sendRedirect("LoginServlet");
			return;
		}
		// --- 追加：集計タイプの取得 (all: セット, item: 商品のみ, option: オプションのみ) ---
		String filterType = request.getParameter("filterType");
		if (filterType == null)
			filterType = "all";

		String period = request.getParameter("period");
		if (period == null)
			period = "day";

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;
		String label;

		if ("week".equals(period)) {
			start = now.minusWeeks(1);
			label = "1週間";
		} else if ("month".equals(period)) {
			start = now.minusMonths(1);
			label = "1ヶ月";
		} else if ("year".equals(period)) {
			start = now.minusYears(1);
			label = "1年";
		} else {
			start = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
			label = "本日";
		}

		String startDateStr = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		RankingDAO dao = new RankingDAO();
		// DAOに filterType を渡す
		List<RankingDTO> list = dao.getTopTenRanking(startDateStr, filterType);
		int total = dao.getTotalOrderCount(startDateStr, filterType);

		for (RankingDTO item : list) {
			if (total > 0) {
				double r = (double) item.getOrderCount() / total * 100;
				item.setRatio(Math.round(r * 10.0) / 10.0);
			}
		}

		request.setAttribute("rankingList", list);
		request.setAttribute("totalCount", total);
		request.setAttribute("periodLabel", label);
		request.setAttribute("currentPeriod", period);
		request.setAttribute("selectedFilter", filterType); // JSPに現在の選択を戻す

		request.getRequestDispatcher("/WEB-INF/jsp/Ranking.jsp").forward(request, response);
	}
}