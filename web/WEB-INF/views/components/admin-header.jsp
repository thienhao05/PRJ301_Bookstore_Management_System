<%-- 
    Document   : admin-header
    Created on : Mar 12, 2026, 4:51:43 AM
    Author     : PC
--%>

<%-- 
    Document   : admin-header
    Created on : Mar 12, 2026, 4:51:43 AM
    Author     : PC (Hào)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm sticky-top">
    <div class="container-fluid px-4">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="MainController?action=dashboard">
            <i class="bi bi-book-half me-2 text-warning"></i>
            <span>BOOKSTORE <small class="fw-light text-muted" style="font-size: 0.7rem;">ADMIN</small></span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="adminNavbar">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-3">

                <li class="nav-item">
                    <a class="nav-link py-2" href="MainController?action=dashboard">
                        <i class="bi bi-speedometer2 me-1"></i> Dashboard
                    </a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle py-2" href="#" data-bs-toggle="dropdown">
                        <i class="bi bi-box-seam me-1"></i> Kho Hàng
                    </a>
                    <ul class="dropdown-menu shadow-sm border-0">
                        <li><a class="dropdown-item" href="MainController?action=manageBooks"><i class="bi bi-journal-text me-2"></i>Sách</a></li>
                        <li><a class="dropdown-item" href="MainController?action=manageCategories"><i class="bi bi-tags me-2"></i>Danh mục</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="MainController?action=managePublishers"><i class="bi bi-building me-2"></i>Nhà xuất bản</a></li>
                    </ul>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle py-2" href="#" data-bs-toggle="dropdown">
                        <i class="bi bi-cart-check me-1"></i> Kinh Doanh
                    </a>
                    <ul class="dropdown-menu shadow-sm border-0">
                        <li><a class="dropdown-item" href="MainController?action=manageOrders"><i class="bi bi-receipt me-2"></i>Đơn hàng</a></li>
                        <li><a class="dropdown-item" href="MainController?action=manageReviews"><i class="bi bi-star me-2"></i>Đánh giá (Reviews)</a></li>
                        <li><a class="dropdown-item" href="MainController?action=manageUsers"><i class="bi bi-people me-2"></i>Khách hàng</a></li>
                    </ul>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle py-2" href="#" data-bs-toggle="dropdown">
                        <i class="bi bi-person-badge me-1"></i> Nhân Sự
                    </a>
                    <ul class="dropdown-menu shadow-sm border-0">
                        <li><a class="dropdown-item" href="MainController?action=manageStaffs"><i class="bi bi-person-lines-fill me-2"></i>Nhân viên</a></li>
                        <li><a class="dropdown-item" href="MainController?action=manageShifts"><i class="bi bi-calendar-event me-2"></i>Ca trực</a></li>
                        <li><a class="dropdown-item" href="MainController?action=manageRoles"><i class="bi bi-shield-check me-2"></i>Phân quyền (Roles)</a></li>
                    </ul>
                </li>

                <li class="nav-item">
                    <a class="nav-link py-2" href="MainController?action=manageNews">
                        <i class="bi bi-newspaper me-1"></i> Tin tức
                    </a>
                </li>
            </ul>

            <div class="d-flex align-items-center">
                <div class="dropdown">
                    <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle px-3 py-1 bg-secondary bg-opacity-25 rounded-pill" data-bs-toggle="dropdown">
                        <i class="bi bi-person-circle fs-5 me-2"></i>
                        <span>${sessionScope.LOGIN_USER.username}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2">
                        <li><a class="dropdown-item" href="MainController?action=profile"><i class="bi bi-person me-2"></i>Hồ sơ cá nhân</a></li>
                        <li><a class="dropdown-item text-primary" href="MainController?action=sendNotification"><i class="bi bi-megaphone me-2"></i>Gửi thông báo mới</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="MainController?action=logout"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>