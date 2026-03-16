<%-- 
    Document   : register
    Created on : Mar 12, 2026, 4:50:26 AM
    Author     : PC (Hào)
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tham Gia Cùng Hào's Bookstore</title>
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
                background-color: #198754; /* Màu xanh lá tạo cảm giác tươi mới */
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

                    <%@include file="../components/message-alert.jsp" %>

                    <div class="card register-card">
                        <div class="register-header">
                            <h3 class="fw-bold mb-0">Tạo Tài Khoản</h3>
                            <p class="small opacity-75 mb-0">Trở thành thành viên của gia đình Bookstore</p>
                        </div>

                        <div class="card-body p-4 p-md-5 bg-white">
                            <form action="MainController" method="POST" id="registerForm">
                                <input type="hidden" name="action" value="register">

                                <div class="row g-3">
                                    <div class="col-md-12">
                                        <label class="form-label text-uppercase small">Tên đăng nhập</label>
                                        <input type="text" name="username" class="form-control" placeholder="Ví dụ: hao.tien123" required>
                                    </div>

                                    <div class="col-md-12">
                                        <label class="form-label text-uppercase small">Họ và Tên</label>
                                        <input type="text" name="fullName" class="form-control" placeholder="Nguyễn Thiên Hào" required>
                                    </div>

                                    <div class="col-md-7">
                                        <label class="form-label text-uppercase small">Email</label>
                                        <input type="email" name="email" class="form-control" placeholder="hao@fpt.edu.vn" required>
                                    </div>
                                    <div class="col-md-5">
                                        <label class="form-label text-uppercase small">Số điện thoại</label>
                                        <input type="tel" name="phone" class="form-control" placeholder="090..." required>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="form-label text-uppercase small">Mật khẩu</label>
                                        <input type="password" id="password" name="password" class="form-control" required minlength="6">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label text-uppercase small">Xác nhận mật khẩu</label>
                                        <input type="password" id="confirmPassword" class="form-control" required>
                                    </div>

                                    <div id="passwordError" class="text-danger small d-none mb-2">
                                        <i class="bi bi-exclamation-circle me-1"></i> Mật khẩu xác nhận không khớp!
                                    </div>
                                </div>

                                <div class="d-grid mt-4">
                                    <button type="submit" class="btn btn-success btn-register">
                                        Đăng Ký Thành Viên <i class="bi bi-person-plus ms-2"></i>
                                    </button>
                                </div>

                                <div class="text-center mt-4 pt-2 border-top">
                                    <p class="small text-muted mb-0">Bạn đã có tài khoản?</p>
                                    <a href="login.jsp" class="fw-bold text-decoration-none text-success">Đăng nhập ngay</a>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="text-center mt-4">
                        <a href="MainController?action=home" class="text-muted small text-decoration-none">
                            <i class="bi bi-house-door me-1"></i> Quay về trang chủ
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Script kiểm tra mật khẩu khớp nhau ngay tại Client
            document.getElementById('registerForm').addEventListener('submit', function (e) {
                const pass = document.getElementById('password').value;
                const confirm = document.getElementById('confirmPassword').value;
                const error = document.getElementById('passwordError');

                if (pass !== confirm) {
                    e.preventDefault();
                    error.classList.remove('d-none');
                } else {
                    error.classList.add('d-none');
                }
            });
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>