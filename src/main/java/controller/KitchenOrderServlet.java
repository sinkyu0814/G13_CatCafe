package controller;

import java.io.IOException;
import java.util.List;

import database.KitchenOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.KitchenOrderDTO;

//@WebServlet("/KitchenOrderServlet")
/**
 * Servlet implementation class KitchenOrderServlet
 */
public class KitchenOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KitchenOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		response.getWriter().append("Served at: ").append(request.getContextPath());KitchenOrderDAO dao = new KitchenOrderDAO();
		List<KitchenOrderDTO> list;

		database.KitchenOrderDAO dao = new KitchenOrderDAO();
		try {
			list = dao.findAll();
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("orders", list);
		request.getRequestDispatcher("/WEB-INF/jsp/orderList.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		doGet(request, response);
	}

}
