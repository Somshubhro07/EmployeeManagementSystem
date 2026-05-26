package com.ems.servlet;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import com.ems.util.EmailUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

        if ("dashboard".equals(action)) {
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

            request.setAttribute("employees", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("nextSortOrder", "ASC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
            request.setAttribute("totalCount", totalCount);

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } else if ("add".equals(action)) {
            request.getRequestDispatcher("/admin/add-employee.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String salaryStr = request.getParameter("salary");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if ("add".equals(action) || "edit".equals(action)) {
            // Validation
            String errorMsg = null;
            if (name == null || name.trim().isEmpty() || department == null || department.trim().isEmpty() ||
                salaryStr == null || salaryStr.trim().isEmpty() || email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty()) {
                errorMsg = "All fields are required.";
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

            if ("add".equals(action)) {
                if (employeeDAO.addEmployee(emp)) {
                    String subject = "Welcome — Account Created";
                    String body = "Hello " + emp.getName() + ",\n\nYour account has been created.\n"
                                + "Username: " + emp.getEmail().split("@")[0] + "\nPassword: employee123\n\nHR Department";
                    EmailUtil.sendNotification(emp.getEmail(), subject, body);
                    request.getSession().setAttribute("successMsg", "Employee added successfully!");
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    request.setAttribute("errorMsg", "Failed to add employee. Email might exist.");
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
                    request.setAttribute("errorMsg", "Failed to update employee. Email might exist.");
                    request.setAttribute("employee", employeeDAO.getEmployeeById(id));
                    request.getRequestDispatcher("/admin/edit-employee.jsp").forward(request, response);
                }
            }
        } else if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    employeeDAO.deleteEmployee(Integer.parseInt(idParam));
                    request.getSession().setAttribute("successMsg", "Employee deleted successfully!");
                } catch (NumberFormatException e) {}
            }
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
}
