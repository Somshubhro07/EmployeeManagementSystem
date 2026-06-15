package com.ems.dao;

import com.ems.model.Task;
import com.ems.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private Task mapTask(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getInt("task_id"));
        t.setEmployeeName(rs.getString("employee_name"));
        t.setTaskTitle(rs.getString("task_title"));
        t.setTaskDescription(rs.getString("task_description"));
        t.setDeadline(rs.getString("deadline"));
        t.setStatus(rs.getString("status"));
        return t;
    }

    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM employee_tasks ORDER BY task_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapTask(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getAllTasks", e);
        }
        return list;
    }

    public List<Task> getTasksByEmployeeName(String employeeName) {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM employee_tasks WHERE employee_name = ? ORDER BY task_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapTask(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getTasksByEmployeeName", e);
        }
        return list;
    }

    public Task getTaskById(int id) {
        String sql = "SELECT * FROM employee_tasks WHERE task_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapTask(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error in getTaskById", e);
        }
        return null;
    }

    public boolean addTask(Task t) {
        String sql = "INSERT INTO employee_tasks (employee_name, task_title, task_description, deadline, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getEmployeeName());
            stmt.setString(2, t.getTaskTitle());
            stmt.setString(3, t.getTaskDescription());
            stmt.setString(4, t.getDeadline());
            stmt.setString(5, t.getStatus() != null ? t.getStatus() : "PENDING");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error in addTask", e);
        }
    }

    public boolean updateTaskStatus(int id, String status) {
        String sql = "UPDATE employee_tasks SET status = ? WHERE task_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error in updateTaskStatus", e);
        }
    }

    public boolean deleteTask(int id) {
        String sql = "DELETE FROM employee_tasks WHERE task_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error in deleteTask", e);
        }
    }
}
