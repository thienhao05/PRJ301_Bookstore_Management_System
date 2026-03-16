<%-- 
    Document   : web-header
    Created on : Mar 12, 2026, 4:52:12 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary d-flex align-items-center" href="MainController?action=home">
            <i class="bi bi-book-half me-2"></i>
            <span class="tracking-tight">BOOKSTORE</span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#webNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="webNavbar">
            <form class="d-flex mx-auto mt-3 mt-lg-0 w-100" style="max-width: 500px;" action="MainController" method="GET">
                <input type="hidden" name="action" value="search">
                <div class="input-group">
                    <input class="form-control border-end-0 rounded-start-pill px-4" 
                           type="search" name="query" 
                           placeholder="Tìm tên sách, tác giả..." aria-label="Search">
                    <button class="btn btn-outline-primary border-start-0 rounded-end-pill px-3" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                </div>
            </form>

            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <li class="nav-item">
                    <a class="nav-link px-3 fw-medium" href="MainController?action=home">Trang Chủ</a>
                </li>

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle px-3 fw-medium" href="#" id="catDropdown" role="button" data-bs-toggle="dropdown">
                        Danh Mục
                    </a>
                    <ul class="dropdown-menu border-0 shadow-sm p-2" aria-labelledby="catDropdown">
                        <li><a class="dropdown-item rounded" href="MainController?action=category&id=1">Văn Học</a></li>
                        <li><a class="dropdown-item rounded" href="MainController?action=category&id=2">Kinh Tế</a></li>
                        <li><a class="dropdown-item rounded" href="MainController?action=category&id=3">Kỹ Thuật</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-primary rounded" href="MainController?action=allBooks">Xem tất cả</a></li>
                    </ul>
                </li>

                <li class="nav-item">
                    <a class="nav-link px-3 position-relative" href="MainController?action=viewCart">
                        <i class="bi bi-cart3 fs-5"></i>
                        <c:if test="${not empty sessionScope.CART}">
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" 
                                  style="margin-left: -10px; margin-top: 10px; font-size: 0.6rem;">
                                ${sessionScope.CART.size()}
                            </span>
                        </c:if>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.LOGIN_USER}">
                        <li class="nav-item dropdown ms-lg-3">
                            <a class="nav-link dropdown-toggle fw-bold text-dark d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                <div class="avatar-sm bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 30px; height: 30px;">
                                    ${sessionScope.LOGIN_USER.fullName.substring(0,1).toUpperCase()}
                                </div>
                                <span class="d-none d-xl-inline">${sessionScope.LOGIN_USER.fullName}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end border-0 shadow-sm p-2" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item rounded" href="MainController?action=viewProfile"><i class="bi bi-person me-2"></i>Hồ sơ</a></li>
                                <li><a class="dropdown-item rounded" href="MainController?action=myOrders"><i class="bi bi-bag-check me-2"></i>Đơn hàng</a></li>

                                <%-- Nếu là Admin thì hiện nút quay lại Dashboard --%>
                                <c:if test="${sessionScope.LOGIN_USER.roleId == 1}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger fw-bold rounded" href="MainController?action=dashboard"><i class="bi bi-shield-lock me-2"></i>Quản trị</a></li>
                                </c:if>

                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-muted rounded" href="MainController?action=logout"><i class="bi bi-box-arrow-right me-2"></i>Đăng xuất</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item ms-lg-3">
                            <a class="btn btn-primary px-4 rounded-pill shadow-sm fw-bold" href="login.jsp">Đăng Nhập</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
