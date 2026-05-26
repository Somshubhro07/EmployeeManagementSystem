package com.ems.servlet;

import com.ems.dao.UserDAO;
import com.ems.model.User;
import com.ems.util.PasswordUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String passwordInput = request.getParameter("password");

        // Basic validation
        if (username == null || username.trim().isEmpty() || passwordInput == null || passwordInput.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Please enter both username and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getUserByUsername(username.trim());
        
        if (user != null) {
            // Hash the inputted password and check if it matches the stored hash
            String hashedInput = PasswordUtil.hashPassword(passwordInput.trim());
            if (hashedInput.equals(user.getPassword())) {
                // Successful login
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());

                // Redirect based on role
                if ("ADMIN".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/employee/profile");
                }
                return;
            }
        }

        // Invalid credentials
        request.setAttribute("errorMsg", "Invalid username or password.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
