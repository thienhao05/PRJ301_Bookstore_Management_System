<%-- 
    Document   : send-notification
    Created on : Mar 12, 2026, 4:46:22 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Gửi Thông Báo - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .form-label {
                font-weight: 600;
                color: #495057;
            }
            .noti-preview {
                background-color: #fff;
                border-left: 4px solid #0d6efd;
                padding: 15px;
                border-radius: 8px;
            }
        </style>
    </head>
    <body>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h2 class="fw-bold mb-0">Trung Tâm Thông Báo</h2>
                            <p class="text-muted">Gửi tin nhắn, cập nhật hoặc khuyến mãi đến người dùng</p>
                        </div>
                        <a href="MainController?action=dashboard" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left me-1"></i> Dashboard
                        </a>
                    </div>

                    <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                        <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="MSG_SUCCESS" scope="session"/>
                    </c:if>

                    <div class="card shadow-sm">
                        <div class="card-body p-4">
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="sendNotification">

                                <div class="mb-4">
                                    <label class="form-label">Gửi đến:</label>
                                    <select name="targetUser" class="form-select shadow-sm" id="targetSelect">
                                        <option value="all">Tất cả người dùng (Broadcast)</option>
                                        <option value="specific">Người dùng cụ thể</option>
                                    </select>
                                </div>

                                <div class="mb-4 d-none" id="userIdField">
                                    <label class="form-label">Nhập User ID:</label>
                                    <input type="number" name="receiverId" class="form-control" placeholder="Ví dụ: 101">
                                </div>

                                <div class="row mb-4">
                                    <div class="col-md-6">
                                        <label class="form-label">Loại thông báo:</label>
                                        <select name="notiType" class="form-select">
                                            <option value="Info">Thông tin (Info)</option>
                                            <option value="Promotion">Khuyến mãi (Promo)</option>
                                            <option value="Warning">Cảnh báo (Warning)</option>
                                            <option value="System">Hệ thống (System)</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Tiêu đề:</label>
                                        <input type="text" name="title" class="form-control" placeholder="Ví dụ: Ưu đãi tháng 3..." required>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label">Nội dung tin nhắn:</label>
                                    <textarea name="content" class="form-control" rows="5" placeholder="Viết nội dung thông báo tại đây..." required></textarea>
                                </div>

                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary btn-lg shadow-sm">
                                        <i class="bi bi-send-fill me-2"></i> Phát hành thông báo
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="mt-5">
                        <h6 class="text-muted text-uppercase small fw-bold mb-3">Xem trước hiển thị</h6>
                        <div class="noti-preview shadow-sm border">
                            <h6 class="fw-bold mb-1"><i class="bi bi-megaphone me-2 text-primary"></i> [Tiêu đề sẽ hiển thị ở đây]</h6>
                            <p class="mb-0 text-muted small">Nội dung chi tiết của thông báo sẽ xuất hiện trong trung tâm thông báo của người dùng...</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            document.getElementById('targetSelect').addEventListener('change', function () {
                const userIdField = document.getElementById('userIdField');
                if (this.value === 'specific') {
                    userIdField.classList.remove('d-none');
                } else {
                    userIdField.classList.add('d-none');
                }
            });
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
