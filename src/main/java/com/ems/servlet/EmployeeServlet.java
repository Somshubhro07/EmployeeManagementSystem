package com.ems.servlet;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmployeeServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        Employee emp = employeeDAO.getEmployeeByUserId(userId);

        if (emp != null) {
            request.setAttribute("employee", emp);
        } else {
            request.setAttribute("errorMsg", "Employee profile not found.");
        }
        
        request.getRequestDispatcher("/employee/profile.jsp").forward(request, response);
    }
}
