package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import database.SalesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.SalesDTO;

@WebServlet("/SalesServlet")
public class SalesServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = request.getParameter("type");
		if (type == null)
			type = "year";

		// デフォルトは現在の年・月
		LocalDate now = LocalDate.now();
		String year = request.getParameter("year");
		if (year == null)
			year = String.valueOf(now.getYear());

		String month = request.getParameter("month");
		if (month == null)
			month = String.format("%02d", now.getMonthValue());

		SalesDAO dao = new SalesDAO();
		List<SalesDTO> salesList = dao.getSalesData(type, year, month);

		request.setAttribute("salesList", salesList);
		request.setAttribute("currentType", type);
		request.setAttribute("selectedYear", year);
		request.setAttribute("selectedMonth", month);

		request.getRequestDispatcher("/WEB-INF/jsp/SalesDashboard.jsp").forward(request, response);
	}
}