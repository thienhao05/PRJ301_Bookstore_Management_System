<%-- 
    Document   : manage-reviews
    Created on : Mar 12, 2026, 4:46:11 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Đánh Giá - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .star-yellow {
                color: #ffc107;
            }
            .comment-text {
                font-style: italic;
                color: #495057;
            }
            .text-truncate-2 {
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Quản Lý Đánh Giá</h2>
                    <p class="text-muted">Lắng nghe phản hồi từ độc giả để cải thiện dịch vụ</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary shadow-sm">
                        <i class="bi bi-speedometer2 me-1"></i> Dashboard
                    </a>
                </div>
            </div>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="MSG_SUCCESS" scope="session"/>
            </c:if>

            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Khách hàng</th>
                                    <th>Sách</th>
                                    <th>Đánh giá</th>
                                    <th>Nội dung</th>
                                    <th class="text-center">Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="r" items="${REVIEW_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${r.review_id}</td>
                                        <td><strong>User ID: ${r.user_id}</strong></td>
                                        <td><span class="badge bg-light text-dark border">Book ID: ${r.book_id}</span></td>
                                        <td>
                                            <c:forEach begin="1" end="5" var="i">
                                                <i class="bi ${i <= r.rating ? 'bi-star-fill star-yellow' : 'bi-star text-muted'}"></i>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <div class="comment-text text-truncate-2" style="max-width: 250px;">
                                                "${r.comment}"
                                            </div>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${r.status == 'Approved'}">
                                                    <span class="badge rounded-pill bg-success-subtle text-success border border-success px-3">Công khai</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge rounded-pill bg-secondary-subtle text-secondary border border-secondary px-3">Đã ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="btn-group shadow-sm">
                                                <a href="MainController?action=updateReviewStatus&id=${r.review_id}&status=${r.status == 'Approved' ? 'Hidden' : 'Approved'}" 
                                                   class="btn btn-sm btn-white border ${r.status == 'Approved' ? 'text-warning' : 'text-success'}"
                                                   title="${r.status == 'Approved' ? 'Ẩn đánh giá' : 'Hiện đánh giá'}">
                                                    <i class="bi ${r.status == 'Approved' ? 'bi-eye-slash' : 'bi-eye'}"></i>
                                                </a>
                                                <a href="MainController?action=deleteReview&id=${r.review_id}" 
                                                   class="btn btn-sm btn-white text-danger border"
                                                   onclick="return confirm('Bạn có chắc muốn xóa vĩnh viễn đánh giá này?')">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty REVIEW_LIST}">
                                    <tr>
                                        <td colspan="7" class="text-center py-5 text-muted">Chưa có đánh giá nào từ khách hàng.</td>
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