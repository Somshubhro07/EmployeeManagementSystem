<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Employee — Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Main Navigation Bar -->
    <header class="header-bar">
        <div class="brand">
            <div class="brand-dot"></div>
            <div class="brand-name">EMS Portal</div>
        </div>
        <div class="user-nav">
            <span class="user-tag">Admin Workspace</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <!-- Page Layout with Sidebar -->
    <div class="layout-with-sidebar">
        
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=add">Add Employee</a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=tasks">Tasks</a>
                </li>
            </ul>
        </aside>

        <!-- Main Workspace Area -->
        <main>
            
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-error">
                    <strong>Error:</strong> <c:out value="${errorMsg}" />
                </div>
            </c:if>

            <div class="card">
                <div style="border-bottom: 1px solid #e5e5e5; padding-bottom: 1rem; margin-bottom: 2rem;">
                    <h1 class="page-title" style="font-size: 2.25rem;">Modify Record</h1>
                    <p style="color: #666666; font-size: 0.9rem; margin-top: 0.25rem;">
                        Updating details for: <strong><c:out value="${employee.name}" /></strong> (ID: #<c:out value="${employee.id}" />)
                    </p>
                </div>

                <!-- Update Form -->
                <form action="${pageContext.request.contextPath}/admin/dashboard?action=edit&id=${employee.id}" method="POST">
                    
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.5rem;">
                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="name" class="form-label">Full Name</label>
                            <input type="text" id="name" name="name" class="form-control" placeholder="e.g. Rahul Sharma" required value="<c:out value='${employee.name}'/>">
                        </div>

                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="email" class="form-label">Email Address</label>
                            <input type="email" id="email" name="email" class="form-control" placeholder="e.g. rahul@company.in" required value="<c:out value='${employee.email}'/>">
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.5rem;">
                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="department" class="form-label">Department</label>
                            <select id="department" name="department" class="form-control" required>
                                <option value="Engineering" ${employee.department == 'Engineering' ? 'selected' : ''}>Engineering</option>
                                <option value="Finance" ${employee.department == 'Finance' ? 'selected' : ''}>Finance</option>
                                <option value="HR" ${employee.department == 'HR' ? 'selected' : ''}>HR</option>
                                <option value="Marketing" ${employee.department == 'Marketing' ? 'selected' : ''}>Marketing</option>
                                <option value="Operations" ${employee.department == 'Operations' ? 'selected' : ''}>Operations</option>
                                <option value="Management" ${employee.department == 'Management' ? 'selected' : ''}>Management</option>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="phone" class="form-label">Phone Number</label>
                            <input type="tel" id="phone" name="phone" class="form-control" placeholder="e.g. 9876543210" required value="<c:out value='${employee.phone}'/>">
                        </div>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.5rem;">
                        <label for="salary" class="form-label">Salary (Monthly INR)</label>
                        <input type="number" step="0.01" id="salary" name="salary" class="form-control" placeholder="e.g. 75000.00" required value="<c:out value='${employee.salary}'/>">
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 2rem;">
                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" id="address" name="address" class="form-control" placeholder="e.g. 12 MG Road" value="<c:out value='${employee.address}'/>">
                        </div>
                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="state" class="form-label">State</label>
                            <input type="text" id="state" name="state" class="form-control" placeholder="e.g. Maharashtra" value="<c:out value='${employee.state}'/>">
                        </div>
                    </div>

                    <div style="display: flex; gap: 1rem; justify-content: flex-end;">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                            Cancel
                        </a>
                        <button type="submit" class="btn btn-primary">
                            Save Changes
                        </button>
                    </div>

                </form>
            </div>

        </main>
    </div>

</body>
</html>
