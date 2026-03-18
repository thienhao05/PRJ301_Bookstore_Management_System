<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-style.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
                display: flex;
            }
            .main-content {
                margin-left: 250px;
                padding: 30px;
                width: 100%;
                min-height: 100vh;
            }
            .stat-card {
                border: none;
                border-radius: 15px;
                padding: 25px;
                color: white;
                transition: 0.3s;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            .stat-card:hover { transform: translateY(-5px); }
            .stat-card .icon {
                font-size: 2.5rem;
                opacity: 0.3;
            }
            .stat-card .number {
                font-size: 2rem;
                font-weight: 700;
            }
            .stat-card .label {
                font-size: 0.9rem;
                opacity: 0.85;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            }
            .quick-link {
                display: flex;
                align-items: center;
                padding: 12px 15px;
                border-radius: 10px;
                text-decoration: none;
                color: #495057;
                transition: 0.2s;
                border: 1px solid #e9ecef;
                margin-bottom: 10px;
            }
            .quick-link:hover {
                background: #f0f7ff;
                border-color: #0d6efd;
                color: #0d6efd;
                transform: translateX(5px);
            }
            .status-badge {
                padding: 5px 12px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 500;
            }
        </style>
    </head>
    <body>

        <%-- SIDEBAR --%>
        <%@include file="../components/admin-sidebar.jsp" %>

        <div class="main-content">

            <%-- Header --%>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-0">Bảng Điều Khiển Tổng Quan</h2>
                    <p class="text-muted mb-0">
                        Xin chào,
                        <strong>${sessionScope.LOGIN_USER.fullName}</strong>!
                        Chào mừng bạn trở lại.
                    </p>
                </div>
                <div class="text-end">
                    <div class="text-muted small">
                        <i class="bi bi-calendar3 me-1"></i>
                        Hôm nay: <fmt:formatDate value="<%= new java.util.Date() %>"
                                                 pattern="dd/MM/yyyy"/>
                    </div>
                    <c:choose>
                        <c:when test="${sessionScope.LOGIN_USER.roleId == 1}">
                            <span class="badge bg-danger">Admin</span>
                        </c:when>
                        <c:when test="${sessionScope.LOGIN_USER.roleId == 2}">
                            <span class="badge bg-warning text-dark">Manager</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-info text-dark">Staff</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <%@include file="../components/message-alert.jsp" %>

            <%-- STAT CARDS --%>
            <div class="row g-4 mb-4">
                <div class="col-md-6 col-lg-3">
                    <div class="stat-card bg-primary">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="label">Doanh Thu</div>
                                <div class="number">
                                    <fmt:formatNumber value="${TOTAL_REVENUE != null ? TOTAL_REVENUE : 0}"
                                                      pattern="#,###"/> đ
                                </div>
                            </div>
                            <i class="bi bi-cash-stack icon"></i>
                        </div>
                        <div class="mt-2 small opacity-75">
                            <i class="bi bi-arrow-up me-1"></i>Từ đơn đã giao
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3">
                    <div class="stat-card bg-success">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="label">Đơn Hàng</div>
                                <div class="number">
                                    ${TOTAL_ORDERS != null ? TOTAL_ORDERS : 0}
                                </div>
                            </div>
                            <i class="bi bi-bag-check icon"></i>
                        </div>
                        <div class="mt-2 small opacity-75">
                            <i class="bi bi-clock me-1"></i>Tổng tất cả đơn
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3">
                    <div class="stat-card bg-warning">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="label">Khách Hàng</div>
                                <div class="number">
                                    ${TOTAL_USERS != null ? TOTAL_USERS : 0}
                                </div>
                            </div>
                            <i class="bi bi-people icon"></i>
                        </div>
                        <div class="mt-2 small opacity-75">
                            <i class="bi bi-person-plus me-1"></i>Tài khoản đã đăng ký
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-3">
                    <div class="stat-card bg-info">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <div class="label">Sách Trong Kho</div>
                                <div class="number">
                                    ${TOTAL_BOOKS != null ? TOTAL_BOOKS : 0}
                                </div>
                            </div>
                            <i class="bi bi-book icon"></i>
                        </div>
                        <div class="mt-2 small opacity-75">
                            <i class="bi bi-box me-1"></i>Tổng số lượng tồn kho
                        </div>
                    </div>
                </div>
            </div>

            <%-- NỘI DUNG CHÍNH --%>
            <div class="row g-4">

                <%-- Đơn hàng mới nhất --%>
                <div class="col-lg-8">
                    <div class="card h-100">
                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between
                                        align-items-center mb-3">
                                <h5 class="fw-bold mb-0">
                                    <i class="bi bi-receipt me-2 text-primary"></i>
                                    Đơn Hàng Mới Nhất
                                </h5>
                                <a href="MainController?action=manageOrders"
                                   class="btn btn-sm btn-outline-primary rounded-pill px-3">
                                    Xem tất cả
                                </a>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Mã Đơn</th>
                                            <th>Khách Hàng</th>
                                            <th>Ngày</th>
                                            <th>Tổng Tiền</th>
                                            <th>Trạng Thái</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${RECENT_ORDERS}">
                                            <tr>
                                                <td class="fw-bold text-primary">
                                                    #${order.order_id}
                                                </td>
                                                <td>
                                                    <i class="bi bi-person me-1 text-muted"></i>
                                                    User #${order.user_id}
                                                </td>
                                                <td class="text-muted small">
                                                    <fmt:formatDate value="${order.order_date}"
                                                                    pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td class="fw-bold">
                                                    <fmt:formatNumber
                                                        value="${order.total_amount}"
                                                        pattern="#,###"/> đ
                                                </td>
                                                <td>
                                                    <c:set var="st" value="${order.status}"/>
                                                    <span class="status-badge
                                                        ${st == 'Pending'    ? 'bg-warning-subtle text-warning border border-warning' :
                                                          st == 'Processing' ? 'bg-info-subtle text-info border border-info' :
                                                          st == 'Shipping'   ? 'bg-primary-subtle text-primary border border-primary' :
                                                          st == 'Delivered'  ? 'bg-success-subtle text-success border border-success' :
                                                                               'bg-danger-subtle text-danger border border-danger'}">
                                                        ${st}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="MainController?action=manageOrderDetail&id=${order.order_id}"
                                                       class="btn btn-sm btn-outline-secondary rounded-pill px-2">
                                                        <i class="bi bi-eye"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty RECENT_ORDERS}">
                                            <tr>
                                                <td colspan="6" class="text-center py-4 text-muted">
                                                    Chưa có đơn hàng nào
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- Cột phải --%>
                <div class="col-lg-4">

                    <%-- Truy cập nhanh --%>
                    <div class="card mb-4">
                        <div class="card-body p-4">
                            <h5 class="fw-bold mb-3">
                                <i class="bi bi-lightning me-2 text-warning"></i>
                                Truy cập nhanh
                            </h5>

                            <a href="MainController?action=manageNews"
                               class="quick-link">
                                <i class="bi bi-pencil-square me-3 text-primary"></i>
                                Đăng tin tức mới
                            </a>

                            <a href="MainController?action=manageBooks"
                               class="quick-link">
                                <i class="bi bi-plus-circle me-3 text-success"></i>
                                Nhập thêm sách
                            </a>

                            <a href="MainController?action=manageOrders"
                               class="quick-link">
                                <i class="bi bi-bag-check me-3 text-info"></i>
                                Xem đơn hàng
                            </a>

                            <%-- Chỉ Admin và Manager --%>
                            <c:if test="${sessionScope.LOGIN_USER.roleId == 1
                                       || sessionScope.LOGIN_USER.roleId == 2}">
                                <a href="MainController?action=manageDiscounts"
                                   class="quick-link">
                                    <i class="bi bi-percent me-3 text-danger"></i>
                                    Tạo mã giảm giá
                                </a>
                                <a href="MainController?action=sendNotification"
                                   class="quick-link">
                                    <i class="bi bi-bell me-3 text-warning"></i>
                                    Gửi thông báo
                                </a>
                            </c:if>
                        </div>
                    </div>

                    <%-- Thống kê nhanh theo trạng thái đơn hàng --%>
                    <div class="card">
                        <div class="card-body p-4">
                            <h5 class="fw-bold mb-3">
                                <i class="bi bi-pie-chart me-2 text-info"></i>
                                Trạng thái đơn hàng
                            </h5>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="small">
                                    <span class="badge bg-warning-subtle text-warning
                                                 border border-warning me-1">Pending</span>
                                </span>
                                <a href="MainController?action=manageOrders"
                                   class="small text-muted text-decoration-none">
                                    Xem chi tiết →
                                </a>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="small">
                                    <span class="badge bg-info-subtle text-info
                                                 border border-info me-1">Processing</span>
                                </span>
                                <a href="MainController?action=manageOrders"
                                   class="small text-muted text-decoration-none">
                                    Xem chi tiết →
                                </a>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="small">
                                    <span class="badge bg-primary-subtle text-primary
                                                 border border-primary me-1">Shipping</span>
                                </span>
                                <a href="MainController?action=manageOrders"
                                   class="small text-muted text-decoration-none">
                                    Xem chi tiết →
                                </a>
                            </div>
                            <div class="d-flex justify-content-between">
                                <span class="small">
                                    <span class="badge bg-success-subtle text-success
                                                 border border-success me-1">Delivered</span>
                                </span>
                                <a href="MainController?action=manageOrders"
                                   class="small text-muted text-decoration-none">
                                    Xem chi tiết →
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>