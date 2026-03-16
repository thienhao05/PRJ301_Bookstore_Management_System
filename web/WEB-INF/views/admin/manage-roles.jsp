<%-- 
    Document   : manage-roles
    Created on : Mar 12, 2026, 4:44:57 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Vai Trò - Admin</title>
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
            .role-badge {
                padding: 8px 16px;
                border-radius: 50px;
                font-weight: 600;
                text-transform: uppercase;
                font-size: 0.75rem;
            }
            /* Màu sắc riêng cho từng Role phổ biến */
            .bg-admin {
                background-color: #ffe5e5;
                color: #d9534f;
                border: 1px solid #d9534f;
            }
            .bg-staff {
                background-color: #e5f1ff;
                color: #0275d8;
                border: 1px solid #0275d8;
            }
            .bg-user {
                background-color: #f0f3f5;
                color: #5bc0de;
                border: 1px solid #5bc0de;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Phân Quyền Hệ Thống</h2>
                    <p class="text-muted">Quản lý các nhóm quyền truy cập vào tài nguyên website</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2 shadow-sm">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-dark shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addRoleModal">
                        <i class="bi bi-shield-lock me-1"></i> Thêm Vai Trò
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
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Tên Vai Trò</th>
                                    <th>Mô tả quyền hạn</th>
                                    <th class="text-center">Số lượng User</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="role" items="${ROLE_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted fw-bold">#${role.id}</td>
                                        <td>
                                            <span class="role-badge
                                                  ${role.name == 'Admin' ? 'bg-admin' : 
                                                    role.name == 'Staff' ? 'bg-staff' : 'bg-user'}">
                                                      ${role.name}
                                                  </span>
                                            </td>
                                            <td>
                                                <small class="text-muted">${role.description}</small>
                                            </td>
                                            <td class="text-center">
                                                <span class="fw-bold text-dark">--</span> 
                                            </td>
                                            <td class="text-end pe-4">
                                                <div class="btn-group">
                                                    <button class="btn btn-sm btn-outline-primary border-0">
                                                        <i class="bi bi-pencil"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-outline-danger border-0" 
                                                            onclick="return confirm('Cảnh báo: Xóa vai trò có thể khiến người dùng không thể đăng nhập. Bạn chắc chứ?')">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="addRoleModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                        <input type="hidden" name="action" value="addRole">
                        <div class="modal-header bg-dark text-white">
                            <h5 class="modal-title fw-bold">Thiết Lập Vai Trò Mới</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body p-4">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Tên vai trò (Ví dụ: Manager, Shipper...)</label>
                                <input type="text" name="name" class="form-control" required>
                            </div>
                            <div class="mb-0">
                                <label class="form-label fw-bold">Mô tả chi tiết quyền hạn</label>
                                <textarea name="description" class="form-control" rows="3" placeholder="Được quyền xem đơn hàng, không được xóa sách..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer bg-light">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-dark px-4">Lưu Vai Trò</button>
                        </div>
                    </form>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>
    </html>
