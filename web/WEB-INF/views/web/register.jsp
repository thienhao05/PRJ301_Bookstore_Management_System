<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Ký - Hào's Bookstore</title>
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
            .register-card {
                border: none;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            .register-header {
                background-color: #198754;
                padding: 30px;
                color: white;
                text-align: center;
            }
            .form-label {
                font-weight: 600;
                color: #495057;
                font-size: 0.85rem;
            }
            .form-control {
                border-radius: 10px;
                padding: 10px 15px;
                background-color: #f8f9fa;
            }
            .form-control:focus {
                box-shadow: 0 0 0 0.25rem rgba(25, 135, 84, 0.1);
                border-color: #198754;
            }
            .btn-register {
                padding: 12px;
                border-radius: 10px;
                font-weight: 600;
                transition: 0.3s;
            }
            .btn-register:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(25, 135, 84, 0.3);
            }
        </style>
    </head>
    <body>
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-md-8 col-lg-6 col-xl-5">

                    <%-- Hiện thông báo lỗi nếu có --%>
                    <c:if test="${not empty requestScope.MSG_ERROR}">
                        <div class="alert alert-danger border-0 shadow-sm mb-4" role="alert">
                            <i class="bi bi-exclamation-circle-fill me-2"></i>
                            ${requestScope.MSG_ERROR}
                        </div>
                    </c:if>

                    <div class="card register-card">
                        <div class="register-header">
                            <i class="bi bi-book-half display-5 mb-2"></i>
                            <h3 class="fw-bold mb-0">Tạo Tài Khoản</h3>
                            <p class="small opacity-75 mb-0">
                                Trở thành thành viên của Hào's Bookstore
                            </p>
                        </div>

                        <div class="card-body p-4 p-md-5 bg-white">
                            <form action="MainController" method="POST" id="registerForm">
                                <input type="hidden" name="action" value="register">

                                <div class="row g-3">

                                    <%-- Họ và tên --%>
                                    <div class="col-12">
                                        <label class="form-label text-uppercase small">
                                            Họ và Tên <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-end-0">
                                                <i class="bi bi-person text-muted"></i>
                                            </span>
                                            <input type="text"
                                                   name="fullName"
                                                   class="form-control border-start-0"
                                                   placeholder="Nguyễn Văn A"
                                                   required>
                                        </div>
                                    </div>

                                    <%-- Email --%>
                                    <div class="col-12">
                                        <label class="form-label text-uppercase small">
                                            Địa chỉ Email <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-end-0">
                                                <i class="bi bi-envelope text-muted"></i>
                                            </span>
                                            <input type="email"
                                                   name="email"
                                                   class="form-control border-start-0"
                                                   placeholder="example@email.com"
                                                   required>
                                        </div>
                                    </div>

                                    <%-- Số điện thoại --%>
                                    <div class="col-12">
                                        <label class="form-label text-uppercase small">
                                            Số điện thoại
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-end-0">
                                                <i class="bi bi-telephone text-muted"></i>
                                            </span>
                                            <input type="tel"
                                                   name="phone"
                                                   class="form-control border-start-0"
                                                   placeholder="090xxxxxxx">
                                        </div>
                                    </div>

                                    <%-- Mật khẩu --%>
                                    <div class="col-md-6">
                                        <label class="form-label text-uppercase small">
                                            Mật khẩu <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-end-0">
                                                <i class="bi bi-lock text-muted"></i>
                                            </span>
                                            <input type="password"
                                                   id="password"
                                                   name="password"
                                                   class="form-control border-start-0"
                                                   placeholder="Tối thiểu 6 ký tự"
                                                   required minlength="6">
                                        </div>
                                    </div>

                                    <%-- Xác nhận mật khẩu --%>
                                    <div class="col-md-6">
                                        <label class="form-label text-uppercase small">
                                            Xác nhận mật khẩu <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text bg-light border-end-0">
                                                <i class="bi bi-lock-fill text-muted"></i>
                                            </span>
                                            <%-- QUAN TRỌNG: name="confirm" phải khớp với controller --%>
                                            <input type="password"
                                                   id="confirmPassword"
                                                   name="confirm"
                                                   class="form-control border-start-0"
                                                   placeholder="Nhập lại mật khẩu"
                                                   required>
                                        </div>
                                    </div>

                                    <%-- Thông báo lỗi mật khẩu (client-side) --%>
                                    <div class="col-12">
                                        <div id="passwordError"
                                             class="alert alert-danger py-2 small d-none mb-0">
                                            <i class="bi bi-exclamation-circle me-1"></i>
                                            Mật khẩu xác nhận không khớp!
                                        </div>
                                    </div>

                                </div>

                                <%-- Nút đăng ký --%>
                                <div class="d-grid mt-4">
                                    <button type="submit" class="btn btn-success btn-register">
                                        <i class="bi bi-person-plus me-2"></i>
                                        Đăng Ký Thành Viên
                                    </button>
                                </div>

                                <%-- Link đăng nhập --%>
                                <div class="text-center mt-4 pt-3 border-top">
                                    <p class="small text-muted mb-1">Bạn đã có tài khoản?</p>
                                    <a href="MainController?action=login"
                                       class="fw-bold text-decoration-none text-success">
                                        <i class="bi bi-box-arrow-in-right me-1"></i>
                                        Đăng nhập ngay
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <%-- Link về trang chủ --%>
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
            // Kiểm tra mật khẩu khớp ngay tại client trước khi submit
            document.getElementById('registerForm').addEventListener('submit', function (e) {
                const pass = document.getElementById('password').value;
                const confirm = document.getElementById('confirmPassword').value;
                const error = document.getElementById('passwordError');

                if (pass !== confirm) {
                    e.preventDefault(); // chặn submit
                    error.classList.remove('d-none');
                    document.getElementById('confirmPassword').focus();
                } else {
                    error.classList.add('d-none');
                }
            });

            // Ẩn thông báo lỗi khi user gõ lại
            document.getElementById('confirmPassword').addEventListener('input', function () {
                const pass = document.getElementById('password').value;
                const confirm = this.value;
                const error = document.getElementById('passwordError');

                if (pass === confirm) {
                    error.classList.add('d-none');
                }
            });
        </script>
    </body>
</html>