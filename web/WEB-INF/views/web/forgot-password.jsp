<%-- 
    Document   : forgot-password
    Created on : Mar 12, 2026, 4:50:48 AM
    Author     : PC (Hào)
--%>

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
                background-color: #f4f7f6;
                height: 100vh;
                display: flex;
                align-items: center;
                font-family: 'Segoe UI', sans-serif;
            }
            .order-card {
                border: none;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            }
            .icon-circle {
                width: 80px;
                height: 80px;
                background-color: #f0f7ff;
                color: #0d6efd;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 50%;
                margin: 0 auto 20px;
            }
            .form-control {
                border-radius: 10px;
                padding: 12px 15px;
            }
            .btn-reset {
                padding: 12px;
                border-radius: 10px;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5 col-xl-4">

                    <%@include file="message-alert.jsp" %>

                    <div class="card order-card p-4 p-md-5 bg-white">
                        <div class="text-center mb-4">
                            <div class="icon-circle">
                                <i class="bi bi-shield-lock-fill fs-1"></i>
                            </div>
                            <h3 class="fw-bold text-dark">Quên mật khẩu?</h3>
                            <p class="text-muted small">Đừng lo! Nhập email tài khoản của bạn và chúng tôi sẽ gửi hướng dẫn khôi phục.</p>
                        </div>

                        <form action="MainController" method="POST">
                            <input type="hidden" name="action" value="forgotPassword">

                            <div class="mb-4">
                                <label class="form-label small fw-bold text-muted">Địa chỉ Email</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                    <input type="email" name="email" class="form-control bg-light border-start-0" 
                                           placeholder="vi-du@email.com" required autofocus>
                                </div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary btn-reset shadow-sm">
                                    Gửi yêu cầu khôi phục
                                </button>
                                <a href="login.jsp" class="btn btn-link text-decoration-none text-muted small mt-2">
                                    <i class="bi bi-arrow-left me-1"></i> Quay lại Đăng nhập
                                </a>
                            </div>
                        </form>
                    </div>

                    <p class="text-center mt-4 text-muted small">
                        Bạn chưa có tài khoản? <a href="register.jsp" class="fw-bold text-primary text-decoration-none">Đăng ký ngay</a>
                    </p>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>