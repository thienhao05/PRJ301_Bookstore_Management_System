<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Bootstrap Icons nếu page chưa load --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

<style>
    #sidebar-wrapper {
        min-height: 100vh;
        width: 250px;
        transition: all 0.3s;
        background: #212529;
        position: fixed;   /* FIX: sidebar cố định không bị cuộn --%>
        top: 0;
        left: 0;
        z-index: 100;
        overflow-y: auto;  /* FIX: cuộn nội dung sidebar nếu quá dài */
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
        text-decoration: none;
        display: block;
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
    /* FIX: nút logout màu đỏ */
    .list-group-item.logout-item {
        color: #ff6b6b !important;
    }
    .list-group-item.logout-item:hover {
        color: #fff !important;
        background: #dc3545 !important;
    }
    .menu-label {
        font-size: 0.75rem;
        text-transform: uppercase;
        letter-spacing: 1px;
        color: #6c757d;
        padding: 1.5rem 1.5rem 0.5rem;
        font-weight: bold;
    }
    .role-badge {
        font-size: 0.7rem;
        padding: 3px 8px;
        border-radius: 10px;
    }
</style>

<div id="sidebar-wrapper" class="border-end shadow">

    <%-- Header sidebar --%>
    <div class="sidebar-heading fw-bold text-center">
        <i class="bi bi-cpu-fill text-primary me-2"></i>
        <c:choose>
            <c:when test="${sessionScope.LOGIN_USER.roleId == 1}">Admin Panel</c:when>
            <c:when test="${sessionScope.LOGIN_USER.roleId == 2}">Manager Panel</c:when>
            <c:otherwise>Staff Panel</c:otherwise>
        </c:choose>
    </div>

    <%-- Thông tin user --%>
    <div class="px-3 py-2 border-bottom border-secondary">
        <div class="d-flex align-items-center">
            <div class="bg-primary text-white rounded-circle d-flex align-items-center
                 justify-content-center me-2"
                 style="width:34px;height:34px;font-size:14px;flex-shrink:0;">
                ${sessionScope.LOGIN_USER.fullName.substring(0,1).toUpperCase()}
            </div>
            <div>
                <div class="text-white small fw-bold">
                    ${sessionScope.LOGIN_USER.fullName}
                </div>
                <div style="font-size:11px;">
                    <c:choose>
                        <c:when test="${sessionScope.LOGIN_USER.roleId == 1}">
                            <span class="badge bg-danger role-badge">Admin</span>
                        </c:when>
                        <c:when test="${sessionScope.LOGIN_USER.roleId == 2}">
                            <span class="badge bg-warning text-dark role-badge">Manager</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-info text-dark role-badge">Staff</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="list-group list-group-flush">

        <%-- Dashboard --%>
        <a href="MainController?action=dashboard"
           class="list-group-item list-group-item-action">
            <i class="bi bi-speedometer2 me-2"></i>Dashboard
        </a>

        <%-- KHO HÀNG --%>
        <div class="menu-label">Kho hàng</div>
        <a href="MainController?action=manageBooks"
           class="list-group-item list-group-item-action">
            <i class="bi bi-book me-2"></i>Quản lý Sách
        </a>
        <a href="MainController?action=manageCategories"
           class="list-group-item list-group-item-action">
            <i class="bi bi-grid me-2"></i>Danh mục
        </a>
        <a href="MainController?action=managePublishers"
           class="list-group-item list-group-item-action">
            <i class="bi bi-building me-2"></i>Nhà xuất bản
        </a>

        <%-- KINH DOANH --%>
        <div class="menu-label">Kinh doanh</div>
        <a href="MainController?action=manageOrders"
           class="list-group-item list-group-item-action">
            <i class="bi bi-cart3 me-2"></i>Đơn hàng
        </a>
        <a href="MainController?action=manageReviews"
           class="list-group-item list-group-item-action">
            <i class="bi bi-chat-left-text me-2"></i>Đánh giá
        </a>
        <a href="MainController?action=manageUsers"
           class="list-group-item list-group-item-action">
            <i class="bi bi-people me-2"></i>Khách hàng
        </a>

        <%-- NHÂN SỰ: Chỉ Admin(1) và Manager(2) --%>
        <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                      || sessionScope.LOGIN_USER.roleId == 2}">
              <div class="menu-label">Nhân sự</div>
              <a href="MainController?action=manageStaffs"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-person-badge me-2"></i>Nhân viên
              </a>
              <a href="MainController?action=manageShifts"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-calendar-range me-2"></i>Ca trực
              </a>
              <a href="MainController?action=manageRoles"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-shield-check me-2"></i>Vai trò (Roles)
              </a>
        </c:if>

        <%-- NỘI DUNG --%>
        <div class="menu-label">Nội dung</div>
        <a href="MainController?action=manageNews"
           class="list-group-item list-group-item-action">
            <i class="bi bi-megaphone me-2"></i>Tin tức
        </a>

        <%-- Chỉ Admin(1) và Manager(2) --%>
        <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                      || sessionScope.LOGIN_USER.roleId == 2}">
              <a href="MainController?action=sendNotification"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-bell me-2"></i>Gửi thông báo
              </a>
              <a href="MainController?action=manageDiscounts"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-percent me-2"></i>Mã giảm giá
              </a>
              <a href="MainController?action=manageShipping"
                 class="list-group-item list-group-item-action">
                  <i class="bi bi-truck me-2"></i>Vận chuyển
              </a>
        </c:if>

        <%-- TÀI KHOẢN --%>
        <div class="menu-label">Tài khoản</div>
        <a href="MainController?action=profile"
           class="list-group-item list-group-item-action">
            <i class="bi bi-person me-2"></i>Hồ sơ cá nhân
        </a>

        <%-- FIX: dùng class logout-item thay vì text-danger --%>
        <a href="MainController?action=logout"
           class="list-group-item list-group-item-action logout-item">
            <i class="bi bi-box-arrow-right me-2"></i>Đăng xuất
        </a>

    </div>
</div>