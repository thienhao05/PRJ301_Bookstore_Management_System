<%-- 
    Document   : manage-shifts
    Created on : Mar 12, 2026, 4:44:46 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Ca Làm Việc - Admin</title>
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
            .shift-time {
                font-weight: 600;
                color: #0d6efd;
                background-color: #e7f1ff;
                padding: 4px 10px;
                border-radius: 8px;
            }
            .table thead {
                background-color: #f1f3f5;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Lịch Trình Ca Làm Việc</h2>
                    <p class="text-muted">Thiết lập các khung giờ trực cho nhân viên cửa hàng</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2 shadow-sm">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-success shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addShiftModal">
                        <i class="bi bi-clock-history me-1"></i> Thêm Ca Mới
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
                                    <th>Tên Ca</th>
                                    <th>Khung Giờ</th>
                                    <th>Mô Tả</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${SHIFT_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${s.id}</td>
                                        <td><strong class="text-dark">${s.name}</strong></td>
                                        <td>
                                            <span class="shift-time">
                                                <i class="bi bi-clock me-1"></i> ${s.startTime} - ${s.endTime}
                                            </span>
                                        </td>
                                        <td>
                                            <small class="text-muted">${s.description}</small>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="btn-group shadow-sm">
                                                <a href="MainController?action=editShift&id=${s.id}" class="btn btn-sm btn-white text-info border">
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>
                                                <a href="MainController?action=deleteShift&id=${s.id}" 
                                                   class="btn btn-sm btn-white text-danger border"
                                                   onclick="return confirm('Bạn có chắc muốn xóa ca làm việc này?')">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty SHIFT_LIST}">
                                    <tr>
                                        <td colspan="5" class="text-center py-5 text-muted">Chưa có ca làm việc nào được thiết lập.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addShiftModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                    <input type="hidden" name="action" value="addShift">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title fw-bold">Thiết Lập Ca Làm Việc</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tên ca (Ví dụ: Ca Sáng, Ca Gãy...)</label>
                            <input type="text" name="name" class="form-control" placeholder="Nhập tên ca..." required>
                        </div>
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Giờ bắt đầu</label>
                                <input type="time" name="startTime" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Giờ kết thúc</label>
                                <input type="time" name="endTime" class="form-control" required>
                            </div>
                        </div>
                        <div class="mb-0">
                            <label class="form-label fw-bold">Ghi chú công việc</label>
                            <textarea name="description" class="form-control" rows="3" placeholder="Các công việc chính trong ca..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-success px-4">Lưu Ca Trực</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>