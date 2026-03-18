<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Nhà Xuất Bản - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
            .card { border: none; border-radius: 15px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
            .table thead { background-color: #f1f3f5; }
            .contact-info { font-size: 0.85rem; color: #6c757d; }
        </style>
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-4 px-2">
                <div>
                    <h2 class="fw-bold mb-0">Quản Lý Nhà Xuất Bản</h2>
                    <p class="text-muted">Danh sách các đối tác cung cấp sách của hệ thống</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary me-2 shadow-sm">
                        <i class="bi bi-speedometer2"></i> Dashboard
                    </a>
                    <button class="btn btn-primary shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addPublisherModal">
                        <i class="bi bi-plus-circle me-1"></i> Thêm NXB Mới
                    </button>
                </div>
            </div>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="MSG_SUCCESS" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.MSG_ERROR}">
                <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-x-circle-fill me-2"></i> ${sessionScope.MSG_ERROR}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="MSG_ERROR" scope="session"/>
            </c:if>

            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Tên Nhà Xuất Bản</th>
                                    <th>Địa Chỉ / Mô Tả</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="p" items="${PUBLISHER_LIST}">
                                <tr>
                                    <%-- ✅ Dùng p.id thay vì p.publisher_id --%>
                                    <td class="ps-4 text-muted fw-bold">#${p.id}</td>
                                    <td><strong class="text-dark">${p.name}</strong></td>
                                    <%-- ✅ Dùng p.description thay vì p.address (DAO map address -> description) --%>
                                    <td>
                                        <span class="text-truncate d-inline-block" style="max-width: 350px;" title="${p.description}">
                                            ${p.description}
                                        </span>
                                    </td>
                                    <td class="text-end pe-4">
                                        <div class="btn-group shadow-sm">
                                            <a href="MainController?action=editPublisher&id=${p.id}" class="btn btn-sm btn-white text-info border">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                            <a href="MainController?action=deletePublisher&id=${p.id}"
                                               class="btn btn-sm btn-white text-danger border"
                                               onclick="return confirm('Xóa Nhà xuất bản có thể ảnh hưởng đến các đầu sách liên quan. Bạn chắc chắn chứ?')">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty PUBLISHER_LIST}">
                                <tr>
                                    <td colspan="4" class="text-center py-5 text-muted">
                                        Chưa có dữ liệu Nhà xuất bản.
                                    </td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <%-- MODAL THÊM NXB --%>
        <div class="modal fade" id="addPublisherModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                    <input type="hidden" name="action" value="addPublisher">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">Thêm Nhà Xuất Bản Mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Tên NXB <span class="text-danger">*</span></label>
                            <input type="text" name="name" class="form-control" placeholder="Ví dụ: NXB Trẻ" required>
                        </div>
                        <div class="mb-0">
                            <label class="form-label fw-bold">Địa chỉ / Mô tả</label>
                            <textarea name="address" class="form-control" rows="3" placeholder="Số, Tên đường, Quận/Huyện..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary px-4">Lưu Thông Tin</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
