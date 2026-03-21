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
            .btn-check:checked + .payment-method {
                border-color: #0d6efd;
                background-color: #f0f7ff;
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="mb-4">
                <a href="MainController?action=viewCart" class="btn btn-outline-secondary btn-sm mb-3">
                    <i class="bi bi-arrow-left"></i> Quay lại giỏ hàng
                </a>
                <h2 class="fw-bold text-dark">Thanh Toán & <span class="text-primary">Đặt Hàng</span></h2>
                <p class="text-muted">Vui lòng kiểm tra kỹ thông tin giao hàng trước khi xác nhận.</p>
            </div>

            <%-- ✅ SỬA: Đường dẫn include đúng --%>
            <%@include file="../components/message-alert.jsp" %>

            <form action="MainController" method="POST">
                <%-- ✅ SỬA: action đổi thành placeOrder --%>
                <input type="hidden" name="action" value="placeOrder">

                <%-- ✅ THÊM: Truyền totalAmount từ session --%>
                <input type="hidden" name="totalAmount" value="${sessionScope.TOTAL_PRICE}">

                <div class="row g-4">
                    <%-- CỘT TRÁI: Thông tin giao hàng --%>
                    <div class="col-lg-7">
                        <div class="card order-card p-4 h-100">
                            <h5 class="fw-bold mb-4 text-uppercase small">
                                <i class="bi bi-geo-alt-fill me-2 text-primary"></i>Địa chỉ nhận hàng
                            </h5>

                            <div class="row g-3">
                                <div class="col-md-12">
                                    <label class="form-label">Họ và tên người nhận</label>
                                    <input type="text" name="receiverName" class="form-control"
                                           value="${sessionScope.LOGIN_USER.fullName}" required
                                           placeholder="Nhập họ và tên...">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Số điện thoại</label>
                                    <input type="tel" name="phone" class="form-control"
                                           value="${sessionScope.LOGIN_USER.phone}" required
                                           placeholder="Nhập số điện thoại...">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control bg-light"
                                           value="${sessionScope.LOGIN_USER.email}" readonly>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Địa chỉ chi tiết</label>
                                    <textarea name="address" class="form-control" rows="3"
                                              placeholder="Số nhà, tên đường, Phường/Xã, Quận/Huyện, Tỉnh/Thành phố..."
                                              required></textarea>
                                </div>
                            </div>

                            <h5 class="fw-bold mt-5 mb-4 text-uppercase small">
                                <i class="bi bi-wallet2 me-2 text-primary"></i>Phương thức thanh toán
                            </h5>
                            <div class="row g-2">
                                <div class="col-md-6">
                                    <input type="radio" name="paymentMethod" value="COD"
                                           class="btn-check" id="method_cod" checked>
                                    <label class="payment-method p-3 w-100 d-block" for="method_cod">
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-cash-stack fs-3 me-3 text-success"></i>
                                            <div>
                                                <div class="fw-bold">Tiền mặt (COD)</div>
                                                <small class="text-muted">Thanh toán khi nhận hàng</small>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                                <div class="col-md-6">
                                    <input type="radio" name="paymentMethod" value="BANK"
                                           class="btn-check" id="method_bank">
                                    <label class="payment-method p-3 w-100 d-block" for="method_bank">
                                        <div class="d-flex align-items-center">
                                            <i class="bi bi-bank fs-3 me-3 text-primary"></i>
                                            <div>
                                                <div class="fw-bold">Chuyển khoản</div>
                                                <small class="text-muted">Internet Banking / QR</small>
                                            </div>
                                        </div>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%-- CỘT PHẢI: Tóm tắt đơn hàng --%>
                    <div class="col-lg-5">
                        <div class="card order-card sticky-top shadow-sm" style="top: 100px;">
                            <div class="card-body p-4">
                                <h5 class="fw-bold mb-4 text-uppercase small">Tóm tắt đơn hàng</h5>

                                <div class="mb-4" style="max-height: 250px; overflow-y: auto;">
                                    <c:forEach var="item" items="${sessionScope.CART}">
                                        <c:forEach var="book" items="${sessionScope.CART_BOOKS}">
                                            <c:if test="${book.bookId == item.book_id}">
                                                <div class="d-flex justify-content-between align-items-center mb-3">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-light rounded p-2 me-3"
                                                             style="width: 40px; height: 50px; text-align: center;">
                                                            <i class="bi bi-book text-muted"></i>
                                                        </div>
                                                        <div style="max-width: 180px;">
                                                            <div class="fw-bold small text-truncate">${book.title}</div>
                                                            <small class="text-muted">Số lượng: ${item.quantity}</small>
                                                        </div>
                                                    </div>
                                                    <div class="fw-bold text-primary small text-end">
                                                        <fmt:formatNumber value="${book.price * item.quantity}"
                                                                          pattern="#,###"/> đ
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </div>

                                <hr class="opacity-25">

                                <div class="d-flex justify-content-between mb-2 mt-4">
                                    <span class="text-muted">Tạm tính</span>
                                    <span class="fw-bold">
                                        <fmt:formatNumber value="${sessionScope.TOTAL_PRICE}"
                                                          pattern="#,###"/> đ
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="text-muted">Phí vận chuyển</span>
                                    <span class="text-success fw-bold small text-uppercase">Miễn phí</span>
                                </div>

                                <div class="bg-primary bg-opacity-10 p-3 rounded-3 mb-4
                                            d-flex justify-content-between align-items-center mt-4">
                                    <span class="fw-bold text-primary">TỔNG CỘNG</span>
                                    <span class="h4 fw-bold text-danger mb-0">
                                        <fmt:formatNumber value="${sessionScope.TOTAL_PRICE}"
                                                          pattern="#,###"/> đ
                                    </span>
                                </div>

                                <c:choose>
                                    <c:when test="${not empty sessionScope.CART}">
                                        <button type="submit"
                                                class="btn btn-primary btn-lg w-100 shadow-sm fw-bold py-3 rounded-pill">
                                            <i class="bi bi-check-circle me-2"></i>XÁC NHẬN ĐẶT HÀNG
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-secondary btn-lg w-100 disabled py-3 fw-bold rounded-pill">
                                            GIỎ HÀNG TRỐNG
                                        </button>
                                    </c:otherwise>
                                </c:choose>

                                <p class="text-center small text-muted mt-3 mb-0">
                                    <i class="bi bi-shield-check me-1 text-success"></i>
                                    Thanh toán an toàn & bảo mật
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
