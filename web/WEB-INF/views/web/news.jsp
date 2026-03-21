<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${NEWS_DETAIL.title} - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body { background-color: #f4f7f6; font-family: 'Segoe UI', sans-serif; }
            .news-content { line-height: 1.9; font-size: 1.05rem; color: #333; }
            .related-card {
                border: none;
                border-radius: 12px;
                transition: 0.3s;
            }
            .related-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            }
        </style>
    </head>
    <body>

        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="row g-4">

                <%-- CỘT TRÁI: Nội dung bài viết --%>
                <div class="col-lg-8">

                    <%-- Breadcrumb --%>
                    <nav aria-label="breadcrumb" class="mb-3">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="MainController?action=home"
                                   class="text-decoration-none">Trang chủ</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="MainController?action=viewNews"
                                   class="text-decoration-none">Tin tức</a>
                            </li>
                            <li class="breadcrumb-item active text-truncate"
                                style="max-width:200px;">
                                ${NEWS_DETAIL.title}
                            </li>
                        </ol>
                    </nav>

                    <div class="card border-0 shadow-sm"
                         style="border-radius:15px; overflow:hidden;">

                        <%-- Thumbnail --%>
                        <div class="bg-primary bg-opacity-10 d-flex align-items-center
                                    justify-content-center"
                             style="height:250px;">
                            <i class="bi bi-newspaper display-1 text-primary opacity-25"></i>
                        </div>

                        <div class="card-body p-4 p-md-5">
                            <%-- Meta info --%>
                            <div class="d-flex align-items-center gap-3 mb-3 text-muted small">
                                <span>
                                    <i class="bi bi-clock me-1"></i>
                                    <fmt:formatDate value="${NEWS_DETAIL.created_at}"
                                                    pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                                <span>
                                    <i class="bi bi-person me-1"></i>
                                    Staff #${NEWS_DETAIL.staff_id}
                                </span>
                            </div>

                            <%-- Tiêu đề --%>
                            <h2 class="fw-bold text-dark mb-4">
                                ${NEWS_DETAIL.title}
                            </h2>

                            <hr class="opacity-25 mb-4">

                            <%-- Nội dung --%>
                            <div class="news-content">
                                ${NEWS_DETAIL.content}
                            </div>

                            <hr class="opacity-25 mt-4">

                            <%-- Nút quay lại --%>
                            <a href="MainController?action=viewNews"
                               class="btn btn-outline-secondary rounded-pill px-4">
                                <i class="bi bi-arrow-left me-2"></i>Quay lại tin tức
                            </a>
                        </div>
                    </div>
                </div>

                <%-- CỘT PHẢI: Tin liên quan --%>
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm p-3"
                         style="border-radius:15px; position:sticky; top:100px;">
                        <h5 class="fw-bold mb-3">
                            <i class="bi bi-bookmark me-2 text-primary"></i>
                            Tin liên quan
                        </h5>

                        <c:forEach var="related" items="${RELATED_NEWS}">
                            <a href="MainController?action=newsDetail&id=${related.news_id}"
                               class="text-decoration-none">
                                <div class="card related-card p-3 mb-3 bg-light">
                                    <div class="fw-bold text-dark small mb-1">
                                        ${related.title}
                                    </div>
                                    <div class="text-muted" style="font-size:11px;">
                                        <i class="bi bi-clock me-1"></i>
                                        <fmt:formatDate value="${related.created_at}"
                                                        pattern="dd/MM/yyyy"/>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>

                        <c:if test="${empty RELATED_NEWS}">
                            <p class="text-muted small text-center py-3">
                                Không có tin liên quan
                            </p>
                        </c:if>

                        <hr class="opacity-25">

                        <%-- Banner mua sách --%>
                        <div class="text-center p-3 rounded-3"
                             style="background:linear-gradient(135deg,#e3f0ff,#cce0ff);">
                            <i class="bi bi-book-half display-5 text-primary mb-2"></i>
                            <p class="fw-bold text-dark small mb-2">
                                Yêu thích đọc sách?
                            </p>
                            <a href="MainController?action=view"
                               class="btn btn-primary btn-sm rounded-pill px-3">
                                Khám phá ngay
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>