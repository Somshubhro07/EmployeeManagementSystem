<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Tasks - Admin Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="header-bar">
        <div class="brand">
            <div class="brand-dot"></div>
            <div class="brand-name" style="margin-bottom:0;">EMS Portal</div>
        </div>
        <div class="user-nav">
            <span class="user-tag">Admin: <c:out value="${sessionScope.username}" /></span>
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
            <c:if test="${not empty sessionScope.successMsg}">
                <div class="alert alert-success"><c:out value="${sessionScope.successMsg}" /></div>
                <c:remove var="successMsg" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.errorMsg}">
                <div class="alert alert-error"><c:out value="${sessionScope.errorMsg}" /></div>
                <c:remove var="errorMsg" scope="session" />
            </c:if>

            <div class="card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <h1 style="margin-bottom: 0;">Assigned Tasks</h1>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=assign" class="btn btn-primary">Assign Task</a>
                </div>

                <div class="table-responsive">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Employee</th>
                                <th>Task</th>
                                <th>Deadline</th>
                                <th>Status</th>
                                <th style="text-align: right;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty tasks}">
                                    <c:forEach var="task" items="${tasks}">
                                        <tr>
                                            <td style="font-weight: 500;"><c:out value="${task.employeeName}" /></td>
                                            <td>
                                                <strong><c:out value="${task.taskTitle}" /></strong>
                                                <c:if test="${not empty task.taskDescription}">
                                                    <br><span style="color: #666666; font-size: 0.85rem;"><c:out value="${task.taskDescription}" /></span>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${task.deadline}" /></td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/admin/dashboard?action=updateTaskStatus&id=${task.id}" method="POST" style="display:inline;">
                                                    <select name="status" class="form-control" style="padding: 6px 10px; width: auto; font-size: 0.85rem;" onchange="this.form.submit()">
                                                        <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                                        <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                                        <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                                    </select>
                                                </form>
                                            </td>
                                            <td style="text-align: right;">
                                                <form action="${pageContext.request.contextPath}/admin/dashboard?action=deleteTask&id=${task.id}" method="POST" onsubmit="return confirm('Delete this task?');" style="display:inline;">
                                                    <button type="submit" class="btn btn-primary btn-action">Delete</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: #666666; padding: 3rem;">No tasks assigned yet.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
