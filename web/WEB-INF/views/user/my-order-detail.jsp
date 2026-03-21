<%-- 
    Document   : my-order-detail
    Created on : Mar 12, 2026, 4:48:02 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn hàng #${ORDER.order_id} - Hào's Bookstore</title>
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
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .status-badge {
                padding: 8px 16px;
                border-radius: 50px;
                font-weight: 600;
                font-size: 0.8rem;
            }
            .product-img {
                width: 60px;
                height: 80px;
                object-fit: cover;
                border-radius: 5px;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="mb-4">
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb mb-2">
                        <li class="breadcrumb-item"><a href="MainController?action=home" class="text-decoration-none">Trang chủ</a></li>
                        <li class="breadcrumb-item"><a href="MainController?action=myOrders" class="text-decoration-none">Đơn hàng của tôi</a></li>
                        <li class="breadcrumb-item active">#${ORDER.order_id}</li>
                    </ol>
                </nav>
                <div class="d-flex justify-content-between align-items-center flex-wrap">
                    <h3 class="fw-bold mb-0">Đơn hàng #${ORDER.order_id}</h3>

                    <c:set var="st" value="${ORDER.status}"/>
                    <span class="status-badge mt-2 mt-md-0
                          ${st == 'Pending' ? 'bg-warning-subtle text-warning border border-warning' : 
                            st == 'Processing' ? 'bg-info-subtle text-info border border-info' : 
                            st == 'Shipping' ? 'bg-primary-subtle text-primary border border-primary' : 
                            st == 'Delivered' ? 'bg-success-subtle text-success border border-success' : 
                            'bg-danger-subtle text-danger border border-danger'}">
                              ${st == 'Pending' ? 'Đang chờ xác nhận' : 
                                st == 'Processing' ? 'Đang đóng gói' : 
                                st == 'Shipping' ? 'Đang giao hàng' : 
                                st == 'Delivered' ? 'Giao hàng thành công' : 'Đã hủy'}
                          </span>
                    </div>
                    <p class="text-muted small mt-2">Đặt lúc: <fmt:formatDate value="${ORDER.order_date}" pattern="dd/MM/yyyy HH:mm"/></p>
                </div>

                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="card order-card mb-4">
                            <div class="card-body p-4">
                                <h5 class="fw-bold mb-4">Sản phẩm đã chọn</h5>
                                <div class="table-responsive">
                                    <table class="table align-middle">
                                        <thead>
                                            <tr class="text-muted small text-uppercase">
                                                <th>Sách</th>
                                                <th class="text-center">Số lượng</th>
                                                <th class="text-end">Thành tiền</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="item" items="${DETAILS}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-light p-2 rounded me-3">
                                                            <i class="bi bi-book fs-4 text-secondary"></i>
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-dark">${item.book_title}</div>
                                                            <div class="small text-muted"><fmt:formatNumber value="${item.price}" pattern="#,###"/> đ</div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="text-center">x${item.quantity}</td>
                                                <td class="text-end fw-bold">
                                            <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/> đ
                                            </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4">
                        <div class="card order-card mb-4">
                            <div class="card-body p-4">
                                <h6 class="fw-bold mb-3"><i class="bi bi-geo-alt me-2 text-primary"></i>Địa chỉ nhận hàng</h6>
                                <div class="small">
                                    <p class="mb-1 fw-bold">Người nhận: ${ORDER.user_id} (ID)</p>
                                    <p class="text-muted mb-0">Địa chỉ: [Địa chỉ được lưu trong hệ thống]</p>
                                </div>
                            </div>
                        </div>

                        <div class="card order-card bg-white">
                            <div class="card-body p-4">
                                <h6 class="fw-bold mb-4">Tóm tắt đơn hàng</h6>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Tạm tính</span>
                                    <span><fmt:formatNumber value="${ORDER.total_amount}" pattern="#,###"/> đ</span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Phí vận chuyển</span>
                                    <span class="text-success">Miễn phí</span>
                                </div>
                                <hr class="my-3 opacity-25">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fw-bold">Tổng cộng</span>
                                    <span class="fs-4 fw-bold text-danger">
                                        <fmt:formatNumber value="${ORDER.total_amount}" pattern="#,###"/> đ
                                    </span>
                                </div>

                                <div class="mt-4">
                                    <a href="MainController?action=contact" class="btn btn-outline-dark w-100 rounded-pill btn-sm">
                                        <i class="bi bi-chat-dots me-2"></i>Liên hệ hỗ trợ
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%@include file="web-footer.jsp" %>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>
    </html>
