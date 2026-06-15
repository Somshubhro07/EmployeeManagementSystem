<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Profile — EMS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Main Sticky Navigation Bar -->
    <header class="header-bar">
        <div class="brand">
            <div class="brand-dot"></div>
            <div class="brand-name">EMS Portal</div>
        </div>
        <div class="user-nav">
            <span class="user-tag">Employee Workspace</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <main class="main-wrapper">
        <div style="display: flex; gap: 1rem; margin-bottom: 2rem;">
            <a href="${pageContext.request.contextPath}/employee/profile" class="btn btn-primary">Profile</a>
            <a href="${pageContext.request.contextPath}/employee/profile?action=tasks" class="btn btn-secondary">My Tasks</a>
        </div>

        <c:if test="${not empty errorMsg}">
            <div class="alert alert-error">
                <strong>Error:</strong> <c:out value="${errorMsg}" />
            </div>
        </c:if>

        <c:if test="${not empty employee}">
            <div class="card">
                <div style="border-bottom: 1px solid #e5e5e5; padding-bottom: 1.5rem; margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <span style="font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.15em; color: #666666; font-weight: 600;">Welcome back</span>
                        <h1 style="font-size: 3rem; line-height: 1.1; margin-top: 0.25rem;"><c:out value="${employee.name}" /></h1>
                    </div>
                    <div style="text-align: right;">
                        <span style="font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.05em; color: #666666; display: block;">Department</span>
                        <span style="font-size: 1.25rem; font-weight: 500; color: #000000;"><c:out value="${employee.department}" /></span>
                    </div>
                </div>

                <div class="profile-grid">
                    <div class="profile-field">
                        <div class="profile-field-label">Employee ID</div>
                        <div class="profile-field-value">#<c:out value="${employee.id}" /></div>
                    </div>

                    <div class="profile-field">
                        <div class="profile-field-label">Salary (Monthly)</div>
                        <div class="profile-field-value salary-val">
                            INR <fmt:formatNumber value="${employee.salary}" type="number" maxFractionDigits="2" minFractionDigits="2"/>
                        </div>
                    </div>

                    <div class="profile-field">
                        <div class="profile-field-label">Email Address</div>
                        <div class="profile-field-value"><c:out value="${employee.email}" /></div>
                    </div>

                    <div class="profile-field">
                        <div class="profile-field-label">Contact Number</div>
                        <div class="profile-field-value"><c:out value="${employee.phone}" /></div>
                    </div>

                    <div class="profile-field">
                        <div class="profile-field-label">Address</div>
                        <div class="profile-field-value" style="font-size: 1rem;"><c:out value="${employee.address}" default="--" /></div>
                    </div>

                    <div class="profile-field">
                        <div class="profile-field-label">State</div>
                        <div class="profile-field-value" style="font-size: 1rem;"><c:out value="${employee.state}" default="--" /></div>
                    </div>

                    <div class="profile-field" style="grid-column: span 2;">
                        <div class="profile-field-label">Account Created At</div>
                        <div class="profile-field-value" style="font-size: 1rem; font-weight: 400; color: #666666;">
                            <fmt:formatDate value="${employee.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

    </main>

</body>
</html>
