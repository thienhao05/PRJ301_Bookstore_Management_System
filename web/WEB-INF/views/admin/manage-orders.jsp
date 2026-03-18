<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Đơn Hàng - Admin</title>
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
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .status-badge {
                padding: 5px 12px;
                border-radius: 20px;
                font-weight: 500;
                font-size: 0.85rem;
            }
        </style>
    </head>
    <body>

        <%@include file="../components/admin-sidebar.jsp" %>

        <div class="main-content">

            <%-- Header --%>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-0">Quản Lý Đơn Hàng</h2>
                    <p class="text-muted mb-0">
                        Theo dõi và cập nhật trạng thái đơn hàng của khách
                    </p>
                </div>
                <div class="text-muted small">
                    Tổng:
                    <strong class="text-primary">
                        ${empty ORDER_LIST ? 0 : ORDER_LIST.size()}
                    </strong>
                    đơn hàng
                </div>
            </div>

            <%@include file="../components/message-alert.jsp" %>

            <%-- Bộ lọc trạng thái --%>
            <div class="mb-4 d-flex flex-wrap gap-2">
                <a href="MainController?action=manageOrders"
                   class="btn btn-sm btn-primary rounded-pill px-3">
                    Tất cả
                </a>
                <a href="MainController?action=manageOrders&status=Pending"
                   class="btn btn-sm btn-outline-warning rounded-pill px-3">
                    Pending
                </a>
                <a href="MainController?action=manageOrders&status=Processing"
                   class="btn btn-sm btn-outline-info rounded-pill px-3">
                    Processing
                </a>
                <a href="MainController?action=manageOrders&status=Shipping"
                   class="btn btn-sm btn-outline-primary rounded-pill px-3">
                    Shipping
                </a>
                <a href="MainController?action=manageOrders&status=Delivered"
                   class="btn btn-sm btn-outline-success rounded-pill px-3">
                    Delivered
                </a>
                <a href="MainController?action=manageOrders&status=Canceled"
                   class="btn btn-sm btn-outline-danger rounded-pill px-3">
                    Canceled
                </a>
            </div>

            <%-- Bảng đơn hàng --%>
            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">Mã Đơn</th>
                                    <th>Khách Hàng</th>
                                    <th>Ngày Đặt</th>
                                    <th>Tổng Tiền</th>
                                    <th class="text-center">Trạng Thái</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${ORDER_LIST}">
                                    <tr>
                                        <td class="ps-4 fw-bold text-primary">
                                            #${o.order_id}
                                        </td>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <div class="bg-light rounded-circle
                                                            d-flex align-items-center
                                                            justify-content-center me-2"
                                                     style="width:34px;height:34px;">
                                                    <i class="bi bi-person text-muted"></i>
                                                </div>
                                                <span>User #${o.user_id}</span>
                                            </div>
                                        </td>
                                        <td class="text-muted small">
                                            <i class="bi bi-calendar3 me-1"></i>
                                            <fmt:formatDate value="${o.order_date}"
                                                            pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td class="fw-bold">
                                            <fmt:formatNumber value="${o.total_amount}"
                                                              pattern="#,###"/> đ
                                        </td>
                                        <td class="text-center">
                                            <c:set var="st" value="${o.status}"/>
                                            <span class="status-badge
                                                ${st == 'Pending'    ? 'bg-warning-subtle text-warning border border-warning' :
                                                  st == 'Processing' ? 'bg-info-subtle text-info border border-info' :
                                                  st == 'Shipping'   ? 'bg-primary-subtle text-primary border border-primary' :
                                                  st == 'Delivered'  ? 'bg-success-subtle text-success border border-success' :
                                                                       'bg-danger-subtle text-danger border border-danger'}">
                                                ${st}
                                            </span>
                                        </td>
                                        <td class="text-end pe-4">
                                            <a href="MainController?action=manageOrderDetail&id=${o.order_id}"
                                               class="btn btn-sm btn-primary shadow-sm rounded-pill px-3">
                                                <i class="bi bi-eye me-1"></i>Chi tiết
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty ORDER_LIST}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5 text-muted">
                                            <i class="bi bi-inbox display-4 opacity-25 d-block mb-3"></i>
                                            Chưa có đơn hàng nào trong hệ thống.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>