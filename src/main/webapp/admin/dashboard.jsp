<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard — EMS</title>
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
            <span class="user-tag">Admin: <c:out value="${username}" /></span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <!-- Main Page Layout with Sidebar -->
    <div class="layout-with-sidebar">
        
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <ul class="sidebar-menu">
                <li class="sidebar-item active">
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

        <!-- Dashboard Content -->
        <main>

            <!-- Flash Session Messages -->
            <c:if test="${not empty sessionScope.successMsg}">
                <div class="alert alert-success">
                    <strong>Success:</strong> <c:out value="${sessionScope.successMsg}" />
                </div>
                <c:remove var="successMsg" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.errorMsg}">
                <div class="alert alert-error">
                    <strong>Error:</strong> <c:out value="${sessionScope.errorMsg}" />
                </div>
                <c:remove var="errorMsg" scope="session" />
            </c:if>

            <c:if test="${not empty stats}">
                <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.5rem; margin-bottom: 2rem;">
                    <div class="card" style="padding: 1.5rem;">
                        <div style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; color: #666666;">Total Employees</div>
                        <div style="font-family: 'Syne', sans-serif; font-size: 2.5rem; font-weight: 700;">${stats.totalEmployees}</div>
                    </div>
                    <div class="card" style="padding: 1.5rem;">
                        <div style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; color: #666666;">Avg Salary</div>
                        <div style="font-family: 'Syne', sans-serif; font-size: 2.5rem; font-weight: 700;"><fmt:formatNumber value="${stats.avgSalary}" type="number" maxFractionDigits="0"/></div>
                    </div>
                    <div class="card" style="padding: 1.5rem;">
                        <div style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.1em; color: #666666;">Monthly Bill</div>
                        <div style="font-family: 'Syne', sans-serif; font-size: 2.5rem; font-weight: 700;"><fmt:formatNumber value="${stats.totalSalaryBill}" type="number" maxFractionDigits="0"/></div>
                    </div>
                </div>
            </c:if>

            <div class="card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <div>
                        <h1 class="page-title">Employees</h1>
                        <p style="color: #666666; font-size: 0.9rem; margin-top: 0.25rem;">Total: <strong><c:out value="${totalCount}" /></strong></p>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=add" class="btn btn-primary">Add Employee</a>
                </div>

                <!-- Employees Table -->
                <div class="table-responsive">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <!-- Sorting Column Headers -->
                                <th>
                                    <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=name&sortOrder=${nextSortOrder}&page=${currentPage}">
                                        Name
                                        <c:if test="${sortBy == 'name'}">
                                            <c:choose>
                                                <c:when test="${sortOrder == 'ASC'}">&uarr;</c:when>
                                                <c:otherwise>&darr;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a>
                                </th>
                                <th>
                                    <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=department&sortOrder=${nextSortOrder}&page=${currentPage}">
                                        Department
                                        <c:if test="${sortBy == 'department'}">
                                            <c:choose>
                                                <c:when test="${sortOrder == 'ASC'}">&uarr;</c:when>
                                                <c:otherwise>&darr;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a>
                                </th>
                                <th>
                                    <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=salary&sortOrder=${nextSortOrder}&page=${currentPage}">
                                        Salary (Monthly)
                                        <c:if test="${sortBy == 'salary'}">
                                            <c:choose>
                                                <c:when test="${sortOrder == 'ASC'}">&uarr;</c:when>
                                                <c:otherwise>&darr;</c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </a>
                                </th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th style="text-align: right;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty employees}">
                                    <c:forEach var="emp" items="${employees}" varStatus="status">
                                        <tr>
                                            <td style="font-weight: 500;"><c:out value="${emp.name}" /></td>
                                            <td><c:out value="${emp.department}" /></td>
                                            <td style="font-weight: 500;">
                                                INR <fmt:formatNumber value="${emp.salary}" type="number" maxFractionDigits="2" minFractionDigits="2"/>
                                            </td>
                                            <td><c:out value="${emp.email}" /></td>
                                            <td><c:out value="${emp.phone}" /></td>
                                            <td style="text-align: right;">
                                                <div class="actions-cell" style="justify-content: flex-end;">
                                                    <a href="${pageContext.request.contextPath}/admin/dashboard?action=edit&id=${emp.id}" class="btn btn-secondary btn-action">
                                                        Edit
                                                    </a>
                                                    <!-- Secure form submission for Delete operation -->
                                                    <form action="${pageContext.request.contextPath}/admin/dashboard?action=delete&id=${emp.id}" method="POST" onsubmit="return confirm('Are you sure you want to delete ${emp.name} and their user portal?');" style="display:inline;">
                                                        <button type="submit" class="btn btn-primary btn-action" style="background-color: #000000; color: #ffffff;">
                                                            Delete
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" style="text-align: center; color: #666666; padding: 3rem;">
                                            No employee records found.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination Control Pills -->
                <div class="pagination-container">
                    <div class="pagination-info">
                        Showing page <strong><c:out value="${currentPage}" /></strong> of <strong><c:out value="${totalPages}" /></strong>
                    </div>
                    <div class="pagination-controls">
                        <!-- Previous button -->
                        <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage - 1}" 
                           class="pagination-link ${currentPage == 1 ? 'disabled' : ''}">
                            &laquo;
                        </a>

                        <!-- Page numbers -->
                        <c:forEach var="p" begin="1" end="${totalPages}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=${sortBy}&sortOrder=${sortOrder}&page=${p}" 
                               class="pagination-link ${currentPage == p ? 'active' : ''}">
                                <c:out value="${p}" />
                            </a>
                        </c:forEach>

                        <!-- Next button -->
                        <a href="${pageContext.request.contextPath}/admin/dashboard?sortBy=${sortBy}&sortOrder=${sortOrder}&page=${currentPage + 1}" 
                           class="pagination-link ${currentPage == totalPages ? 'disabled' : ''}">
                            &raquo;
                        </a>
                    </div>
                </div>

            </div>
        </main>
    </div>

</body>
</html>
