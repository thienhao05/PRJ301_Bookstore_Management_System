<%-- 
    Document   : manage-categories
    Created on : Mar 12, 2026, 4:43:51 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Chuyên Mục - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card {
                border: none;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            }
            .status-active {
                color: #198754;
                font-weight: 600;
            }
            .status-hidden {
                color: #dc3545;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Danh Mục Chuyên Mục</h2>
                    <p class="text-muted">Phân loại sách để khách hàng dễ tìm kiếm</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                        <i class="bi bi-plus-lg"></i> Thêm Chuyên Mục
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
                                    <th>Tên Chuyên Mục</th>
                                    <th>Mô tả</th>
                                    <th class="text-center">Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cat" items="${CATEGORY_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${cat.id}</td>
                                        <td><strong class="text-dark">${cat.name}</strong></td>
                                        <td>
                                            <span class="text-truncate d-inline-block" style="max-width: 300px;">
                                                ${cat.description}
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${cat.status == 1}">
                                                    <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">Đang hiển thị</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill">Đã ẩn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end pe-4">
                                            <a href="MainController?action=editCategory&id=${cat.id}" class="btn btn-sm btn-outline-info me-1">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="MainController?action=deleteCategory&id=${cat.id}" 
                                               class="btn btn-sm btn-outline-danger"
                                               onclick="return confirm('Xóa chuyên mục này có thể ảnh hưởng đến sách liên quan. Bạn chắc chứ?')">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addCategoryModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="MainController" method="POST" class="modal-content">
                    <input type="hidden" name="action" value="addCategory">
                    <div class="modal-header">
                        <h5 class="modal-title fw-bold">Thêm Chuyên Mục Mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tên chuyên mục</label>
                            <input type="text" name="name" class="form-control" placeholder="Ví dụ: Công nghệ thông tin" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mô tả ngắn</label>
                            <textarea name="description" class="form-control" rows="3" placeholder="Sách về lập trình, phần cứng..."></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Trạng thái</label>
                            <select name="status" class="form-select">
                                <option value="1">Hiển thị ngay</option>
                                <option value="0">Tạm ẩn</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary px-4">Lưu lại</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
