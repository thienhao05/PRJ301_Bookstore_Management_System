<%-- 
    Document   : login
    Created on : Mar 12, 2026, 4:50:18 AM
    Author     : PC (Hào)
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Nhập - Hào's Bookstore</title>
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
            .login-card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.08);
                overflow: hidden;
            }
            .login-header {
                background: #0d6efd;
                color: white;
                padding: 30px;
                text-align: center;
            }
            .form-control {
                border-radius: 8px;
                padding: 12px;
                border: 1px solid #dee2e6;
            }
            .form-control:focus {
                box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.1);
                border-color: #0d6efd;
            }
            .btn-login {
                padding: 12px;
                border-radius: 8px;
                font-weight: 600;
                transition: 0.3s;
            }
            .btn-login:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(13, 110, 253, 0.2);
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5 col-lg-4">

                    <c:if test="${not empty requestScope.MSG_ERROR}">
                        <div class="alert alert-danger border-0 shadow-sm mb-4 text-center small" role="alert">
                            <i class="bi bi-exclamation-circle-fill me-2"></i> ${requestScope.MSG_ERROR}
                        </div>
                    </c:if>

                    <div class="card login-card">
                        <div class="login-header">
                            <i class="bi bi-book-half display-5 mb-2"></i>
                            <h4 class="fw-bold mb-0">Hào's Bookstore</h4>
                            <p class="small opacity-75 mb-0">Hệ thống quản lý hiệu sách</p>
                        </div>

                        <div class="card-body p-4 p-md-5 bg-white">
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="login">

                                <div class="mb-3">
                                    <label class="form-label small fw-bold text-muted">Tên đăng nhập</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-envelope text-muted"></i></span>
                                        <input type="email" name="email" class="form-control bg-light border-0" 
                                               placeholder="Nhập email" required autofocus>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label small fw-bold text-muted d-flex justify-content-between">
                                        Mật khẩu
                                       <a href="MainController?action=forgotPassword">Quên mật khẩu?</a>
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-lock text-muted"></i></span>
                                        <input type="password" name="password" class="form-control bg-light border-0" 
                                               placeholder="Nhập password" required>
                                    </div>
                                </div>

                                <div class="d-grid mb-3">
                                    <button type="submit" class="btn btn-primary btn-login">
                                        Đăng Nhập <i class="bi bi-box-arrow-in-right ms-2"></i>
                                    </button>
                                </div>

                                <div class="text-center mt-4">
                                    <p class="small text-muted mb-0">Chưa có tài khoản?</p>
                                    <a href="MainController?action=register">Đăng ký thành viên</a>
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>