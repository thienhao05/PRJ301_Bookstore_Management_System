<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh Sách Yêu Thích - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body { background-color: #f4f7f6; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .order-card { border: none; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
            .btn-back { transition: 0.3s; }
            .btn-back:hover { transform: translateX(-5px); }
            .table thead { background-color: #f8f9fa; }
            .book-img-placeholder {
                width: 50px; height: 70px;
                background-color: #e9ecef;
                display: flex; align-items: center; justify-content: center;
                border-radius: 5px;
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Thêm header --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <a href="MainController?action=home"
                       class="btn btn-outline-secondary btn-sm btn-back mb-2">
                        <i class="bi bi-arrow-left"></i> Tiếp tục mua sắm
                    </a>
                    <h2 class="fw-bold text-dark">
                        Sách <span class="text-primary">Yêu Thích</span>
                    </h2>
                    <span class="text-muted">
                        <i class="bi bi-heart-fill text-danger me-1"></i>
                        Lưu giữ những cuốn sách bạn tâm đắc
                    </span>
                </div>
                <span class="badge bg-primary rounded-pill px-3 py-2 shadow-sm">
                    <%-- ✅ SỬA: Dùng fn:length hoặc size() --%>
                    <c:out value="${empty WISHLIST ? 0 : WISHLIST.size()}"/> Sản phẩm
                </span>
            </div>

            <%-- ✅ SỬA: Dùng include thay vì viết tay --%>
            <%@include file="../components/message-alert.jsp" %>

            <div class="row">
                <div class="col-12">
                    <div class="card order-card">
                        <div class="card-body p-4">
                            <div class="table-responsive">
                                <table class="table align-middle mb-0">
                                    <thead>
                                        <tr>
                                            <th class="border-0">Sản phẩm</th>
                                            <th class="border-0">Giá bán</th>
                                            <th class="border-0 text-center">Tình trạng</th>
                                            <th class="border-0 text-end pe-4">Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%-- ✅ SỬA: Dùng ${WISHLIST} cho khớp với controller --%>
                                        <c:forEach var="item" items="${WISHLIST}">
                                            <tr>
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <div class="book-img-placeholder me-3">
                                                            <i class="bi bi-book text-muted fs-4"></i>
                                                        </div>
                                                        <div>
                                                            <div class="fw-bold text-dark">${item.title}</div>
                                                            <div class="small text-muted">Tác giả: ${item.author}</div>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="fw-bold text-primary">
                                                        <fmt:formatNumber value="${item.price}" pattern="#,###"/> đ
                                                    </span>
                                                </td>
                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${item.stock > 0}">
                                                            <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-3">
                                                                Còn hàng
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill px-3">
                                                                Hết hàng
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-end pe-3">
                                                    <div class="btn-group shadow-sm">
                                                        <%-- Thêm vào giỏ hàng --%>
                                                        <a href="MainController?action=addToCart&bookId=${item.bookId}"
                                                           class="btn btn-sm btn-white text-primary border ${item.stock <= 0 ? 'disabled' : ''}"
                                                           title="Thêm vào giỏ hàng">
                                                            <i class="bi bi-cart-plus-fill"></i>
                                                        </a>
                                                        <%-- Xem chi tiết --%>
                                                        <a href="MainController?action=bookDetail&id=${item.bookId}"
                                                           class="btn btn-sm btn-white text-info border"
                                                           title="Xem chi tiết">
                                                            <i class="bi bi-eye-fill"></i>
                                                        </a>
                                                        <%-- ✅ SỬA: Dùng removeFromWishlist với param id=bookId --%>
                                                        <a href="MainController?action=removeFromWishlist&id=${item.bookId}"
                                                           class="btn btn-sm btn-white text-danger border"
                                                           onclick="return confirm('Xóa khỏi danh sách yêu thích?')"
                                                           title="Xóa">
                                                            <i class="bi bi-trash"></i>
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty WISHLIST}">
                                            <tr>
                                                <td colspan="4" class="text-center py-5">
                                                    <div class="text-muted mb-3">
                                                        <i class="bi bi-heartbreak display-1 opacity-25"></i>
                                                    </div>
                                                    <h5>Danh sách yêu thích đang trống</h5>
                                                    <p class="small text-muted">
                                                        Hãy dạo quanh cửa hàng và tìm cuốn sách bạn yêu nhé!
                                                    </p>
                                                    <a href="MainController?action=home"
                                                       class="btn btn-primary mt-2 shadow-sm rounded-pill px-4">
                                                        Khám phá ngay
                                                    </a>
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

        <%-- ✅ SỬA: Thêm footer --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
