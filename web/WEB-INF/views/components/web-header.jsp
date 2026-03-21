<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:if test="${not empty sessionScope.MSG_SUCCESS}">
    <div class="alert alert-success text-center mb-0 rounded-0 py-2">
        <i class="bi bi-check-circle-fill me-2"></i>${sessionScope.MSG_SUCCESS}
    </div>
    <c:remove var="MSG_SUCCESS" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.MSG_ERROR}">
    <div class="alert alert-danger text-center mb-0 rounded-0 py-2">
        <i class="bi bi-exclamation-circle-fill me-2"></i>${sessionScope.MSG_ERROR}
    </div>
    <c:remove var="MSG_ERROR" scope="session"/>
</c:if>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
    <div class="container">

        <%-- Logo --%>
        <a class="navbar-brand fw-bold text-primary d-flex align-items-center"
           href="MainController?action=home">
            <i class="bi bi-book-half me-2"></i>
            <span>BOOKSTORE</span>
        </a>

        <%-- Toggle mobile --%>
        <button class="navbar-toggler border-0" type="button"
                data-bs-toggle="collapse" data-bs-target="#webNavbar"
                aria-controls="webNavbar" aria-expanded="false">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="webNavbar">

            <%-- Thanh tìm kiếm --%>
            <form class="d-flex mx-auto mt-3 mt-lg-0 w-100"
                  style="max-width: 500px;"
                  action="MainController" method="GET">
                <input type="hidden" name="action" value="search">
                <div class="input-group">
                    <input class="form-control border-end-0 rounded-start-pill px-4"
                           type="search"
                           name="query"
                           placeholder="Tìm tên sách, tác giả...">
                    <button class="btn btn-outline-primary border-start-0 rounded-end-pill px-3"
                            type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <%-- Menu --%>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">

                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=home">
                        <i class="bi bi-house me-1"></i>Trang Chủ
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=view">
                        <i class="bi bi-journal-text me-1"></i>Sách
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=viewNews">
                        <i class="bi bi-newspaper me-1"></i>Tin Tức
                    </a>
                </li>

                <%-- Icon giỏ hàng --%>
                <li class="nav-item">
                    <a class="nav-link px-3 position-relative"
                       href="MainController?action=viewCart"
                       title="Giỏ hàng">
                        <i class="bi bi-cart3 fs-5"></i>
                        <%-- Badge số lượng nếu có --%>
                        <c:if test="${not empty sessionScope.CART
                                   && sessionScope.TOTAL_ITEMS > 0}">
                            <span class="position-absolute top-0 start-100 translate-middle
                                         badge rounded-pill bg-danger"
                                  style="font-size:0.6rem; margin-left:-8px; margin-top:8px;">
                                ${sessionScope.TOTAL_ITEMS}
                            </span>
                        </c:if>
                    </a>
                </li>

                <%-- User menu --%>
                <c:choose>
                    <c:when test="${not empty sessionScope.LOGIN_USER}">
                        <li class="nav-item dropdown ms-lg-2">
                            <a class="nav-link dropdown-toggle fw-bold text-dark
                                      d-flex align-items-center"
                               href="#"
                               id="userDropdown"
                               role="button"
                               data-bs-toggle="dropdown"
                               aria-expanded="false">
                                <div class="bg-primary text-white rounded-circle
                                            d-flex align-items-center justify-content-center me-2"
                                     style="width:34px;height:34px;
                                            font-size:14px;flex-shrink:0;">
                                    ${sessionScope.LOGIN_USER.fullName.substring(0,1).toUpperCase()}
                                </div>
                                <span class="d-none d-xl-inline">
                                    ${sessionScope.LOGIN_USER.fullName}
                                </span>
                            </a>

                            <ul class="dropdown-menu dropdown-menu-end border-0 shadow p-2"
                                aria-labelledby="userDropdown"
                                style="min-width:220px;">

                                <%-- Header: tên + email --%>
                                <li class="px-3 py-2 border-bottom mb-1">
                                    <div class="fw-bold text-dark small">
                                        ${sessionScope.LOGIN_USER.fullName}
                                    </div>
                                    <div class="text-muted" style="font-size:12px;">
                                        ${sessionScope.LOGIN_USER.email}
                                    </div>
                                </li>

                                <%-- Menu items --%>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=profile">
                                        <i class="bi bi-person me-2 text-primary"></i>
                                        Hồ sơ cá nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=myOrders">
                                        <i class="bi bi-bag-check me-2 text-success"></i>
                                        Đơn hàng của tôi
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=viewCart">
                                        <i class="bi bi-cart3 me-2 text-warning"></i>
                                        Giỏ hàng
                                        <c:if test="${sessionScope.TOTAL_ITEMS > 0}">
                                            <span class="badge bg-danger rounded-pill ms-1">
                                                ${sessionScope.TOTAL_ITEMS}
                                            </span>
                                        </c:if>
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=viewWishlist">
                                        <i class="bi bi-heart me-2 text-danger"></i>
                                        Yêu thích
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=viewNotifications">
                                        <i class="bi bi-bell me-2 text-warning"></i>
                                        Thông báo
                                    </a>
                                </li>

                                <%-- Nút quản trị: Admin(1), Manager(2), Staff(3) --%>
                                <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                                          || sessionScope.LOGIN_USER.roleId == 2
                                          || sessionScope.LOGIN_USER.roleId == 3}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item fw-bold rounded py-2"
                                           href="MainController?action=dashboard"
                                           style="color:#dc3545;">
                                            <i class="bi bi-shield-lock me-2"></i>
                                            Trang quản trị
                                        </a>
                                    </li>
                                </c:if>

                                <%-- Logout --%>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=logout">
                                        <i class="bi bi-box-arrow-right me-2 text-danger"></i>
                                        <span class="text-danger">Đăng xuất</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <%-- Chưa đăng nhập --%>
                        <li class="nav-item ms-lg-2">
                            <a class="btn btn-primary px-4 rounded-pill shadow-sm fw-bold"
                               href="MainController?action=login">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng Nhập
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>

            </ul>
        </div>
    </div>
</nav>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>