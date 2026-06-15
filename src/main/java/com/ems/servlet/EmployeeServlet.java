package com.ems.servlet;

import com.ems.dao.EmployeeDAO;
import com.ems.dao.TaskDAO;
import com.ems.model.Employee;
import com.ems.model.Task;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmployeeServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final TaskDAO taskDAO = new TaskDAO();

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

        if (emp == null) {
            request.setAttribute("errorMsg", "Employee profile not found.");
            request.getRequestDispatcher("/employee/profile.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        if ("tasks".equals(action)) {
            List<Task> tasks = taskDAO.getTasksByEmployeeName(emp.getName());
            request.setAttribute("employee", emp);
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("/employee/tasks.jsp").forward(request, response);
        } else {
            request.setAttribute("employee", emp);
            request.getRequestDispatcher("/employee/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateTaskStatus".equals(action)) {
            String idParam = request.getParameter("id");
            String status = request.getParameter("status");
            if (idParam != null && status != null) {
                try {
                    taskDAO.updateTaskStatus(Integer.parseInt(idParam), status.trim());
                    request.getSession().setAttribute("successMsg", "Task status updated!");
                } catch (NumberFormatException e) {}
            }
            response.sendRedirect(request.getContextPath() + "/employee/profile?action=tasks");
        }
    }
}
