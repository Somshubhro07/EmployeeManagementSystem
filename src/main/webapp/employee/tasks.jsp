<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Tasks - EMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="header-bar">
        <div class="brand">
            <div class="brand-dot"></div>
            <div class="brand-name" style="margin-bottom:0;">EMS Portal</div>
        </div>
        <div class="user-nav">
            <span class="user-tag">Employee: <c:out value="${employee.name}" /></span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="main-wrapper">
        <div style="display: flex; gap: 1rem; margin-bottom: 2rem;">
            <a href="${pageContext.request.contextPath}/employee/profile" class="btn btn-secondary">Profile</a>
            <a href="${pageContext.request.contextPath}/employee/profile?action=tasks" class="btn btn-primary">My Tasks</a>
        </div>

        <c:if test="${not empty sessionScope.successMsg}">
            <div class="alert alert-success"><c:out value="${sessionScope.successMsg}" /></div>
            <c:remove var="successMsg" scope="session" />
        </c:if>

        <div class="card">
            <h1 style="margin-bottom: 2rem;">My Tasks</h1>

            <div class="table-responsive">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Task</th>
                            <th>Description</th>
                            <th>Deadline</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty tasks}">
                                <c:forEach var="task" items="${tasks}">
                                    <tr>
                                        <td style="font-weight: 500;"><c:out value="${task.taskTitle}" /></td>
                                        <td style="color: #666666; font-size: 0.9rem;"><c:out value="${task.taskDescription}" /></td>
                                        <td><c:out value="${task.deadline}" /></td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/employee/profile?action=updateTaskStatus&id=${task.id}" method="POST" style="display:inline;">
                                                <select name="status" class="form-control" style="padding: 6px 10px; width: auto; font-size: 0.85rem;" onchange="this.form.submit()">
                                                    <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                                    <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                                    <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                                </select>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" style="text-align: center; color: #666666; padding: 3rem;">No tasks assigned to you.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

</body>
</html>
