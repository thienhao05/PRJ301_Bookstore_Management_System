<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Tin Tức - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-style.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                display: flex;
            }
            .main-content {
                margin-left: 250px;
                padding: 30px;
                width: 100%;
                min-height: 100vh;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .text-truncate-2 {
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
                max-width: 300px;
            }
        </style>
    </head>
    <body>

        <%@include file="../components/admin-sidebar.jsp" %>

        <div class="main-content">

            <%-- Header --%>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-0">Quản Lý Tin Tức</h2>
                    <p class="text-muted mb-0">
                        Đăng bài viết mới hoặc cập nhật thông tin cửa hàng
                    </p>
                </div>
                <button class="btn btn-primary shadow-sm px-4"
                        data-bs-toggle="modal"
                        data-bs-target="#addNewsModal">
                    <i class="bi bi-pencil-square me-1"></i>Viết Bài Mới
                </button>
            </div>

            <%@include file="../components/message-alert.jsp" %>

            <%-- Bảng tin tức --%>
            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Tiêu đề bài viết</th>
                                    <th>Nội dung tóm tắt</th>
                                    <th>Ngày đăng</th>
                                    <th>Tác giả</th>
                                    <th class="text-end pe-4">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="n" items="${NEWS_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted">#${n.news_id}</td>

                                        <td>
                                            <strong class="text-dark">${n.title}</strong>
                                        </td>

                                        <td>
                                            <span class="text-truncate-2 text-muted small">
                                                ${n.content}
                                            </span>
                                        </td>

                                        <td>
                                            <div class="small text-muted">
                                                <i class="bi bi-clock me-1"></i>
                                                <fmt:formatDate value="${n.created_at}"
                                                                pattern="dd/MM/yyyy HH:mm"/>
                                            </div>
                                        </td>

                                        <td>
                                            <%-- Highlight nếu là bài của mình --%>
                                            <c:choose>
                                                <c:when test="${n.staff_id == sessionScope.LOGIN_USER.userId}">
                                                    <span class="badge bg-primary-subtle
                                                                 text-primary border
                                                                 border-primary-subtle">
                                                        <i class="bi bi-person-fill me-1"></i>
                                                        Bạn
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-light text-dark border">
                                                        Staff #${n.staff_id}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td class="text-end pe-4">
                                            <%-- Nút sửa: tất cả đều được sửa --%>
                                            <a href="MainController?action=editNews&id=${n.news_id}"
                                               class="btn btn-sm btn-outline-info me-1"
                                               title="Chỉnh sửa bài viết">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <%-- Nút xóa:
                                                 Admin(1) và Manager(2): xóa tất cả
                                                 Staff(3): chỉ xóa bài của mình --%>
                                            <c:choose>
                                                <c:when test="${sessionScope.LOGIN_USER.roleId == 1
                                                            || sessionScope.LOGIN_USER.roleId == 2}">
                                                    <a href="MainController?action=deleteNews&id=${n.news_id}"
                                                       class="btn btn-sm btn-outline-danger"
                                                       title="Xóa bài viết"
                                                       onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:when>
                                                <c:when test="${sessionScope.LOGIN_USER.roleId == 3
                                                            && n.staff_id == sessionScope.LOGIN_USER.userId}">
                                                    <a href="MainController?action=deleteNews&id=${n.news_id}"
                                                       class="btn btn-sm btn-outline-danger"
                                                       title="Xóa bài viết của bạn"
                                                       onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <%-- Staff không phải tác giả: ẩn nút xóa --%>
                                                    <button class="btn btn-sm btn-outline-secondary"
                                                            disabled title="Bạn không có quyền xóa bài này">
                                                        <i class="bi bi-lock"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty NEWS_LIST}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5 text-muted">
                                            <i class="bi bi-journal-x display-4 opacity-25 d-block mb-3"></i>
                                            Chưa có bài viết nào được đăng.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <%-- Modal thêm bài viết mới --%>
        <div class="modal fade" id="addNewsModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg border-0">
                <form action="MainController" method="POST"
                      class="modal-content shadow-lg"
                      accept-charset="UTF-8">
                    <input type="hidden" name="action" value="addNews">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">
                            <i class="bi bi-pencil-square me-2"></i>Đăng Tin Tức Mới
                        </h5>
                        <button type="button" class="btn-close btn-close-white"
                                data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-bold">
                                Tiêu đề bài viết <span class="text-danger">*</span>
                            </label>
                            <input type="text" name="title" class="form-control"
                                   placeholder="Nhập tiêu đề hấp dẫn..." required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold">
                                Nội dung chi tiết <span class="text-danger">*</span>
                            </label>
                            <textarea name="content" class="form-control" rows="10"
                                      placeholder="Viết nội dung bài viết tại đây..."
                                      required></textarea>
                            <div class="form-text text-muted mt-1">
                                <i class="bi bi-info-circle me-1"></i>
                                Bạn có thể dùng thẻ HTML cơ bản để định dạng nội dung.
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary"
                                data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" class="btn btn-primary px-4">
                            <i class="bi bi-send me-1"></i>Đăng Bài Ngay
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>