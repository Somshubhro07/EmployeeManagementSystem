package com.ems.dao;

import com.ems.model.Employee;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDAOTest {

    @Test
    public void testGetTotalCount() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        int count = employeeDAO.getTotalCount();
        assertTrue(count >= 15, "There should be at least 15 seeded employees");
    }

    @Test
    public void testGetAllEmployees() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        List<Employee> employees = employeeDAO.getAllEmployees(1, 5, "name", "ASC");
        
        assertNotNull(employees);
        assertEquals(5, employees.size(), "Should return exactly 5 employees for page 1");
    }

    @Test
    public void testGetEmployeeById() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee emp = employeeDAO.getEmployeeById(1);
        
        assertNotNull(emp, "Employee with ID 1 should exist");
        assertNotNull(emp.getName(), "Employee name should not be null");
    }

    @Test
    public void testAddUpdateDeleteEmployeeFlow() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        // 1. Add Employee
        Employee newEmp = new Employee();
        newEmp.setName("Test User");
        newEmp.setDepartment("Engineering");
        newEmp.setSalary(new BigDecimal("50000.00"));
        newEmp.setEmail("test.user" + System.currentTimeMillis() + "@company.in");
        newEmp.setPhone("1234567890");
        
        boolean added = employeeDAO.addEmployee(newEmp);
        assertTrue(added, "Employee should be added successfully");
        
        // Find the added employee ID (since our DAO doesn't return the new ID directly, we'll find by email which becomes username)
        int newTotal = employeeDAO.getTotalCount();
        List<Employee> allEmps = employeeDAO.getAllEmployees(1, newTotal, "id", "DESC");
        Employee fetchedEmp = allEmps.stream()
            .filter(e -> e.getEmail().equals(newEmp.getEmail()))
            .findFirst()
            .orElse(null);
            
        assertNotNull(fetchedEmp, "Should be able to fetch the newly added employee");
        
        // 2. Update Employee
        fetchedEmp.setSalary(new BigDecimal("55000.00"));
        boolean updated = employeeDAO.updateEmployee(fetchedEmp);
        assertTrue(updated, "Employee should be updated successfully");
        
        Employee reFetched = employeeDAO.getEmployeeById(fetchedEmp.getId());
        assertEquals(0, new BigDecimal("55000.00").compareTo(reFetched.getSalary()), "Salary should be updated");
        
        // 3. Delete Employee
        boolean deleted = employeeDAO.deleteEmployee(fetchedEmp.getId());
        assertTrue(deleted, "Employee should be deleted successfully");
        
        Employee deletedEmp = employeeDAO.getEmployeeById(fetchedEmp.getId());
        assertNull(deletedEmp, "Deleted employee should not be found");
    }
}
