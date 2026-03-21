<%-- 
    Document   : manage-discounts
    Created on : Mar 12, 2026, 4:45:46 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Mã Giảm Giá - Admin</title>
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
            .code-badge {
                font-family: 'Courier New', Courier, monospace;
                font-weight: bold;
                letter-spacing: 1px;
            }
            .status-active {
                color: #198754;
                background-color: #e6f4ea;
            }
            .status-expired {
                color: #dc3545;
                background-color: #fce8e8;
            }
        </style>
    </head>
    <body>

        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Chương Trình Khuyến Mãi</h2>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb mb-0">
                            <li class="breadcrumb-item"><a href="MainController?action=dashboard">Dashboard</a></li>
                            <li class="breadcrumb-item active">Mã giảm giá</li>
                        </ol>
                    </nav>
                </div>
                <div>
                    <button class="btn btn-primary shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addDiscountModal">
                        <i class="bi bi-plus-circle me-2"></i> Tạo Mã Mới
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
                                    <th>Mã Code</th>
                                    <th>Mức Giảm</th>
                                    <th>Thời Gian Áp Dụng</th>
                                    <th class="text-center">Trạng Thái</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="d" items="${DISCOUNT_LIST}">
                                <tr>
                                    <td class="ps-4 text-muted">#${d.discount_id}</td>
                                    <td>
                                        <span class="badge bg-dark text-white code-badge px-3 py-2">${d.code}</span>
                                    </td>
                                    <td>
                                        <span class="fw-bold text-danger">-${d.discount_percent}%</span>
                                    </td>
                                    <td>
                                        <div class="small text-muted">
                                            Từ: <fmt:formatDate value="${d.start_date}" pattern="dd/MM/yyyy"/><br>
                                            Đến: <fmt:formatDate value="${d.end_date}" pattern="dd/MM/yyyy"/>
                                        </div>
                                    </td>
                                    <td class="text-center">
                                <c:choose>
                                    <c:when test="${d.status == 'Active'}">
                                        <span class="badge rounded-pill status-active border border-success px-3">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge rounded-pill status-expired border border-danger px-3">Tạm dừng</span>
                                    </c:otherwise>
                                </c:choose>
                                </td>
                                <td class="text-end pe-4">
                                    <a href="MainController?action=editDiscount&id=${d.discount_id}" class="btn btn-sm btn-outline-info me-1">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <a href="MainController?action=deleteDiscount&id=${d.discount_id}" 
                                       class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Xác nhận xóa mã giảm giá này?')">
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

        <div class="modal fade" id="addDiscountModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                    <input type="hidden" name="action" value="addDiscount">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Thiết Lập Mã Giảm Giá</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Mã Code (Viết liền, không dấu)</label>
                            <input type="text" name="code" class="form-control text-uppercase" placeholder="VD: BOOKFPT2026" required>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <label class="form-label fw-bold">Phần trăm giảm (%)</label>
                                <input type="number" name="percentage" class="form-control" min="1" max="100" placeholder="1 - 100" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Ngày bắt đầu</label>
                                <input type="date" name="startDate" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Ngày kết thúc</label>
                                <input type="date" name="endDate" class="form-control" required>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary px-4">Lưu Mã</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>