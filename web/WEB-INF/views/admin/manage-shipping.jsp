<%-- 
    Document   : manage-shipping
    Created on : Mar 12, 2026, 4:45:34 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Nhân Viên - Admin</title>
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
            .staff-avatar {
                width: 40px;
                height: 40px;
                background-color: #e9ecef;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                color: #6c757d;
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
                    <h2 class="fw-bold text-dark mb-0">Đội Ngũ Nhân Viên</h2>
                    <p class="text-muted">Quản lý tài khoản, phân quyền và lịch trực nhân sự</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2 shadow-sm">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-primary shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addStaffModal">
                        <i class="bi bi-person-plus-fill me-1"></i> Thêm Nhân Viên
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
                                    <th>Nhân viên</th>
                                    <th>Thông tin liên lạc</th>
                                    <th>Vai trò</th>
                                    <th>Ca trực</th>
                                    <th class="text-center">Trạng thái</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="s" items="${STAFF_LIST}">
                                <tr>
                                    <td class="ps-4 text-muted fw-bold">#${s.userId}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="staff-avatar me-3">${s.fullName.substring(0,1).toUpperCase()}</div>
                                            <div>
                                                <div class="fw-bold text-dark">${s.fullName}</div>
                                                <small class="text-muted">@${s.username}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="small"><i class="bi bi-envelope me-1"></i> ${s.email}</div>
                                        <div class="small"><i class="bi bi-telephone me-1"></i> ${s.phone}</div>
                                    </td>
                                    <td>
                                        <span class="role-badge ${s.roleId == 1 ? 'bg-danger-subtle text-danger' : 'bg-primary-subtle text-primary'}">
                                            ${s.roleId == 1 ? 'ADMIN' : 'STAFF'}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            <i class="bi bi-clock me-1"></i> Ca ${s.shiftId}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <span class="badge rounded-pill ${s.status == 'Active' ? 'bg-success' : 'bg-secondary'}">
                                            ${s.status}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <div class="btn-group">
                                            <a href="MainController?action=editStaff&id=${s.userId}" class="btn btn-sm btn-outline-info">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="MainController?action=deleteStaff&id=${s.userId}" 
                                               class="btn btn-sm btn-outline-danger"
                                               onclick="return confirm('Bạn có chắc muốn đình chỉ tài khoản nhân viên này?')">
                                                <i class="bi bi-person-x"></i>
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

        <div class="modal fade" id="addStaffModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg border-0">
                <form action="MainController" method="POST" class="modal-content shadow-lg">
                    <input type="hidden" name="action" value="addStaff">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Đăng Ký Tài Khoản Nhân Viên</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Họ và Tên</label>
                                <input type="text" name="fullName" class="form-control" placeholder="Nguyễn Văn A" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Tên đăng nhập (Username)</label>
                                <input type="text" name="username" class="form-control" placeholder="staff_01" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Mật khẩu</label>
                                <input type="password" name="password" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Email</label>
                                <input type="email" name="email" class="form-control" placeholder="staff@bookstore.com" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Phân quyền</label>
                                <select name="roleId" class="form-select">
                                    <option value="2">Nhân viên (Staff)</option>
                                    <option value="1">Quản trị viên (Admin)</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Gán Ca Trực</label>
                                <select name="shiftId" class="form-select">
                                    <c:forEach var="shift" items="${SHIFT_LIST}">
                                        <option value="${shift.id}">${shift.name} (${shift.startTime})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Số điện thoại</label>
                                <input type="text" name="phone" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary px-4">Tạo Tài Khoản</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>