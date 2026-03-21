<%-- 
    Document   : book-detail
    Created on : Mar 12, 2026, 4:48:58 AM
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
        <title>${BOOK.title} - Hào's Bookstore</title>
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
            .book-cover-large {
                width: 100%;
                max-width: 400px;
                height: 500px;
                background-color: #fff;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .price-tag {
                font-size: 2rem;
                color: #0d6efd;
                font-weight: bold;
            }
            .stock-badge {
                font-size: 0.85rem;
                padding: 6px 12px;
                border-radius: 50px;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="mb-4">
                <a href="MainController?action=home" class="btn btn-outline-secondary btn-sm btn-back mb-3">
                    <i class="bi bi-arrow-left"></i> Quay lại cửa hàng
                </a>
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb small">
                        <li class="breadcrumb-item"><a href="MainController?action=home" class="text-decoration-none">Trang chủ</a></li>
                        <li class="breadcrumb-item active">${BOOK.title}</li>
                    </ol>
                </nav>
            </div>

            <div class="row g-5">
                <div class="col-lg-5 text-center">
                    <div class="book-cover-large mx-auto">
                        <div class="text-muted">
                            <i class="bi bi-book display-1 opacity-25"></i>
                            <p class="mt-2 fw-bold">IMAGE PLACEHOLDER</p>
                        </div>
                    </div>
                </div>

                <div class="col-lg-7">
                    <div class="card order-card h-100 p-4">
                        <div class="card-body">
                            <h1 class="fw-bold text-dark mb-2">${BOOK.title}</h1>
                            <p class="text-muted fs-5 mb-3">Tác giả: <span class="text-dark fw-medium">${BOOK.author}</span></p>

                            <div class="d-flex align-items-center mb-4">
                                <div class="text-warning me-3">
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-half"></i>
                                    <span class="text-muted small ms-2">(4.5/5 - 128 đánh giá)</span>
                                </div>
                                <span class="stock-badge ${BOOK.stock > 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
                                    <i class="bi ${BOOK.stock > 0 ? 'bi-check-circle' : 'bi-x-circle'} me-1"></i>
                                    ${BOOK.stock > 0 ? 'Còn hàng' : 'Hết hàng'}
                                </span>
                            </div>

                            <div class="price-tag mb-4">
                                <fmt:formatNumber value="${BOOK.price}" pattern="#,###"/> <small>đ</small>
                            </div>

                            <hr class="opacity-25 my-4">

                            <h6 class="fw-bold text-uppercase small text-muted mb-3">Mô tả tóm tắt</h6>
                            <p class="text-secondary lh-lg mb-4">
                                Đây là một trong những tác phẩm xuất sắc nhất thuộc thể loại này, mang lại cho người đọc 
                                những góc nhìn mới mẻ và kiến thức sâu sắc. Một cuốn sách không thể thiếu trong tủ sách của bạn.
                            </p>

                            <form action="MainController" method="POST" class="mt-auto">
                                <input type="hidden" name="action" value="addToCart">
                                <input type="hidden" name="bookId" value="${BOOK.bookId}">

                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <span class="input-group-text bg-white border-end-0">SL:</span>
                                            <input type="number" name="quantity" class="form-control border-start-0" value="1" min="1" max="${BOOK.stock}">
                                        </div>
                                    </div>
                                    <div class="col-md-8">
                                        <button type="submit" class="btn btn-primary btn-lg w-100 shadow-sm fw-bold ${BOOK.stock <= 0 ? 'disabled' : ''}">
                                            <i class="bi bi-cart-plus me-2"></i> Thêm vào giỏ hàng
                                        </button>
                                    </div>
                                    <div class="col-12">
                                        <a href="MainController?action=addToWishlist&id=${BOOK.bookId}" class="btn btn-outline-danger w-100 rounded-pill mt-2">
                                            <i class="bi bi-heart me-2"></i> Thêm vào danh sách yêu thích
                                        </a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-12">
                    <div class="card order-card shadow-sm border-0">
                        <div class="card-body p-4">
                            <ul class="nav nav-tabs border-0 mb-4" id="bookTab" role="tablist">
                                <li class="nav-item">
                                    <button class="nav-link active fw-bold border-0 bg-transparent text-primary" data-bs-toggle="tab" data-bs-target="#details">Chi tiết sản phẩm</button>
                                </li>
                                <li class="nav-item">
                                    <button class="nav-link fw-bold border-0 bg-transparent text-muted ms-4" data-bs-toggle="tab" data-bs-target="#reviews">Đánh giá khách hàng</button>
                                </li>
                            </ul>
                            <div class="tab-content" id="bookTabContent">
                                <div class="tab-pane fade show active" id="details">
                                    <table class="table table-sm table-borderless">
                                        <tr><td class="text-muted w-25">Mã sách:</td><td class="fw-medium">#${BOOK.bookId}</td></tr>
                                        <tr><td class="text-muted">Nhà xuất bản:</td><td class="fw-medium">NXB ID: ${BOOK.publisherId}</td></tr>
                                        <tr><td class="text-muted">Chuyên mục:</td><td class="fw-medium">CAT-${BOOK.categoryId}</td></tr>
                                        <tr><td class="text-muted">Số lượng tồn:</td><td class="fw-medium">${BOOK.stock} cuốn</td></tr>
                                    </table>
                                </div>
                                <div class="tab-pane fade" id="reviews">
                                    <p class="text-muted text-center py-4">Chưa có đánh giá nào cho sản phẩm này.</p>
                                </div>
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
