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
            body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
            .card { border: none; border-radius: 15px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
            .star-yellow { color: #ffc107; }
            .comment-text { font-style: italic; color: #495057; }
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
                <a href="MainController?action=dashboard" class="btn btn-outline-secondary shadow-sm">
                    <i class="bi bi-speedometer2 me-1"></i> Dashboard
                </a>
            </div>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
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
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="r" items="${REVIEW_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${r.review_id}</td>
                                        <td><strong>User #${r.user_id}</strong></td>
                                        <td><span class="badge bg-light text-dark border">Book #${r.book_id}</span></td>
                                        <td>
                                            <c:forEach begin="1" end="5" var="i">
                                                <c:choose>
                                                    <c:when test="${i <= r.rating}">
                                                        <i class="bi bi-star-fill star-yellow"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bi bi-star text-muted"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                            <span class="ms-1 text-muted small">(${r.rating}/5)</span>
                                        </td>
                                        <td>
                                            <div class="comment-text text-truncate-2" style="max-width: 250px;">
                                                "${r.comment}"
                                            </div>
                                        </td>
                                        <td class="text-end pe-4">
                                            <%-- ✅ Chỉ giữ nút Xóa, bỏ nút toggle status vì DB chưa có cột status --%>
                                            <a href="MainController?action=deleteReview&reviewId=${r.review_id}"
                                               class="btn btn-sm btn-outline-danger shadow-sm"
                                               onclick="return confirm('Bạn có chắc muốn xóa vĩnh viễn đánh giá này?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty REVIEW_LIST}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5 text-muted">
                                            <i class="bi bi-chat-square-text fs-1 d-block mb-2"></i>
                                            Chưa có đánh giá nào từ khách hàng.
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
