<%-- 
    Document   : manage-staff
    Created on : Mar 12, 2026, 4:44:34 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Nhân Sự - Admin</title>
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
            .table thead {
                background-color: #f1f3f5;
            }
            .status-active {
                background-color: #e6f4ea;
                color: #1e7e34;
            }
            .status-inactive {
                background-color: #fdeaea;
                color: #d9534f;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Hồ Sơ Nhân Viên</h2>
                    <p class="text-muted">Quản lý thông tin hợp đồng, lương và ngày vào làm</p>
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
                                    <th class="ps-4 py-3">Staff ID</th>
                                    <th>User ID</th>
                                    <th>Ngày Vào Làm</th>
                                    <th>Mức Lương</th>
                                    <th class="text-center">Trạng Thái</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="s" items="${STAFF_LIST}">
                                <tr>
                                    <td class="ps-4 fw-bold text-primary">#${s.staff_id}</td>
                                    <td>
                                        <span class="badge bg-light text-dark border">UID: ${s.user_id}</span>
                                    </td>
                                    <td>
                                        <i class="bi bi-calendar3 me-1 text-muted"></i>
                                <fmt:formatDate value="${s.hire_date}" pattern="dd/MM/yyyy"/>
                                </td>
                                <td class="fw-bold">
                                <fmt:formatNumber value="${s.salary}" pattern="#,###"/> đ
                                </td>
                                <td class="text-center">
                                    <span class="badge rounded-pill ${s.status == 'Active' ? 'status-active' : 'status-inactive'} px-3">
                                        ${s.status}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <div class="btn-group">
                                        <a href="MainController?action=editStaff&id=${s.staff_id}" class="btn btn-sm btn-outline-info">
                                            <i class="bi bi-pencil-square"></i>
                                        </a>
                                        <a href="MainController?action=deleteStaff&id=${s.staff_id}" 
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Xác nhận cho nhân viên này nghỉ việc?')">
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
                        <h5 class="modal-title fw-bold">Ký Hợp Đồng Nhân Viên Mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Họ và Tên</label>
                                <input type="text" name="fullName" class="form-control" placeholder="Nhập tên nhân viên..." required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Email (Dùng để đăng nhập)</label>
                                <input type="email" name="email" class="form-control" placeholder="staff@example.com" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Mật khẩu khởi tạo</label>
                                <input type="password" name="password" class="form-control" required>
                            </div>

                            <div class="col-md-3">
                                <label class="form-label fw-bold">Mức lương (VNĐ)</label>
                                <input type="number" name="salary" class="form-control" placeholder="0" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label fw-bold">Ngày vào làm</label>
                                <input type="date" name="hireDate" class="form-control" required>
                            </div>
                        </div>
                        <div class="alert alert-info mt-3 mb-0 py-2 small">
                            <i class="bi bi-info-circle me-2"></i> Hệ thống sẽ tự động tạo tài khoản User với quyền Staff cho nhân viên này.
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary px-4">Tạo Hồ Sơ</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
