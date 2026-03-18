<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // 1. Kiểm tra trạng thái đăng nhập ngay tại cổng
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");

    if (user != null) {
        // Nếu là Admin (roleId = 1 hoặc 2), đẩy thẳng vào Dashboard để quản lý
        if (user.getRoleId() == 1 || user.getRoleId() == 2) {
            response.sendRedirect("MainController?action=dashboard");
        } else {
            // Nếu là khách hàng hoặc role khác, đẩy về trang chủ mua sắm
            response.sendRedirect("MainController?action=home");
        }
        return; // Dừng xử lý các dòng bên dưới
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hào's Bookstore - Chào mừng bạn</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6; /* Hệ màu đồng bộ với các trang trước */
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: 'Segoe UI', Tahoma, sans-serif;
                margin: 0;
            }
            .landing-card {
                background: white;
                border: none;
                border-radius: 30px;
                box-shadow: 0 20px 60px rgba(0,0,0,0.1);
                overflow: hidden;
                max-width: 450px;
                width: 90%;
                text-align: center;
            }
            .hero-gradient {
                background: linear-gradient(135deg, #0d6efd 0%, #003da5 100%);
                padding: 60px 20px;
                color: white;
            }
            .btn-action {
                border-radius: 15px;
                padding: 12px 25px;
                font-weight: 600;
                transition: 0.3s;
            }
            .btn-primary-custom {
                background-color: #0d6efd;
                color: white;
                border: none;
            }
            .btn-primary-custom:hover {
                background-color: #0056b3;
                transform: translateY(-3px);
                box-shadow: 0 10px 20px rgba(13, 110, 253, 0.3);
            }
        </style>
    </head>
    <body>

        <div class="landing-card shadow-lg">
            <div class="hero-gradient">
                <div class="mb-3">
                    <i class="bi bi-book-half" style="font-size: 4rem;"></i>
                </div>
                <h2 class="fw-bold">Hào's Bookstore</h2>
                <p class="small opacity-75 mb-0">Hệ thống quản lý & mua sắm sách trực tuyến</p>
            </div>

            <div class="card-body p-5">
                <h4 class="text-dark fw-bold mb-3">Sẵn sàng khám phá?</h4>
                <p class="text-muted small mb-5">Chào mừng bạn đến với dự án tâm huyết của Hao. Hãy chọn một hành động để bắt đầu!</p>

                <div class="d-grid gap-3">
                    <a href="MainController?action=home" class="btn btn-primary-custom btn-action">
                        <i class="bi bi-shop me-2"></i>Vào cửa hàng ngay
                    </a>

                    <div class="row g-2">
                        <div class="col-6">
                            <a href="MainController?action=login" class="btn btn-outline-primary btn-action w-100">
                                Đăng nhập
                            </a>
                        </div>
                        <div class="col-6">
                            <a href="MainController?action=logout" class="btn btn-outline-secondary btn-action w-100">
                                Đăng ký
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="bg-light py-3">
                <small class="text-muted">© 2026 Designed by Thien Hao (FPTU)</small>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>