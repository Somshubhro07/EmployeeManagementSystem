package com.ems.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("userId") != null);
        String role = loggedIn ? (String) session.getAttribute("role") : null;
        String requestURI = httpRequest.getRequestURI();

        if (!loggedIn) {
            // Not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            // Logged in, check role authorization
            if (requestURI.contains("/admin/") && !"ADMIN".equals(role)) {
                // Non-admin trying to access admin pages -> redirect to employee profile
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/employee/profile");
            } else if (requestURI.contains("/employee/") && !"EMPLOYEE".equals(role)) {
                // Admin trying to access employee pages -> redirect to admin dashboard
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/dashboard");
            } else {
                // Authorized, proceed
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {}
}
