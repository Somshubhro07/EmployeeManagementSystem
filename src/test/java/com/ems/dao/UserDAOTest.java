package com.ems.dao;

import com.ems.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @Test
    public void testGetUserByUsername_AdminExists() {
        UserDAO userDAO = new UserDAO();
        User admin = userDAO.getUserByUsername("admin");
        
        assertNotNull(admin, "Admin user should exist in the database (seeded)");
        assertEquals("admin", admin.getUsername(), "Username should be admin");
        assertEquals("ADMIN", admin.getRole(), "Role should be ADMIN");
    }

    @Test
    public void testGetUserByUsername_EmployeeExists() {
        UserDAO userDAO = new UserDAO();
        User employee = userDAO.getUserByUsername("vihaan.patel");
        
        assertNotNull(employee, "Employee user should exist in the database (seeded)");
        assertEquals("vihaan.patel", employee.getUsername(), "Username should match");
        assertEquals("EMPLOYEE", employee.getRole(), "Role should be EMPLOYEE");
    }

    @Test
    public void testGetUserByUsername_NotFound() {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername("nonexistent.user");
        
        assertNull(user, "User should be null for nonexistent username");
    }
}
