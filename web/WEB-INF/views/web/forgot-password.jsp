<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quên Mật Khẩu - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background: linear-gradient(135deg, #f4f7f6 0%, #e1e9e8 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                font-family: 'Segoe UI', sans-serif;
            }
            .forgot-card {
                border: none;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            .forgot-header {
                background: linear-gradient(135deg, #0d6efd 0%, #003da5 100%);
                padding: 30px;
                color: white;
                text-align: center;
            }
            .form-control {
                border-radius: 10px;
                padding: 10px 15px;
                background-color: #f8f9fa;
            }
            .form-control:focus {
                box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.1);
                border-color: #0d6efd;
            }
            .step-badge {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                background: #0d6efd;
                color: white;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                font-size: 13px;
                font-weight: bold;
                margin-right: 8px;
                flex-shrink: 0;
            }
        </style>
    </head>
    <body>
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-md-7 col-lg-5">

                    <%-- Thông báo lỗi --%>
                    <c:if test="${not empty requestScope.MSG_ERROR}">
                        <div class="alert alert-danger border-0 shadow-sm mb-4" role="alert">
                            <i class="bi bi-exclamation-circle-fill me-2"></i>
                            ${requestScope.MSG_ERROR}
                        </div>
                    </c:if>

                    <div class="card forgot-card">
                        <div class="forgot-header">
                            <i class="bi bi-shield-lock display-5 mb-2"></i>
                            <h4 class="fw-bold mb-1">Quên Mật Khẩu?</h4>
                            <p class="small opacity-75 mb-0">
                                Nhập email và mật khẩu mới để đặt lại
                            </p>
                        </div>

                        <div class="card-body p-4 p-md-5 bg-white">

                            <%-- Hướng dẫn --%>
                            <div class="alert alert-info border-0 py-2 mb-4 small">
                                <i class="bi bi-info-circle me-2"></i>
                                Nhập email đã đăng ký và mật khẩu mới để đặt lại tài khoản
                            </div>

                            <form action="MainController" method="POST"
                                  id="forgotForm" accept-charset="UTF-8">
                                <input type="hidden" name="action" value="resetPassword">

                                <%-- Email --%>
                                <div class="mb-3">
                                    <label class="form-label fw-bold small text-uppercase d-flex align-items-center">
                                        <span class="step-badge">1</span>
                                        Xác nhận Email
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0">
                                            <i class="bi bi-envelope text-muted"></i>
                                        </span>
                                        <input type="email"
                                               name="email"
                                               class="form-control border-start-0"
                                               placeholder="Email đã đăng ký"
                                               required autofocus>
                                    </div>
                                </div>

                                <%-- Mật khẩu mới --%>
                                <div class="mb-3">
                                    <label class="form-label fw-bold small text-uppercase d-flex align-items-center">
                                        <span class="step-badge">2</span>
                                        Mật Khẩu Mới
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0">
                                            <i class="bi bi-lock text-muted"></i>
                                        </span>
                                        <input type="password"
                                               id="newPassword"
                                               name="newPassword"
                                               class="form-control border-start-0"
                                               placeholder="Tối thiểu 6 ký tự"
                                               required minlength="6">
                                    </div>
                                </div>

                                <%-- Xác nhận mật khẩu mới --%>
                                <div class="mb-4">
                                    <label class="form-label fw-bold small text-uppercase d-flex align-items-center">
                                        <span class="step-badge">3</span>
                                        Xác Nhận Mật Khẩu Mới
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0">
                                            <i class="bi bi-lock-fill text-muted"></i>
                                        </span>
                                        <input type="password"
                                               id="confirmPassword"
                                               name="confirm"
                                               class="form-control border-start-0"
                                               placeholder="Nhập lại mật khẩu mới"
                                               required>
                                    </div>
                                    <%-- Thông báo lỗi client-side --%>
                                    <div id="passwordError"
                                         class="text-danger small mt-1 d-none">
                                        <i class="bi bi-exclamation-circle me-1"></i>
                                        Mật khẩu xác nhận không khớp!
                                    </div>
                                </div>

                                <%-- Nút submit --%>
                                <div class="d-grid mb-3">
                                    <button type="submit"
                                            class="btn btn-primary btn-lg fw-bold"
                                            style="border-radius:10px; padding:12px;">
                                        <i class="bi bi-arrow-repeat me-2"></i>
                                        Đặt Lại Mật Khẩu
                                    </button>
                                </div>

                                <%-- Link quay lại --%>
                                <div class="text-center">
                                    <a href="MainController?action=login"
                                       class="text-decoration-none text-muted small">
                                        <i class="bi bi-arrow-left me-1"></i>
                                        Quay lại đăng nhập
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="text-center mt-4">
                        <a href="MainController?action=home"
                           class="text-muted small text-decoration-none">
                            <i class="bi bi-house-door me-1"></i>
                            Quay về trang chủ
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Kiểm tra mật khẩu khớp client-side
            document.getElementById('forgotForm').addEventListener('submit', function (e) {
                const newPass = document.getElementById('newPassword').value;
                const confirm = document.getElementById('confirmPassword').value;
                const error   = document.getElementById('passwordError');

                if (newPass !== confirm) {
                    e.preventDefault();
                    error.classList.remove('d-none');
                    document.getElementById('confirmPassword').focus();
                } else {
                    error.classList.add('d-none');
                }
            });

            // Ẩn lỗi khi gõ lại
            document.getElementById('confirmPassword').addEventListener('input', function () {
                const newPass = document.getElementById('newPassword').value;
                const error   = document.getElementById('passwordError');
                if (newPass === this.value) {
                    error.classList.add('d-none');
                }
            });
        </script>
    </body>
</html>
