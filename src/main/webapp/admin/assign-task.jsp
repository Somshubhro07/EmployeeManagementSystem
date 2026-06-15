<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assign Task - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="header-bar">
        <div class="brand">
            <div class="brand-dot"></div>
            <div class="brand-name" style="margin-bottom:0;">EMS Portal</div>
        </div>
        <div class="user-nav">
            <span class="user-tag">Admin Workspace</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="layout-with-sidebar">
        <aside class="sidebar">
            <ul class="sidebar-menu">
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                </li>
                <li class="sidebar-item">
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=add">Add Employee</a>
                </li>
                <li class="sidebar-item active">
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=tasks">Tasks</a>
                </li>
            </ul>
        </aside>

        <main>
            <c:if test="${not empty errorMsg}">
                <div class="alert alert-error"><c:out value="${errorMsg}" /></div>
            </c:if>

            <div class="card">
                <div style="border-bottom: 1px solid #e5e5e5; padding-bottom: 1rem; margin-bottom: 2rem;">
                    <h1 style="font-size: 2.25rem;">Assign Task</h1>
                    <p style="color: #666666; font-size: 0.9rem;">Assign a new task to an employee.</p>
                </div>

                <form action="${pageContext.request.contextPath}/admin/dashboard?action=assign" method="POST">

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.5rem;">
                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="employeeName" class="form-label">Employee</label>
                            <select id="employeeName" name="employeeName" class="form-control" required>
                                <option value="" disabled selected>Select Employee</option>
                                <c:forEach var="name" items="${employeeNames}">
                                    <option value="${name}"><c:out value="${name}" /></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="deadline" class="form-label">Deadline</label>
                            <input type="date" id="deadline" name="deadline" class="form-control" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="taskTitle" class="form-label">Task Title</label>
                        <input type="text" id="taskTitle" name="taskTitle" class="form-control" placeholder="e.g. Code Review" required>
                    </div>

                    <div class="form-group" style="margin-bottom: 2rem;">
                        <label for="taskDescription" class="form-label">Description</label>
                        <textarea id="taskDescription" name="taskDescription" class="form-control" rows="4" placeholder="Brief description of the task"></textarea>
                    </div>

                    <div style="display: flex; gap: 1rem; justify-content: flex-end;">
                        <a href="${pageContext.request.contextPath}/admin/dashboard?action=tasks" class="btn btn-secondary">Cancel</a>
                        <button type="submit" class="btn btn-primary">Assign Task</button>
                    </div>
                </form>
            </div>
        </main>
    </div>

</body>
</html>
