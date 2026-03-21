<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đặt Hàng Thành Công - Hào's Bookstore</title>
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
            .success-icon {
                width: 90px;
                height: 90px;
                background: linear-gradient(135deg, #28a745, #20c997);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
                animation: popIn 0.5s ease;
            }
            @keyframes popIn {
                0% { transform: scale(0); opacity: 0; }
                80% { transform: scale(1.1); }
                100% { transform: scale(1); opacity: 1; }
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">

            <%-- ✅ THÊM: Hiển thị thông báo thành công/lỗi --%>
            <%@include file="../components/message-alert.jsp" %>

            <div class="row justify-content-center">
                <div class="col-lg-7">

                    <%-- Banner đặt hàng thành công --%>
                    <div class="card order-card p-5 text-center mb-4">
                        <div class="success-icon">
                            <i class="bi bi-check-lg text-white fs-1"></i>
                        </div>
                        <h3 class="fw-bold text-success mb-2">Đặt Hàng Thành Công!</h3>
                        <p class="text-muted mb-1">Cảm ơn bạn đã mua sắm tại Hào's Bookstore.</p>
                        <p class="text-muted">
                            Mã đơn hàng của bạn: 
                            <strong class="text-primary">#${ORDER_ID}</strong>
                        </p>
                    </div>

                    <%-- Tóm tắt đơn hàng --%>
                    <div class="card order-card p-4 mb-4">
                        <h5 class="fw-bold mb-4">
                            <i class="bi bi-receipt me-2 text-primary"></i>Tóm Tắt Đơn Hàng
                        </h5>

                        <%-- ✅ SỬA: Dùng CART_BOOKS kết hợp CART để lấy title + price --%>
                        <c:forEach var="item" items="${sessionScope.CART}">
                            <c:forEach var="book" items="${sessionScope.CART_BOOKS}">
                                <c:if test="${book.bookId == item.book_id}">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <div class="d-flex align-items-center">
                                            <div class="bg-light p-2 rounded me-3"
                                                 style="width: 40px; height: 50px; text-align: center;">
                                                <i class="bi bi-book text-muted"></i>
                                            </div>
                                            <div style="max-width: 250px;">
                                                <div class="fw-bold small text-truncate">${book.title}</div>
                                                <small class="text-muted">Số lượng: ${item.quantity}</small>
                                            </div>
                                        </div>
                                        <div class="fw-bold small text-primary">
                                            <fmt:formatNumber value="${book.price * item.quantity}"
                                                              pattern="#,###"/> đ
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:forEach>

                        <hr class="opacity-25">

                        <div class="d-flex justify-content-between mb-2 mt-3">
                            <span class="text-muted">Phí giao hàng</span>
                            <span class="text-success fw-bold">Miễn phí</span>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span class="h6 fw-bold mb-0">TỔNG CỘNG</span>
                            <span class="h4 fw-bold text-danger mb-0">
                                <%-- ✅ SỬA: Dùng sessionScope.TOTAL_PRICE --%>
                                <fmt:formatNumber value="${sessionScope.TOTAL_PRICE}"
                                                  pattern="#,###"/> đ
                            </span>
                        </div>
                    </div>

                    <%-- Thông tin giao hàng --%>
                    <div class="card order-card p-4 mb-4">
                        <h5 class="fw-bold mb-3">
                            <i class="bi bi-geo-alt me-2 text-primary"></i>Địa Chỉ Giao Hàng
                        </h5>
                        <p class="mb-1 fw-bold">${sessionScope.LOGIN_USER.fullName}</p>
                        <p class="mb-1 text-muted">${sessionScope.LOGIN_USER.phone}</p>
                        <p class="mb-0 text-muted">${sessionScope.LOGIN_USER.email}</p>
                    </div>

                    <%-- Nút điều hướng --%>
                    <div class="d-flex gap-3">
                        <a href="MainController?action=myOrders"
                           class="btn btn-primary btn-lg flex-fill fw-bold py-3 rounded-pill">
                            <i class="bi bi-bag-check me-2"></i>Xem Đơn Hàng Của Tôi
                        </a>
                        <a href="MainController?action=view"
                           class="btn btn-outline-secondary btn-lg flex-fill py-3 rounded-pill">
                            <i class="bi bi-shop me-2"></i>Tiếp Tục Mua Sắm
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
