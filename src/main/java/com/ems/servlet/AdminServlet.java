package com.ems.servlet;

import com.ems.dao.EmployeeDAO;
import com.ems.dao.TaskDAO;
import com.ems.model.Employee;
import com.ems.model.Task;
import com.ems.util.EmailUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final TaskDAO taskDAO = new TaskDAO();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "dashboard";

        switch (action) {
            case "add":
                request.getRequestDispatcher("/admin/add-employee.jsp").forward(request, response);
                break;
            case "edit":
                handleEditGet(request, response);
                break;
            case "tasks":
                request.setAttribute("tasks", taskDAO.getAllTasks());
                request.getRequestDispatcher("/admin/tasks.jsp").forward(request, response);
                break;
            case "assign":
                request.setAttribute("employeeNames", employeeDAO.getAllEmployeeNames());
                request.getRequestDispatcher("/admin/assign-task.jsp").forward(request, response);
                break;
            default:
                handleDashboard(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action) || "edit".equals(action)) {
            handleEmployeePost(action, request, response);
        } else if ("delete".equals(action)) {
            handleDeleteEmployee(request, response);
        } else if ("assign".equals(action)) {
            handleAssignTask(request, response);
        } else if ("deleteTask".equals(action)) {
            handleDeleteTask(request, response);
        } else if ("updateTaskStatus".equals(action)) {
            handleUpdateTaskStatus(request, response);
        }
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException e) {}
        }

        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "name";

        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null || sortOrder.trim().isEmpty()) sortOrder = "ASC";

        int totalCount = employeeDAO.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<Employee> list = employeeDAO.getAllEmployees(page, PAGE_SIZE, sortBy, sortOrder);
        Map<String, Object> stats = employeeDAO.getEmployeeStats();

        request.setAttribute("employees", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("nextSortOrder", "ASC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("stats", stats);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    private void handleEditGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Employee emp = employeeDAO.getEmployeeById(Integer.parseInt(idParam));
                if (emp != null) {
                    request.setAttribute("employee", emp);
                    request.getRequestDispatcher("/admin/edit-employee.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {}
        }
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }

    private void handleEmployeePost(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String salaryStr = request.getParameter("salary");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String state = request.getParameter("state");

        // Validation
        String errorMsg = null;
        if (name == null || name.trim().isEmpty() || department == null || department.trim().isEmpty() ||
            salaryStr == null || salaryStr.trim().isEmpty() || email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty()) {
            errorMsg = "All required fields must be filled.";
        } else if (!email.trim().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            errorMsg = "Please enter a valid email address.";
        } else {
            try {
                BigDecimal salary = new BigDecimal(salaryStr.trim());
                if (salary.compareTo(BigDecimal.ZERO) <= 0) {
                    errorMsg = "Salary must be a positive number.";
                }
            } catch (NumberFormatException e) {
                errorMsg = "Salary must be a valid numeric value.";
            }
        }

        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg);
            if ("add".equals(action)) {
                request.getRequestDispatcher("/admin/add-employee.jsp").forward(request, response);
            } else {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("employee", employeeDAO.getEmployeeById(id));
                request.getRequestDispatcher("/admin/edit-employee.jsp").forward(request, response);
            }
            return;
        }

        Employee emp = new Employee();
        emp.setName(name.trim());
        emp.setDepartment(department.trim());
        emp.setSalary(new BigDecimal(salaryStr.trim()));
        emp.setEmail(email.trim().toLowerCase());
        emp.setPhone(phone.trim());
        emp.setAddress(address != null ? address.trim() : "");
        emp.setState(state != null ? state.trim() : "");

        if ("add".equals(action)) {
            if (employeeDAO.addEmployee(emp)) {
                String subject = "Welcome - Account Created";
                String body = "Hello " + emp.getName() + ",\n\nYour account has been created.\n"
                            + "Username: " + emp.getEmail().split("@")[0] + "\nPassword: employee123\n\nHR Department";
                EmailUtil.sendNotification(emp.getEmail(), subject, body);
                request.getSession().setAttribute("successMsg", "Employee added successfully!");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                request.setAttribute("errorMsg", "Failed to add employee. Email might already exist.");
                request.getRequestDispatcher("/admin/add-employee.jsp").forward(request, response);
            }
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            emp.setId(id);
            if (employeeDAO.updateEmployee(emp)) {
                String subject = "Employee Record Updated";
                String body = "Hello " + emp.getName() + ",\n\nYour profile details have been updated.\n\nHR Department";
                EmailUtil.sendNotification(emp.getEmail(), subject, body);
                request.getSession().setAttribute("successMsg", "Employee updated successfully!");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                request.setAttribute("errorMsg", "Failed to update employee.");
                request.setAttribute("employee", employeeDAO.getEmployeeById(id));
                request.getRequestDispatcher("/admin/edit-employee.jsp").forward(request, response);
            }
        }
    }

    private void handleDeleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                employeeDAO.deleteEmployee(Integer.parseInt(idParam));
                request.getSession().setAttribute("successMsg", "Employee deleted successfully!");
            } catch (NumberFormatException e) {}
        }
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }

    private void handleAssignTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String empName = request.getParameter("employeeName");
        String title = request.getParameter("taskTitle");
        String desc = request.getParameter("taskDescription");
        String deadline = request.getParameter("deadline");

        if (empName == null || empName.trim().isEmpty() || title == null || title.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Employee name and task title are required.");
            request.setAttribute("employeeNames", employeeDAO.getAllEmployeeNames());
            request.getRequestDispatcher("/admin/assign-task.jsp").forward(request, response);
            return;
        }

        Task task = new Task();
        task.setEmployeeName(empName.trim());
        task.setTaskTitle(title.trim());
        task.setTaskDescription(desc != null ? desc.trim() : "");
        task.setDeadline(deadline != null ? deadline.trim() : "");
        task.setStatus("PENDING");

        if (taskDAO.addTask(task)) {
            // Find employee email for notification
            List<Employee> allEmps = employeeDAO.getAllEmployees(1, 100, "name", "ASC");
            for (Employee e : allEmps) {
                if (e.getName().equals(empName.trim())) {
                    String subject = "New Task Assigned: " + title.trim();
                    String body = "Hello " + e.getName() + ",\n\nA new task has been assigned to you.\n"
                                + "Title: " + title.trim() + "\nDeadline: " + deadline + "\n\nHR Department";
                    EmailUtil.sendNotification(e.getEmail(), subject, body);
                    break;
                }
            }
            request.getSession().setAttribute("successMsg", "Task assigned successfully!");
        } else {
            request.getSession().setAttribute("errorMsg", "Failed to assign task.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/dashboard?action=tasks");
    }

    private void handleDeleteTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                taskDAO.deleteTask(Integer.parseInt(idParam));
                request.getSession().setAttribute("successMsg", "Task deleted successfully!");
            } catch (NumberFormatException e) {}
        }
        response.sendRedirect(request.getContextPath() + "/admin/dashboard?action=tasks");
    }

    private void handleUpdateTaskStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String status = request.getParameter("status");
        if (idParam != null && status != null) {
            try {
                taskDAO.updateTaskStatus(Integer.parseInt(idParam), status.trim());
                request.getSession().setAttribute("successMsg", "Task status updated!");
            } catch (NumberFormatException e) {}
        }
        response.sendRedirect(request.getContextPath() + "/admin/dashboard?action=tasks");
    }
}
