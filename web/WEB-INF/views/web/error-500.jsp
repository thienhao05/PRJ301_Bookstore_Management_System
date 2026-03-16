<%-- 
    Document   : error-500
    Created on : Mar 12, 2026, 4:51:09 AM
    Author     : PC (Hào)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lỗi Hệ Thống - 500 Error</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6;
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: 'Segoe UI', sans-serif;
            }
            .error-card {
                border: none;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.08);
                max-width: 500px;
                width: 90%;
            }
            .error-icon {
                font-size: 5rem;
                color: #dc3545;
                margin-bottom: 20px;
                animation: pulse 2s infinite;
            }
            @keyframes pulse {
                0% {
                    transform: scale(1);
                    opacity: 1;
                }
                50% {
                    transform: scale(1.1);
                    opacity: 0.7;
                }
                100% {
                    transform: scale(1);
                    opacity: 1;
                }
            }
            .btn-home {
                border-radius: 10px;
                padding: 12px 30px;
                font-weight: 600;
                transition: 0.3s;
            }
        </style>
    </head>
    <body>

        <div class="card error-card p-5 text-center bg-white">
            <div class="card-body">
                <div class="error-icon">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                </div>
                <h1 class="fw-bold text-dark display-5 mb-3">500</h1>
                <h4 class="fw-bold text-secondary mb-3">Lỗi Hệ Thống!</h4>
                <p class="text-muted mb-4">
                    Đã có một sự cố xảy ra phía máy chủ. Đừng lo, đội ngũ kỹ thuật (là Hao đấy 😉) sẽ sớm khắc phục vấn đề này.
                </p>

                <div class="d-grid gap-2 d-sm-flex justify-content-sm-center mt-2">
                    <a href="MainController?action=home" class="btn btn-primary btn-home shadow-sm">
                        <i class="bi bi-house-door me-2"></i> Về trang chủ
                    </a>
                    <button onclick="history.back()" class="btn btn-outline-secondary btn-home">
                        <i class="bi bi-arrow-left me-2"></i> Quay lại
                    </button>
                </div>

                <%-- Section ẩn dành cho Developer để debug --%>
                <%-- Hao có thể bỏ comment dòng dưới nếu muốn xem stack trace trực tiếp --%>
                <%-- <div class="mt-4 text-start small text-danger" style="display:none;">
                    <code>Error: ${exception}</code>
                </div> --%>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>