<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đơn hàng của tôi - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .order-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
            }
            .order-card:hover {
                box-shadow: 0 8px 25px rgba(0,0,0,0.1) !important;
            }
            .status-pill {
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 0.75rem;
                font-weight: 600;
                text-transform: uppercase;
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="row mb-4">
                <div class="col">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="MainController?action=home" class="text-decoration-none">Trang chủ</a>
                            </li>
                            <li class="breadcrumb-item active">Đơn hàng của tôi</li>
                        </ol>
                    </nav>
                    <h2 class="fw-bold">Lịch sử đặt hàng</h2>
                    <p class="text-muted small">Xem lại tất cả các đơn hàng bạn đã đặt tại hệ thống.</p>
                </div>
            </div>

            <%-- ✅ THÊM: Hiển thị thông báo --%>
            <%@include file="../components/message-alert.jsp" %>

            <div class="row">
                <div class="col-12">
                    <div class="card order-card shadow-sm">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="ps-4 py-3">Mã đơn</th>
                                            <th>Ngày đặt</th>
                                            <th>Tổng tiền</th>
                                            <th class="text-center">Trạng thái</th>
                                            <th class="text-end pe-4">Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="o" items="${ORDER_LIST}">
                                            <tr>
                                                <td class="ps-4 fw-bold text-primary">#${o.order_id}</td>
                                                <td>
                                                    <div class="small">
                                                        <i class="bi bi-clock me-1 text-muted"></i>
                                                        <%-- ✅ SỬA: Dùng fmt:formatDate với type="both" cho Timestamp --%>
                                                        <fmt:formatDate value="${o.order_date}"
                                                                        type="both"
                                                                        dateStyle="short"
                                                                        timeStyle="short"/>
                                                    </div>
                                                </td>
                                                <td class="fw-bold text-dark">
                                                    <fmt:formatNumber value="${o.total_amount}" pattern="#,###"/> đ
                                                </td>
                                                <td class="text-center">
                                                    <c:set var="st" value="${o.status}"/>
                                                    <span class="status-pill
                                                        ${st == 'Pending'    ? 'bg-warning-subtle text-warning border border-warning'   :
                                                          st == 'Processing' ? 'bg-info-subtle text-info border border-info'             :
                                                          st == 'Shipped'    ? 'bg-primary-subtle text-primary border border-primary'    :
                                                          st == 'Delivered'  ? 'bg-success-subtle text-success border border-success'    :
                                                                               'bg-danger-subtle text-danger border border-danger'}">
                                                        ${st}
                                                    </span>
                                                </td>
                                                <td class="text-end pe-4">
                                                    <a href="MainController?action=myOrderDetail&orderId=${o.order_id}"
                                                       class="btn btn-outline-dark btn-sm rounded-pill px-3">
                                                        Chi tiết
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty ORDER_LIST}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5">
                                                    <div class="py-4">
                                                        <i class="bi bi-bag-x text-muted display-1"></i>
                                                        <p class="mt-3 text-muted">Bạn chưa có đơn hàng nào.</p>
                                                        <a href="MainController?action=home"
                                                           class="btn btn-primary mt-2 rounded-pill px-4">
                                                            Mua sắm ngay
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- ✅ SỬA: Đường dẫn include đúng và đặt ra ngoài </div> --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
