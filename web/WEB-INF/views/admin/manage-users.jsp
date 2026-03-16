<%-- 
    Document   : manage-users
    Created on : Mar 12, 2026, 4:44:22 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Người Dùng - Admin</title>
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
            .user-avatar {
                width: 40px;
                height: 40px;
                background-color: #6c757d;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
            }
            .role-badge {
                font-size: 0.75rem;
                padding: 4px 8px;
                border-radius: 6px;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <div class="container-fluid py-4 px-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold text-dark mb-0">Quản Lý Người Dùng</h2>
                    <p class="text-muted">Xem, thêm mới và kiểm soát quyền truy cập của thành viên</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2 shadow-sm">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-primary shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addUserModal">
                        <i class="bi bi-person-plus-fill me-1"></i> Thêm Thành Viên
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
                                    <th>Người dùng</th>
                                    <th>Email</th>
                                    <th>Vai trò</th>
                                    <th class="text-center">Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="u" items="${USER_LIST}">
                                <tr>
                                    <td class="ps-4 text-muted fw-bold">#${u.userId}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="user-avatar me-3 bg-primary bg-gradient">${u.username.substring(0,1).toUpperCase()}</div>
                                            <div class="fw-bold text-dark">${u.username}</div>
                                        </div>
                                    </td>
                                    <td>${u.email}</td>
                                    <td>
                                <c:choose>
                                    <c:when test="${u.roleId == 1}"><span class="role-badge bg-danger-subtle text-danger">ADMIN</span></c:when>
                                    <c:when test="${u.roleId == 2}"><span class="role-badge bg-warning-subtle text-warning">STAFF</span></c:when>
                                    <c:otherwise><span class="role-badge bg-info-subtle text-info">CUSTOMER</span></c:otherwise>
                                </c:choose>
                                </td>
                                <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.status}">
                                        <span class="badge rounded-pill bg-success-subtle text-success border border-success px-3">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge rounded-pill bg-secondary-subtle text-secondary border border-secondary px-3">Đã khóa</span>
                                    </c:otherwise>
                                </c:choose>
                                </td>
                                <td class="text-end pe-4">
                                    <div class="btn-group shadow-sm">
                                        <a href="MainController?action=editUser&id=${u.userId}" class="btn btn-sm btn-white text-info border">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>
                                        <a href="MainController?action=deleteUser&id=${u.userId}" 
                                           class="btn btn-sm btn-white text-danger border"
                                           onclick="return confirm('Xác nhận khóa tài khoản này?')">
                                            <i class="bi bi-person-x-fill"></i>
                                        </a>
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

        <div class="modal fade" id="addUserModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                    <input type="hidden" name="action" value="addUser">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Tạo Tài Khoản Mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tên đăng nhập (Username)</label>
                            <input type="text" name="username" class="form-control" placeholder="Ví dụ: hao_admin" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Địa chỉ Email</label>
                            <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mật khẩu</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <div class="mb-0">
                            <label class="form-label fw-bold">Phân quyền hệ thống</label>
                            <select name="roleId" class="form-select">
                                <option value="3">Khách hàng (Customer)</option>
                                <option value="2">Nhân viên (Staff)</option>
                                <option value="1">Quản trị viên (Admin)</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary px-4">Xác Nhận Tạo</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
