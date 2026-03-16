<%-- 
    Document   : checkout-success
    Created on : Mar 12, 2026, 4:49:45 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh Toán - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Segoe UI', sans-serif;
            }
            .order-card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .form-label {
                font-weight: 600;
                color: #495057;
            }
            .payment-method {
                cursor: pointer;
                border: 2px solid #eee;
                transition: 0.3s;
                border-radius: 10px;
            }
            .payment-method:hover {
                border-color: #0d6efd;
            }
            .form-check-input:checked + .payment-method {
                border-color: #0d6efd;
                background-color: #f0f7ff;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="mb-4">
                <a href="cart.jsp" class="btn btn-outline-secondary btn-sm mb-3">
                    <i class="bi bi-arrow-left"></i> Quay lại giỏ hàng
                </a>
                <h2 class="fw-bold text-dark">Xác Nhận <span class="text-primary">Đơn Hàng</span></h2>
                <p class="text-muted">Chỉ còn một bước nữa để sở hữu những cuốn sách yêu thích</p>
            </div>

            <form action="MainController" method="POST">
                <input type="hidden" name="action" value="placeOrder">

                <div class="row g-4">
                    <div class="col-lg-7">
                        <div class="card order-card p-4">
                            <h5 class="fw-bold mb-4"><i class="bi bi-geo-alt me-2 text-primary"></i>Thông tin nhận hàng</h5>

                            <div class="row g-3">
                                <div class="col-md-12">
                                    <label class="form-label">Họ và tên người nhận</label>
                                    <input type="text" name="receiverName" class="form-control" 
                                           value="${sessionScope.LOGIN_USER.fullName}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Số điện thoại</label>
                                    <input type="tel" name="phone" class="form-control" 
                                           value="${sessionScope.LOGIN_USER.phone}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control bg-light" 
                                           value="${sessionScope.LOGIN_USER.email}" readonly>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Địa chỉ giao hàng chi tiết</label>
                                    <textarea name="address" class="form-control" rows="3" 
                                              placeholder="Số nhà, tên đường, phường/xã, quận/huyện..." required></textarea>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Ghi chú (tùy chọn)</label>
                                    <input type="text" name="note" class="form-control" placeholder="Ví dụ: Giao giờ hành chính...">
                                </div>
                            </div>

                            <h5 class="fw-bold mt-5 mb-4"><i class="bi bi-credit-card me-2 text-primary"></i>Phương thức thanh toán</h5>
                            <div class="row g-2">
                                <div class="col-md-6">
                                    <input type="radio" name="payment" value="COD" class="btn-check" id="cod" checked>
                                    <label class="payment-method p-3 w-100 d-block" for="cod">
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-truck fs-3 me-3 text-primary"></i>
                                            <div>
                                                <div class="fw-bold">Thanh toán khi nhận hàng</div>
                                                <small class="text-muted">COD - Trả tiền mặt</small>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                                <div class="col-md-6">
                                    <input type="radio" name="payment" value="BANK" class="btn-check" id="bank">
                                    <label class="payment-method p-3 w-100 d-block" for="bank">
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-bank fs-3 me-3 text-secondary"></i>
                                            <div>
                                                <div class="fw-bold">Chuyển khoản ngân hàng</div>
                                                <small class="text-muted">Internet Banking / QR</small>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-5">
                        <div class="card order-card sticky-top" style="top: 100px;">
                            <div class="card-body p-4">
                                <h5 class="fw-bold mb-4">Tóm tắt đơn hàng</h5>

                                <div class="mb-3" style="max-height: 300px; overflow-y: auto;">
                                    <c:forEach var="item" items="${sessionScope.CART}">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <div class="d-flex align-items-center">
                                                <div class="bg-light p-2 rounded me-3" style="width: 40px; height: 50px; text-align: center;">
                                                    <i class="bi bi-book text-muted"></i>
                                                </div>
                                                <div style="max-width: 200px;">
                                                    <div class="fw-bold small text-truncate">${item.title}</div>
                                                    <small class="text-muted">Số lượng: ${item.quantity}</small>
                                                </div>
                                            </div>
                                            <div class="fw-bold small text-primary">
                                                <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/> đ
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <hr class="opacity-25">
                                <div class="d-flex justify-content-between mb-2 mt-4">
                                    <span class="text-muted">Tạm tính</span>
                                    <span class="fw-bold"><fmt:formatNumber value="${TOTAL_PRICE}" pattern="#,###"/> đ</span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">Phí giao hàng</span>
                                    <span class="text-success fw-bold">Miễn phí</span>
                                </div>
                                <div class="bg-primary bg-opacity-10 p-3 rounded-3 mb-4 mt-4 d-flex justify-content-between align-items-center">
                                    <span class="fw-bold text-primary">TỔNG CỘNG</span>
                                    <span class="h4 fw-bold text-danger mb-0">
                                        <fmt:formatNumber value="${TOTAL_PRICE}" pattern="#,###"/> đ
                                    </span>
                                </div>

                                <button type="submit" class="btn btn-primary btn-lg w-100 shadow-sm fw-bold py-3 mb-3">
                                    XÁC NHẬN ĐẶT HÀNG
                                </button>
                                <p class="text-center small text-muted mb-0">
                                    <i class="bi bi-shield-check me-1"></i> Thông tin của bạn được bảo mật tuyệt đối.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <%@include file="web-footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
