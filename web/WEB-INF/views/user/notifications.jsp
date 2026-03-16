<%-- 
    Document   : notifications
    Created on : Mar 12, 2026, 4:48:32 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thông báo của tôi - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .noti-card {
                border: none;
                border-radius: 12px;
                transition: 0.3s;
                border-left: 5px solid transparent;
            }
            .noti-card.unread {
                background-color: #fff;
                border-left-color: #0d6efd;
                box-shadow: 0 4px 12px rgba(13, 110, 253, 0.08);
            }
            .noti-card.read {
                background-color: #fcfcfc;
                opacity: 0.8;
                border-left-color: #dee2e6;
            }
            .noti-card:hover {
                transform: scale(1.01);
            }
            .icon-box {
                width: 45px;
                height: 45px;
                border-radius: 10px;
                display: flex;
                align-items: center;
                justify-content: center;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h3 class="fw-bold mb-1">Thông báo</h3>
                            <p class="text-muted small">Cập nhật những tin tức mới nhất từ hệ thống</p>
                        </div>
                        <c:if test="${not empty NOTIFICATIONS_LIST}">
                            <a href="MainController?action=markAllRead" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                                Đánh dấu tất cả đã đọc
                            </a>
                        </c:if>
                    </div>

                    <%@include file="message-alert.jsp" %>

                    <div class="d-grid gap-3">
                        <c:forEach var="n" items="${NOTIFICATIONS_LIST}">
                            <div class="card noti-card p-3 shadow-sm ${n.isRead ? 'read' : 'unread'}">
                                <div class="d-flex align-items-start">
                                    <div class="icon-box me-3
                                         ${n.type == 'Promotion' ? 'bg-danger-subtle text-danger' : 
                                           n.type == 'Warning' ? 'bg-warning-subtle text-warning' : 
                                           'bg-primary-subtle text-primary'}">
                                        <i class="bi
                                           ${n.type == 'Promotion' ? 'bi-megaphone' : 
                                             n.type == 'Warning' ? 'bi-exclamation-triangle' : 
                                             'bi-info-circle'} fs-5"></i>
                                    </div>

                                    <div class="flex-grow-1">
                                        <div class="d-flex justify-content-between">
                                            <h6 class="fw-bold mb-1">${n.title}</h6>
                                            <small class="text-muted">
                                                <fmt:formatDate value="${n.createdAt}" pattern="dd/MM, HH:mm"/>
                                            </small>
                                        </div>
                                        <p class="text-secondary small mb-2">${n.content}</p>

                                        <c:if test="${!n.isRead}">
                                            <a href="MainController?action=readNoti&id=${n.id}" class="text-decoration-none small fw-bold">Xem chi tiết</a>
                                        </c:if>
                                    </div>

                                    <a href="MainController?action=deleteNoti&id=${n.id}" class="btn btn-sm text-danger opacity-50" onclick="return confirm('Xóa thông báo này?')">
                                        <i class="bi bi-x-lg"></i>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty NOTIFICATIONS_LIST}">
                            <div class="text-center py-5">
                                <div class="mb-3">
                                    <i class="bi bi-bell-slash text-muted display-1 opacity-25"></i>
                                </div>
                                <h5 class="text-muted">Hộp thư trống</h5>
                                <p class="small text-muted">Bạn chưa có thông báo nào vào lúc này.</p>
                                <a href="MainController?action=home" class="btn btn-primary mt-2">Tiếp tục mua sắm</a>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <div class="mt-4">
                            <c:set var="targetAction" value="viewNotifications" scope="request"/>
                            <%@include file="pagination.jsp" %>
                        </div>
                    </c:if>

                </div>
            </div>
        </div>

        <%@include file="web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
