package com.ems.dao;

import com.ems.model.Employee;
import com.ems.util.DBConnection;
import com.ems.util.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getInt("id"));
        e.setUserId(rs.getInt("user_id"));
        e.setName(rs.getString("name"));
        e.setDepartment(rs.getString("department"));
        e.setSalary(rs.getBigDecimal("salary"));
        e.setEmail(rs.getString("email"));
        e.setPhone(rs.getString("phone"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        return e;
    }

    public int getTotalCount() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM employees");
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getTotalCount", e);
        }
        return 0;
    }

    public List<Employee> getAllEmployees(int page, int pageSize, String sortBy, String sortOrder) {
        List<Employee> list = new ArrayList<>();
        String col = "name";
        if ("department".equalsIgnoreCase(sortBy)) col = "department";
        else if ("salary".equalsIgnoreCase(sortBy)) col = "salary";
        String dir = "DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC";

        String sql = "SELECT * FROM employees ORDER BY " + col + " " + dir + " LIMIT ? OFFSET ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getAllEmployees", e);
        }
        return list;
    }

    public Employee getEmployeeById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getEmployeeById", e);
        }
        return null;
    }

    public Employee getEmployeeByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getEmployeeByUserId", e);
        }
        return null;
    }

    public boolean addEmployee(Employee e) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String username = e.getEmail();
            if (username.contains("@")) username = username.substring(0, username.indexOf("@"));

            int userId = 0;
            try (PreparedStatement uStmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'EMPLOYEE')", Statement.RETURN_GENERATED_KEYS)) {
                uStmt.setString(1, username);
                uStmt.setString(2, PasswordUtil.hashPassword("employee123"));
                uStmt.executeUpdate();
                try (ResultSet gk = uStmt.getGeneratedKeys()) {
                    if (gk.next()) userId = gk.getInt(1);
                }
            }

            try (PreparedStatement empStmt = conn.prepareStatement("INSERT INTO employees (user_id, name, department, salary, email, phone) VALUES (?, ?, ?, ?, ?, ?)")) {
                empStmt.setInt(1, userId);
                empStmt.setString(2, e.getName());
                empStmt.setString(3, e.getDepartment());
                empStmt.setBigDecimal(4, e.getSalary());
                empStmt.setString(5, e.getEmail());
                empStmt.setString(6, e.getPhone());
                empStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException rex) {
                    throw new RuntimeException("Failed to rollback transaction", rex);
                }
            }
            throw new RuntimeException("Database error in addEmployee", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) {
                    throw new RuntimeException("Failed to close connection", ex);
                }
            }
        }
    }

    public boolean updateEmployee(Employee e) {
        String sql = "UPDATE employees SET name = ?, department = ?, salary = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getName());
            stmt.setString(2, e.getDepartment());
            stmt.setBigDecimal(3, e.getSalary());
            stmt.setString(4, e.getEmail());
            stmt.setString(5, e.getPhone());
            stmt.setInt(6, e.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Database error in updateEmployee", ex);
        }
    }

    public boolean deleteEmployee(int id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int userId = 0;
            try (PreparedStatement getStmt = conn.prepareStatement("SELECT user_id FROM employees WHERE id = ?")) {
                getStmt.setInt(1, id);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) userId = rs.getInt("user_id");
                }
            }

            try (PreparedStatement delEmp = conn.prepareStatement("DELETE FROM employees WHERE id = ?")) {
                delEmp.setInt(1, id);
                delEmp.executeUpdate();
            }

            if (userId > 0) {
                try (PreparedStatement delUser = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                    delUser.setInt(1, userId);
                    delUser.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException rex) {
                    throw new RuntimeException("Failed to rollback transaction", rex);
                }
            }
            throw new RuntimeException("Database error in deleteEmployee", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) {
                    throw new RuntimeException("Failed to close connection", ex);
                }
            }
        }
    }
}
