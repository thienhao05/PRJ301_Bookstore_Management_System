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
            body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
            .noti-card {
                border: none;
                border-radius: 12px;
                transition: 0.3s;
                border-left: 5px solid transparent;
            }
            .noti-card.unread {
                background-color: #fff;
                border-left-color: #0d6efd;
                box-shadow: 0 4px 12px rgba(13,110,253,0.08);
            }
            .noti-card.read {
                background-color: #fcfcfc;
                opacity: 0.8;
                border-left-color: #dee2e6;
            }
            .noti-card:hover { transform: scale(1.01); }
            .icon-box {
                width: 45px; height: 45px;
                border-radius: 10px;
                display: flex; align-items: center; justify-content: center;
                flex-shrink: 0;
            }
        </style>
    </head>
    <body>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h3 class="fw-bold mb-1">
                                <i class="bi bi-bell me-2 text-primary"></i>Thông báo
                            </h3>
                            <p class="text-muted small">Cập nhật những tin tức mới nhất từ hệ thống</p>
                        </div>
                    </div>

                    <%-- ✅ SỬA: Đường dẫn include đúng --%>
                    <%@include file="../components/message-alert.jsp" %>

                    <div class="d-grid gap-3">
                        <%-- ✅ SỬA: Dùng NOTI_LIST cho khớp với controller --%>
                        <c:forEach var="n" items="${NOTI_LIST}">
                            <div class="card noti-card p-3 shadow-sm ${n.is_read ? 'read' : 'unread'}">
                                <div class="d-flex align-items-start">

                                    <div class="icon-box me-3 bg-primary-subtle text-primary">
                                        <i class="bi bi-bell fs-5"></i>
                                    </div>

                                    <div class="flex-grow-1">
                                        <div class="d-flex justify-content-between">
                                            <h6 class="fw-bold mb-1">Thông báo hệ thống</h6>
                                            <small class="text-muted">
                                                <%-- ✅ SỬA: Dùng type="both" cho Timestamp --%>
                                                <fmt:formatDate value="${n.created_at}"
                                                                type="both"
                                                                dateStyle="short"
                                                                timeStyle="short"/>
                                            </small>
                                        </div>
                                        <p class="text-secondary small mb-2">${n.content}</p>

                                        <c:if test="${!n.is_read}">
                                            <a href="MainController?action=readNoti&id=${n.notification_id}"
                                               class="text-decoration-none small fw-bold text-primary">
                                                Đánh dấu đã đọc
                                            </a>
                                        </c:if>
                                    </div>

                                    <a href="MainController?action=deleteNoti&id=${n.notification_id}"
                                       class="btn btn-sm text-danger opacity-50 ms-2"
                                       onclick="return confirm('Xóa thông báo này?')">
                                        <i class="bi bi-x-lg"></i>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty NOTI_LIST}">
                            <div class="text-center py-5">
                                <div class="mb-3">
                                    <i class="bi bi-bell-slash text-muted display-1 opacity-25"></i>
                                </div>
                                <h5 class="text-muted">Hộp thư trống</h5>
                                <p class="small text-muted">Bạn chưa có thông báo nào vào lúc này.</p>
                                <a href="MainController?action=home"
                                   class="btn btn-primary mt-2 rounded-pill px-4">
                                    Tiếp tục mua sắm
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <%-- ✅ SỬA: Đường dẫn include đúng --%>
        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
