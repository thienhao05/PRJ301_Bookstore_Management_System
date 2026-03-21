<%-- 
    Document   : change-passwor
    Created on : Mar 12, 2026, 4:47:24 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đổi Mật Khẩu - Hào's Bookstore</title>
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
            .btn-back {
                transition: 0.3s;
            }
            .btn-back:hover {
                transform: translateX(-5px);
            }
            .form-label {
                font-weight: 600;
                color: #495057;
            }
            /* Đồng bộ màu border với select ở trang chi tiết đơn hàng */
            .form-control:focus {
                border-color: #0d6efd;
                box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
            }
        </style>
    </head>
    <body>

        <div class="container py-5">
            <div class="mb-4">
                <a href="MainController?action=viewProfile" class="btn btn-outline-secondary btn-sm btn-back mb-2">
                    <i class="bi bi-arrow-left"></i> Quay lại hồ sơ
                </a>
                <h2 class="fw-bold text-dark">Thiết lập <span class="text-primary">Mật khẩu</span></h2>
                <span class="text-muted small"><i class="bi bi-shield-lock me-1"></i> Bảo mật tài khoản của bạn</span>
            </div>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="MSG_SUCCESS" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.MSG_ERROR}">
                <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.MSG_ERROR}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="MSG_ERROR" scope="session"/>
            </c:if>

            <div class="row justify-content-center">
                <div class="col-lg-5">
                    <div class="card order-card">
                        <div class="card-body p-4 p-md-5">
                            <div class="text-center mb-4">
                                <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex align-items-center justify-content-center mb-3" style="width: 60px; height: 60px;">
                                    <i class="bi bi-key-fill text-primary fs-3"></i>
                                </div>
                                <h5 class="fw-bold">Thay đổi mật khẩu</h5>
                                <p class="small text-muted">Vui lòng không chia sẻ mật khẩu cho bất kỳ ai</p>
                            </div>

                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="changePassword">

                                <div class="mb-3">
                                    <label class="form-label small text-uppercase">Mật khẩu hiện tại</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock"></i></span>
                                        <input type="password" name="oldPassword" class="form-control border-start-0 ps-0" placeholder="••••••••" required>
                                    </div>
                                </div>

                                <hr class="my-4 opacity-25">

                                <div class="mb-3">
                                    <label class="form-label small text-uppercase">Mật khẩu mới</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-shield-plus"></i></span>
                                        <input type="password" name="newPassword" class="form-control border-start-0 ps-0" placeholder="Tối thiểu 6 ký tự" required minlength="6">
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label small text-uppercase">Xác nhận mật khẩu mới</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-shield-check"></i></span>
                                        <input type="password" name="confirmPassword" class="form-control border-start-0 ps-0" placeholder="Nhập lại mật khẩu mới" required>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-primary btn-lg w-100 shadow-sm fw-bold">
                                    Cập nhật mật khẩu
                                </button>

                                <div class="text-center mt-3">
                                    <a href="MainController?action=viewProfile" class="text-decoration-none small text-muted">Hủy bỏ</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
