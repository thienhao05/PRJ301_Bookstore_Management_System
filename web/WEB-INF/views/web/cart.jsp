<%-- 
    Document   : cart
    Created on : Mar 12, 2026, 4:49:20 AM
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
        <title>Giỏ Hàng Của Bạn - Hào's Bookstore</title>
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
            .btn-back {
                transition: 0.3s;
            }
            .btn-back:hover {
                transform: translateX(-5px);
            }
            .table thead {
                background-color: #f8f9fa;
            }
            .cart-img-placeholder {
                width: 60px;
                height: 80px;
                background-color: #e9ecef;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 8px;
            }
            .quantity-input {
                width: 70px;
                text-align: center;
                border-radius: 8px !important;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="mb-4">
                <a href="MainController?action=home" class="btn btn-outline-secondary btn-sm btn-back mb-3">
                    <i class="bi bi-arrow-left"></i> Tiếp tục mua sắm
                </a>
                <h2 class="fw-bold text-dark">Giỏ Hàng <span class="text-primary">Trực Tuyến</span></h2>
                <p class="text-muted"><i class="bi bi-cart-check-fill me-1 text-success"></i> Kiểm tra lại các sản phẩm trước khi thanh toán</p>
            </div>

            <%@include file="message-alert.jsp" %>

            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="card order-card">
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="ps-4 py-3">Sản phẩm</th>
                                            <th class="text-center">Số lượng</th>
                                            <th class="text-end">Đơn giá</th>
                                            <th class="text-end pe-4">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%-- Giả sử biến CART trong Session chứa danh sách các Item --%>
                                    <c:forEach var="item" items="${sessionScope.CART}">
                                        <tr>
                                            <td class="ps-4">
                                                <div class="d-flex align-items-center py-2">
                                                    <div class="cart-img-placeholder me-3">
                                                        <i class="bi bi-book text-muted fs-4"></i>
                                                    </div>
                                                    <div>
                                                        <div class="fw-bold text-dark">${item.title}</div>
                                                        <a href="MainController?action=removeFromCart&id=${item.bookId}" 
                                                           class="text-danger small text-decoration-none"
                                                           onclick="return confirm('Xóa cuốn sách này khỏi giỏ hàng?')">
                                                            <i class="bi bi-trash3 me-1"></i> Xóa
                                                        </a>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                <form action="MainController" method="POST" class="d-flex justify-content-center">
                                                    <input type="hidden" name="action" value="updateCartQuantity">
                                                    <input type="hidden" name="bookId" value="${item.bookId}">
                                                    <input type="number" name="quantity" class="form-control form-control-sm quantity-input border-primary" 
                                                           value="${item.quantity}" min="1" onchange="this.form.submit()">
                                                </form>
                                            </td>
                                            <td class="text-end text-muted">
                                        <fmt:formatNumber value="${item.price}" pattern="#,###"/> đ
                                        </td>
                                        <td class="text-end fw-bold text-primary pe-4">
                                        <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/> đ
                                        </td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty sessionScope.CART}">
                                        <tr>
                                            <td colspan="4" class="text-center py-5">
                                                <div class="py-4">
                                                    <i class="bi bi-cart-x display-1 text-muted opacity-25"></i>
                                                    <h5 class="mt-3 text-muted">Giỏ hàng của bạn đang trống!</h5>
                                                    <a href="MainController?action=home" class="btn btn-primary mt-3 shadow-sm px-4">Mua sắm ngay</a>
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

                <div class="col-lg-4">
                    <div class="card order-card sticky-top" style="top: 100px;">
                        <div class="card-body p-4">
                            <h5 class="fw-bold mb-4">Hóa Đơn Tạm Tính</h5>

                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Tổng số lượng</span>
                                <span class="fw-medium">${TOTAL_ITEMS != null ? TOTAL_ITEMS : 0} cuốn</span>
                            </div>
                            <div class="d-flex justify-content-between mb-3">
                                <span class="text-muted">Phí giao hàng</span>
                                <span class="text-success fw-medium">Miễn phí</span>
                            </div>
                            <hr class="opacity-25">
                            <div class="d-flex justify-content-between align-items-center mb-4 mt-4">
                                <span class="h6 fw-bold mb-0">TỔNG CỘNG</span>
                                <span class="h4 fw-bold text-danger mb-0">
                                    <fmt:formatNumber value="${TOTAL_PRICE != null ? TOTAL_PRICE : 0}" pattern="#,###"/> đ
                                </span>
                            </div>

                            <c:choose>
                                <c:when test="${not empty sessionScope.CART}">
                                    <a href="MainController?action=checkout" class="btn btn-primary btn-lg w-100 shadow-sm fw-bold py-3">
                                        Tiến Hành Thanh Toán <i class="bi bi-arrow-right ms-2"></i>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-secondary btn-lg w-100 disabled py-3 fw-bold">
                                        Giỏ Hàng Trống
                                    </button>
                                </c:otherwise>
                            </c:choose>

                            <div class="mt-4 text-center">
                                <img src="https://cdn-icons-png.flaticon.com/512/349/349221.png" width="40" class="mx-1 opacity-75" alt="Visa">
                                <img src="https://cdn-icons-png.flaticon.com/512/349/349228.png" width="40" class="mx-1 opacity-75" alt="Mastercard">
                                <img src="https://cdn-icons-png.flaticon.com/512/349/349230.png" width="40" class="mx-1 opacity-75" alt="Paypal">
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
