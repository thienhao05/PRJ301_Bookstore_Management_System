<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:if test="${not empty sessionScope.MSG_SUCCESS}">
    <div class="alert alert-success text-center mb-0 rounded-0">
        ${sessionScope.MSG_SUCCESS}
    </div>
    <c:remove var="MSG_SUCCESS" scope="session"/>
</c:if>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary d-flex align-items-center"
           href="MainController?action=home">
            <i class="bi bi-book-half me-2"></i>
            <span>BOOKSTORE</span>
        </a>

        <button class="navbar-toggler border-0" type="button"
                data-bs-toggle="collapse" data-bs-target="#webNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="webNavbar">

            <form class="d-flex mx-auto mt-3 mt-lg-0 w-100"
                  style="max-width: 500px;"
                  action="MainController" method="GET">
                <input type="hidden" name="action" value="search">
                <div class="input-group">
                    <input class="form-control border-end-0 rounded-start-pill px-4"
                           type="search" name="query"
                           placeholder="Tìm tên sách, tác giả...">
                    <button class="btn btn-outline-primary border-start-0 rounded-end-pill px-3"
                            type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=home">Trang Chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=view">Sách</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium"
                       href="MainController?action=viewNews">Tin Tức</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link px-3 position-relative"
                       href="MainController?action=viewCart">
                        <i class="bi bi-cart3 fs-5"></i>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.LOGIN_USER}">
                        <li class="nav-item dropdown ms-lg-2">
                            <%-- QUAN TRỌNG: phải có data-bs-toggle="dropdown" --%>
                            <a class="nav-link dropdown-toggle fw-bold text-dark d-flex align-items-center"
                               href="#"
                               id="userDropdown"
                               role="button"
                               data-bs-toggle="dropdown"
                               aria-expanded="false">
                                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2"
                                     style="width:32px;height:32px;font-size:14px;flex-shrink:0;">
                                    ${sessionScope.LOGIN_USER.fullName.substring(0,1).toUpperCase()}
                                </div>
                                <span>${sessionScope.LOGIN_USER.fullName}</span>
                            </a>

                            <%-- DROPDOWN MENU --%>
                            <ul class="dropdown-menu dropdown-menu-end border-0 shadow p-2"
                                aria-labelledby="userDropdown"
                                style="min-width: 200px;">

                                <%-- Header dropdown --%>
                                <li class="px-3 py-2 border-bottom mb-1">
                                    <div class="fw-bold text-dark small">
                                        ${sessionScope.LOGIN_USER.fullName}
                                    </div>
                                    <div class="text-muted" style="font-size:12px;">
                                        ${sessionScope.LOGIN_USER.email}
                                    </div>
                                </li>

                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=profile">
                                        <i class="bi bi-person me-2 text-primary"></i>Hồ sơ cá nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=myOrders">
                                        <i class="bi bi-bag-check me-2 text-success"></i>Đơn hàng của tôi
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=viewWishlist">
                                        <i class="bi bi-heart me-2 text-danger"></i>Yêu thích
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=viewNotifications">
                                        <i class="bi bi-bell me-2 text-warning"></i>Thông báo
                                    </a>
                                </li>

                                <%-- Nút quản trị nếu là Admin/Staff --%>
                                <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                                              || sessionScope.LOGIN_USER.roleId == 2}">
                                      <li><hr class="dropdown-divider"></li>
                                      <li>
                                          <a class="dropdown-item fw-bold rounded py-2"
                                             href="MainController?action=dashboard"
                                             style="color: #dc3545;">
                                              <i class="bi bi-shield-lock me-2"></i>Trang quản trị
                                          </a>
                                      </li>
                                </c:if>

                                <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                                              || sessionScope.LOGIN_USER.roleId == 2
                                              || sessionScope.LOGIN_USER.roleId == 3}">
                                      <li><hr class="dropdown-divider"></li>
                                      <li>
                                          <a class="dropdown-item fw-bold rounded py-2"
                                             href="MainController?action=dashboard"
                                             style="color: #dc3545;">
                                              <i class="bi bi-shield-lock me-2"></i>Trang quản trị
                                          </a>
                                      </li>
                                </c:if>

                                <li><hr class="dropdown-divider"></li>

                                <%-- LOGOUT --%>
                                <li>
                                    <a class="dropdown-item rounded py-2"
                                       href="MainController?action=logout"
                                       style="color: #6c757d;">
                                        <i class="bi bi-box-arrow-right me-2 text-danger"></i>
                                        Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
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

<%-- BOOTSTRAP JS - đặt ở đây để dropdown hoạt động --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>