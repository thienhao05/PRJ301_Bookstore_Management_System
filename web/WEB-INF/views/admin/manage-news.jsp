<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Tin Tức - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
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
                    <h2 class="fw-bold mb-0">Quản Lý Tin Tức</h2>
                    <p class="text-muted">Đăng bài viết mới hoặc cập nhật thông tin cửa hàng</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-primary shadow-sm" data-bs-toggle="modal" data-bs-target="#addNewsModal">
                        <i class="bi bi-pencil-square me-1"></i> Viết Bài Mới
                    </button>
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
                                    <th class="ps-4">ID</th>
                                    <th>Tiêu đề bài viết</th>
                                    <th>Nội dung tóm tắt</th>
                                    <th>Ngày đăng</th>
                                    <th>Tác giả (Staff ID)</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="n" items="${NEWS_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${n.news_id}</td>
                                        <td><strong class="text-dark">${n.title}</strong></td>
                                        <td>
                                            <span class="text-truncate-2 text-muted small" style="max-width: 300px;">
                                                ${n.content}
                                            </span>
                                        </td>
                                        <td>
                                            <div class="small">
                                                <i class="bi bi-clock me-1"></i>
                                                <fmt:formatDate value="${n.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="badge bg-light text-dark border">
                                                ID: ${n.staff_id}
                                            </span>
                                        </td>
                                        <td class="text-end pe-4">
                                            <a href="MainController?action=editNews&id=${n.news_id}" class="btn btn-sm btn-outline-info me-1">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="MainController?action=deleteNews&id=${n.news_id}" 
                                               class="btn btn-sm btn-outline-danger"
                                               onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty NEWS_LIST}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5 text-muted">
                                            Chưa có bài viết nào được đăng.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addNewsModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg border-0">
                <form action="MainController" method="POST" class="modal-content shadow-lg">
                    <input type="hidden" name="action" value="addNews">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Đăng Tin Tức Mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tiêu đề bài viết</label>
                            <input type="text" name="title" class="form-control" placeholder="Nhập tiêu đề hấp dẫn..." required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Nội dung chi tiết</label>
                            <textarea name="content" class="form-control" rows="10" placeholder="Viết nội dung bài viết tại đây..." required></textarea>
                            <div class="form-text text-muted">Mẹo: Bạn có thể sử dụng các thẻ HTML cơ bản để định dạng bài viết.</div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary px-4">Đăng Bài Ngay</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>