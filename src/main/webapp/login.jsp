<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Employee Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-body">

    <div class="card login-card animate-fade-up">
        <div class="login-header">
            <div class="brand" style="justify-content: center; margin-bottom: 0.75rem;">
                <div class="brand-dot"></div>
                <div class="brand-name" style="margin-bottom:0;">EMS Portal</div>
            </div>
            <h1 class="login-title" style="text-align: center;">Sign In</h1>
            <p class="login-subtitle" style="text-align: center;">Access your workspace</p>
        </div>

        <c:if test="${not empty errorMsg}">
            <div class="alert alert-error">
                <strong>Error:</strong> <c:out value="${errorMsg}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-group">
                <label for="username" class="form-label">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username" required autofocus autocomplete="off">
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="margin-top: 1rem;">
                Sign In
            </button>
        </form>

        <div style="margin-top: 2rem; text-align: center; font-size: 0.85rem; color: var(--stone-700);">
            <p>Admin: <strong>admin</strong> / <strong>admin123</strong></p>
            <p style="margin-top: 0.25rem;">Employee: <strong>vihaan.patel</strong> / <strong>employee123</strong></p>
        </div>
    </div>

</body>
</html>
