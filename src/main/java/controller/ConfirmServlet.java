package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.MenuDTO;
import model.service.MenuService;
//@WebServlet("/ConfirmServlet")
public class ConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("ListServlet");
            return;
        }

        int id = Integer.parseInt(idStr);

        model.service.MenuService service = new MenuService();
        MenuDTO menu = service.getMenuById(id);

        if (menu == null) {
            response.sendRedirect("ListServlet");
            return;
        }

        request.setAttribute("menu", menu);

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/jsp/Confirm.jsp");
        rd.forward(request, response);
    }
}
