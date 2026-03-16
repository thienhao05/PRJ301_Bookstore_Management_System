<%-- 
    Document   : admin-sidebar
    Created on : Mar 12, 2026, 4:51:53 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    #sidebar-wrapper {
        min-height: 100vh;
        width: 250px;
        transition: all 0.3s;
        background: #212529; /* Màu tối chuyên nghiệp */
    }
    .sidebar-heading {
        padding: 1.5rem 1.25rem;
        font-size: 1.1rem;
        color: #fff;
        border-bottom: 1px solid #343a40;
    }
    .list-group-item {
        background: transparent !important;
        border: none !important;
        color: #adb5bd !important;
        padding: 0.8rem 1.5rem;
        transition: 0.2s;
    }
    .list-group-item:hover {
        color: #fff !important;
        background: #343a40 !important;
        padding-left: 1.8rem;
    }
    .list-group-item.active {
        color: #fff !important;
        background: #0d6efd !important;
        border-radius: 0 25px 25px 0;
        margin-right: 10px;
    }
    .menu-label {
        font-size: 0.75rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        color: #6c757d;
        padding: 1.5rem 1.5rem 0.5rem;
        font-weight: bold;
    }
</style>

<div id="sidebar-wrapper" class="border-end shadow">
    <div class="sidebar-heading fw-bold text-center">
        <i class="bi bi-cpu-fill text-primary me-2"></i>Admin Panel
    </div>

    <div class="list-group list-group-flush">
        <a href="MainController?action=dashboard" class="list-group-item list-group-item-action">
            <i class="bi bi-speedometer2 me-2"></i> Dashboard
        </a>

        <div class="menu-label">Kho hàng</div>
        <a href="MainController?action=manageBooks" class="list-group-item list-group-item-action">
            <i class="bi bi-book me-2"></i> Quản lý Sách
        </a>
        <a href="MainController?action=manageCategories" class="list-group-item list-group-item-action">
            <i class="bi bi-grid me-2"></i> Danh mục
        </a>
        <a href="MainController?action=managePublishers" class="list-group-item list-group-item-action">
            <i class="bi bi-building me-2"></i> Nhà xuất bản
        </a>

        <div class="menu-label">Kinh doanh</div>
        <a href="MainController?action=manageOrders" class="list-group-item list-group-item-action">
            <i class="bi bi-cart3 me-2"></i> Đơn hàng
        </a>
        <a href="MainController?action=manageReviews" class="list-group-item list-group-item-action">
            <i class="bi bi-chat-left-text me-2"></i> Đánh giá
        </a>
        <a href="MainController?action=manageUsers" class="list-group-item list-group-item-action">
            <i class="bi bi-people me-2"></i> Khách hàng
        </a>

        <div class="menu-label">Nhân sự</div>
        <a href="MainController?action=manageStaffs" class="list-group-item list-group-item-action">
            <i class="bi bi-person-badge me-2"></i> Nhân viên
        </a>
        <a href="MainController?action=manageShifts" class="list-group-item list-group-item-action">
            <i class="bi bi-calendar-range me-2"></i> Ca trực
        </a>
        <a href="MainController?action=manageRoles" class="list-group-item list-group-item-action">
            <i class="bi bi-shield-check me-2"></i> Vai trò (Roles)
        </a>

        <div class="menu-label">Nội dung</div>
        <a href="MainController?action=manageNews" class="list-group-item list-group-item-action">
            <i class="bi bi-megaphone me-2"></i> Tin tức
        </a>
        <a href="MainController?action=sendNotification" class="list-group-item list-group-item-action">
            <i class="bi bi-bell me-2"></i> Gửi thông báo
        </a>
    </div>
</div>