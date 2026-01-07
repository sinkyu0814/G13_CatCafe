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
import model.dto.RankingDTO;

@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String period = request.getParameter("period");
		if (period == null)
			period = "day"; // 初期表示は「本日」

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start;
		String label;

		// 期間の計算
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
			// 「本日」は今日の 00:00:00 から
			start = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
			label = "本日";
		}

		String startDateStr = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		RankingDAO dao = new RankingDAO();
		List<RankingDTO> list = dao.getTopTenRanking(startDateStr);
		int total = dao.getTotalOrderCount(startDateStr);

		// --- デバッグログ ---
		System.out.println("検索条件: " + label + " (" + startDateStr + " 以降)");
		System.out.println("データ取得数: " + list.size() + " 件");
		// ------------------

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
		request.getRequestDispatcher("/WEB-INF/jsp/Ranking.jsp").forward(request, response);
	}
}