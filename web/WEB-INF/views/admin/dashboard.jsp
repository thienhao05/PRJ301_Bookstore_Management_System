<%-- 
    Document   : dashboard
    Created on : Mar 12, 2026, 4:43:29 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - BookStore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            :root {
                --sidebar-width: 250px;
            }
            body {
                background-color: #f8f9fa;
            }
            .sidebar {
                width: var(--sidebar-width);
                height: 100vh;
                position: fixed;
                background: #212529;
                color: white;
                padding-top: 20px;
            }
            .main-content {
                margin-left: var(--sidebar-width);
                padding: 20px;
            }
            .nav-link {
                color: #adb5bd;
                transition: 0.3s;
            }
            .nav-link:hover, .nav-link.active {
                color: white;
                background: #343a40;
            }
            .stat-card {
                border: none;
                border-radius: 15px;
                transition: transform 0.3s;
            }
            .stat-card:hover {
                transform: translateY(-5px);
            }
        </style>
    </head>
    <body>

        <div class="sidebar d-flex flex-column p-3">
            <h4 class="text-center mb-4"><i class="bi bi-book-half"></i> Admin Panel</h4>
            <ul class="nav nav-pills flex-column mb-auto">
                <li class="nav-item">
                    <a href="MainController?action=dashboard" class="nav-link active"><i class="bi bi-speedometer2 me-2"></i> Dashboard</a>
                </li>
                <li><a href="MainController?action=manageBooks" class="nav-link"><i class="bi bi-journal-text me-2"></i> Quản lý Sách</a></li>
                <li><a href="MainController?action=manageCategories" class="nav-link"><i class="bi bi-tags me-2"></i> Chuyên mục</a></li>
                <li><a href="MainController?action=manageOrders" class="nav-link"><i class="bi bi-cart3 me-2"></i> Đơn hàng</a></li>
                <li><a href="MainController?action=manageDiscounts" class="nav-link"><i class="bi bi-percent me-2"></i> Khuyến mãi</a></li>
                <li><a href="MainController?action=manageNews" class="nav-link"><i class="bi bi-newspaper me-2"></i> Tin tức</a></li>
            </ul>
            <hr>
            <div class="dropdown">
                <a href="MainController?action=logout" class="btn btn-danger w-100"><i class="bi bi-box-arrow-left"></i> Đăng xuất</a>
            </div>
        </div>

        <div class="main-content">
            <header class="d-flex justify-content-between align-items-center mb-4">
                <h2>Bảng Điều Khiển Tổng Quan</h2>
                <div class="user-info">Chào, <strong>${sessionScope.LOGIN_USER.fullName}</strong></div>
            </header>

            <div class="row g-4 mb-4">
                <div class="col-md-3">
                    <div class="card stat-card bg-primary text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div><h5>Doanh Thu</h5><h3><fmt:formatNumber value="${TOTAL_REVENUE}" type="currency" currencySymbol="đ"/></h3></div>
                            <i class="bi bi-cash-stack fs-1 opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card stat-card bg-success text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div><h5>Đơn Hàng</h5><h3>${TOTAL_ORDERS}</h3></div>
                            <i class="bi bi-bag-check fs-1 opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card stat-card bg-warning text-dark p-3">
                        <div class="d-flex justify-content-between">
                            <div><h5>Khách Hàng</h5><h3>${TOTAL_USERS}</h3></div>
                            <i class="bi bi-people fs-1 opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card stat-card bg-info text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div><h5>Sách Trong Kho</h5><h3>${TOTAL_BOOKS}</h3></div>
                            <i class="bi bi-book fs-1 opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-8">
                    <div class="card shadow-sm border-none p-4 mb-4">
                        <h5>Đơn Hàng Mới Nhất</h5>
                        <table class="table table-hover mt-3">
                            <thead>
                                <tr>
                                    <th>Mã Đơn</th>
                                    <th>Khách Hàng</th>
                                    <th>Ngày</th>
                                    <th>Tổng Tiền</th>
                                    <th>Trạng Thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${RECENT_ORDERS}">
                                    <tr>
                                        <td>#${order.order_id}</td>
                                        <td>User ID: ${order.user_id}</td>
                                        <td><fmt:formatDate value="${order.order_date}" pattern="dd/MM/yyyy"/></td>
                                        <td class="fw-bold"><fmt:formatNumber value="${order.total_amount}" type="currency" currencySymbol="đ"/></td>
                                        <td><span class="badge bg-primary">${order.status}</span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm border-none p-4">
                        <h5>Truy cập nhanh</h5>
                        <div class="list-group list-group-flush mt-3">
                            <a href="MainController?action=manageNews" class="list-group-item list-group-item-action border-0"><i class="bi bi-plus-circle me-2"></i> Đăng tin tức mới</a>
                            <a href="MainController?action=manageBooks" class="list-group-item list-group-item-action border-0"><i class="bi bi-plus-circle me-2"></i> Nhập thêm sách</a>
                            <a href="MainController?action=manageDiscounts" class="list-group-item list-group-item-action border-0"><i class="bi bi-plus-circle me-2"></i> Tạo mã giảm giá</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
