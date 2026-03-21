<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hồ Sơ Cá Nhân - ${sessionScope.LOGIN_USER.fullName}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .order-card {
                border: none;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            }
            .status-badge {
                padding: 6px 12px;
                border-radius: 20px;
                font-weight: 600;
                font-size: 0.85rem;
            }
            .btn-back {
                transition: 0.3s;
            }
            .btn-back:hover {
                transform: translateX(-5px);
            }
            .avatar-circle {
                width: 100px;
                height: 100px;
                background-color: #0d6efd;
                color: white;
                font-size: 2.5rem;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 50%;
                margin: 0 auto 20px;
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Thêm header --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <a href="MainController?action=home"
                       class="btn btn-outline-secondary btn-sm btn-back mb-2">
                        <i class="bi bi-arrow-left"></i> Quay lại cửa hàng
                    </a>
                    <h2 class="fw-bold text-dark">
                        Hồ sơ <span class="text-primary">Cá nhân</span>
                    </h2>
                    <span class="text-muted">
                        <i class="bi bi-person-check me-1"></i> Quản lý thông tin tài khoản của bạn
                    </span>
                </div>
                <span class="status-badge bg-success text-white">
                    <i class="bi bi-patch-check-fill me-1"></i> Đang hoạt động
                </span>
            </div>

            <%-- ✅ SỬA: Dùng include thay vì viết tay --%>
            <%@include file="../components/message-alert.jsp" %>

            <div class="row g-4">
                <%-- CỘT TRÁI: Avatar + nút --%>
                <div class="col-lg-4">
                    <div class="card order-card h-100">
                        <div class="card-body p-4 text-center">
                            <div class="avatar-circle shadow-sm">
                                <%-- ✅ SỬA: Dùng fn:substring hoặc EL an toàn hơn --%>
                                ${sessionScope.LOGIN_USER.fullName.substring(0,1).toUpperCase()}
                            </div>
                            <h4 class="fw-bold mb-1">${sessionScope.LOGIN_USER.fullName}</h4>
                            <p class="text-muted small mb-1">${sessionScope.LOGIN_USER.email}</p>
                            <p class="text-muted small mb-4">Thành viên từ: 2026</p>

                            <hr class="my-4 opacity-50">

                            <div class="d-grid gap-2">
                                <a href="MainController?action=myOrders"
                                   class="btn btn-outline-success shadow-sm">
                                    <i class="bi bi-bag me-2"></i> Đơn hàng của tôi
                                </a>
                                <a href="MainController?action=logout"
                                   class="btn btn-danger shadow-sm mt-2">
                                    <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- CỘT PHẢI: Form cập nhật --%>
                <div class="col-lg-8">
                    <div class="card order-card h-100">
                        <div class="card-body p-4">
                            <h5 class="card-title fw-bold mb-4">Chi tiết tài khoản</h5>

                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="updateProfile">

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted small text-uppercase">
                                            Email (không thể đổi)
                                        </label>
                                        <%-- ✅ SỬA: Bỏ username (không có), dùng email readonly --%>
                                        <input type="text" class="form-control bg-light"
                                               value="${sessionScope.LOGIN_USER.email}" readonly>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold text-muted small text-uppercase">
                                            Vai trò
                                        </label>
                                        <%-- ✅ SỬA: Dùng roleId (int) thay vì roleID (String) --%>
                                        <input type="text" class="form-control bg-light"
                                               value="${sessionScope.LOGIN_USER.roleId == 1 ? 'Admin' :
                                                        sessionScope.LOGIN_USER.roleId == 2 ? 'Manager' :
                                                        sessionScope.LOGIN_USER.roleId == 3 ? 'Staff' : 'Khách hàng'}"
                                               readonly>
                                    </div>
                                    <div class="col-md-12">
                                        <label class="form-label fw-bold">Họ và Tên</label>
                                        <input type="text" name="fullName" class="form-control border-primary"
                                               value="${sessionScope.LOGIN_USER.fullName}" required>
                                    </div>
                                    <div class="col-md-12">
                                        <label class="form-label fw-bold">Địa chỉ Email</label>
                                        <input type="email" name="email" class="form-control border-primary"
                                               value="${sessionScope.LOGIN_USER.email}" required>
                                    </div>
                                    <div class="col-md-12">
                                        <label class="form-label fw-bold">Số điện thoại</label>
                                        <input type="text" name="phone" class="form-control border-primary"
                                               value="${sessionScope.LOGIN_USER.phone}">
                                    </div>
                                </div>

                                <div class="mt-4 pt-2">
                                    <button type="submit" class="btn btn-primary btn-lg px-5 shadow-sm rounded-pill">
                                        <i class="bi bi-save me-2"></i>Lưu thay đổi
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- ✅ THÊM: Footer --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
