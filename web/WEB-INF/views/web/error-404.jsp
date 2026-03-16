<%-- 
    Document   : error-404
    Created on : Mar 12, 2026, 4:50:59 AM
    Author     : PC
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
                background: linear-gradient(135deg, #f4f7f6 0%, #e9ecef 100%);
                height: 100vh;
                display: flex;
                align-items: center;
                font-family: 'Segoe UI', sans-serif;
            }
            .login-card {
                border: none;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            .login-header {
                background-color: #0d6efd;
                padding: 40px 20px;
                color: white;
                text-align: center;
            }
            .form-control {
                border-radius: 10px;
                padding: 12px 15px;
                border: 1px solid #dee2e6;
            }
            .form-control:focus {
                box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.1);
                border-color: #0d6efd;
            }
            .btn-login {
                padding: 12px;
                border-radius: 10px;
                font-weight: 600;
                transition: 0.3s;
            }
            .btn-login:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(13, 110, 253, 0.3);
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5 col-lg-4">

                    <c:if test="${not empty requestScope.ERROR}">
                        <div class="alert alert-danger border-0 shadow-sm mb-4 text-center small" role="alert">
                            <i class="bi bi-exclamation-circle-fill me-2"></i> ${requestScope.ERROR}
                        </div>
                    </c:if>

                    <div class="card login-card">
                        <div class="login-header">
                            <i class="bi bi-book-half display-4 mb-2"></i>
                            <h4 class="fw-bold mb-0">Hào's Bookstore</h4>
                            <p class="small opacity-75 mb-0">Chào mừng bạn quay trở lại</p>
                        </div>
                        <div class="card-body p-4 p-md-5">
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="login">

                                <div class="mb-3">
                                    <label class="form-label small fw-bold text-muted">Tên đăng nhập</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-person text-muted"></i></span>
                                        <input type="text" name="userID" class="form-control bg-light border-0" placeholder="Username" required autofocus>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label small fw-bold text-muted d-flex justify-content-between">
                                        Mật khẩu
                                        <a href="#" class="text-decoration-none fw-normal">Quên?</a>
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-0"><i class="bi bi-shield-lock text-muted"></i></span>
                                        <input type="password" name="password" class="form-control bg-light border-0" placeholder="••••••••" required>
                                    </div>
                                </div>

                                <div class="d-grid mb-3">
                                    <button type="submit" class="btn btn-primary btn-login">
                                        Đăng Nhập <i class="bi bi-arrow-right ms-2"></i>
                                    </button>
                                </div>

                                <div class="text-center mt-4">
                                    <p class="small text-muted mb-0">Chưa có tài khoản?</p>
                                    <a href="register.jsp" class="fw-bold text-decoration-none">Đăng ký ngay</a>
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