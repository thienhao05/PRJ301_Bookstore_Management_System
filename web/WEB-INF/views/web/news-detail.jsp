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

            .news-content {
                line-height: 1.9;
                font-size: 1.05rem;
                color: #333;
            }
            .news-content p { margin-bottom: 1.2rem; }

            .related-card {
                border: none;
                border-radius: 12px;
                transition: 0.3s;
                background: #f8f9fa;
            }
            .related-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.08);
                background: #fff;
            }

            .news-thumb {
                height: 280px;
                background: linear-gradient(135deg, #e3f0ff 0%, #cce0ff 100%);
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .tag-badge {
                font-size: 0.75rem;
                padding: 5px 12px;
                border-radius: 20px;
            }
        </style>
    </head>
    <body>

        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">
            <div class="row g-4">

                <%-- ========== CỘT TRÁI: Nội dung bài viết ========== --%>
                <div class="col-lg-8">

                    <%-- Breadcrumb --%>
                    <nav aria-label="breadcrumb" class="mb-4">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="MainController?action=home"
                                   class="text-decoration-none">
                                    <i class="bi bi-house me-1"></i>Trang chủ
                                </a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="MainController?action=viewNews"
                                   class="text-decoration-none">Tin tức</a>
                            </li>
                            <li class="breadcrumb-item active text-truncate"
                                style="max-width: 250px;">
                                ${NEWS_DETAIL.title}
                            </li>
                        </ol>
                    </nav>

                    <div class="card border-0 shadow-sm"
                         style="border-radius: 20px; overflow: hidden;">

                        <%-- Thumbnail --%>
                        <div class="news-thumb">
                            <i class="bi bi-newspaper"
                               style="font-size: 5rem; color: #0d6efd; opacity: 0.2;"></i>
                        </div>

                        <div class="card-body p-4 p-md-5">

                            <%-- Meta info --%>
                            <div class="d-flex flex-wrap align-items-center
                                        gap-3 mb-3 text-muted small">
                                <span>
                                    <i class="bi bi-clock me-1 text-primary"></i>
                                    <fmt:formatDate value="${NEWS_DETAIL.created_at}"
                                                    pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                                <span>
                                    <i class="bi bi-person-circle me-1 text-primary"></i>
                                    Staff #${NEWS_DETAIL.staff_id}
                                </span>
                                <span class="badge bg-primary-subtle text-primary
                                             tag-badge border border-primary-subtle">
                                    <i class="bi bi-newspaper me-1"></i>Tin tức
                                </span>
                            </div>

                            <%-- Tiêu đề --%>
                            <h2 class="fw-bold text-dark mb-4"
                                style="line-height: 1.4;">
                                ${NEWS_DETAIL.title}
                            </h2>

                            <hr class="opacity-25 mb-4">

                            <%-- Nội dung bài viết --%>
                            <div class="news-content">
                                ${NEWS_DETAIL.content}
                            </div>

                            <hr class="opacity-25 mt-5 mb-4">

                            <%-- Footer bài viết --%>
                            <div class="d-flex justify-content-between
                                        align-items-center flex-wrap gap-3">
                                <a href="MainController?action=viewNews"
                                   class="btn btn-outline-secondary rounded-pill px-4">
                                    <i class="bi bi-arrow-left me-2"></i>
                                    Quay lại tin tức
                                </a>
                                <div class="d-flex gap-2">
                                    <span class="text-muted small align-self-center">
                                        Chia sẻ:
                                    </span>
                                    <a href="#"
                                       class="btn btn-sm btn-outline-primary rounded-circle"
                                       style="width:34px;height:34px;
                                              display:flex;align-items:center;
                                              justify-content:center;">
                                        <i class="bi bi-facebook"></i>
                                    </a>
                                    <a href="#"
                                       class="btn btn-sm btn-outline-info rounded-circle"
                                       style="width:34px;height:34px;
                                              display:flex;align-items:center;
                                              justify-content:center;">
                                        <i class="bi bi-twitter-x"></i>
                                    </a>
                                    <a href="#"
                                       class="btn btn-sm btn-outline-success rounded-circle"
                                       style="width:34px;height:34px;
                                              display:flex;align-items:center;
                                              justify-content:center;">
                                        <i class="bi bi-whatsapp"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- ========== CỘT PHẢI: Sidebar ========== --%>
                <div class="col-lg-4">
                    <div style="position: sticky; top: 100px;">

                        <%-- Tin liên quan --%>
                        <div class="card border-0 shadow-sm p-3 mb-4"
                             style="border-radius: 15px;">
                            <h5 class="fw-bold mb-3 pb-2 border-bottom">
                                <i class="bi bi-bookmark-fill me-2 text-primary"></i>
                                Tin liên quan
                            </h5>

                            <c:forEach var="related" items="${RELATED_NEWS}">
                                <a href="MainController?action=newsDetail&id=${related.news_id}"
                                   class="text-decoration-none">
                                    <div class="card related-card p-3 mb-2">
                                        <div class="fw-bold text-dark small mb-1"
                                             style="display:-webkit-box;
                                                    -webkit-line-clamp:2;
                                                    -webkit-box-orient:vertical;
                                                    overflow:hidden;">
                                            ${related.title}
                                        </div>
                                        <div class="text-muted" style="font-size: 11px;">
                                            <i class="bi bi-clock me-1"></i>
                                            <fmt:formatDate value="${related.created_at}"
                                                            pattern="dd/MM/yyyy"/>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>

                            <c:if test="${empty RELATED_NEWS}">
                                <p class="text-muted small text-center py-3 mb-0">
                                    <i class="bi bi-inbox me-2"></i>
                                    Không có tin liên quan
                                </p>
                            </c:if>
                        </div>

                        <%-- Banner mua sách --%>
                        <div class="card border-0 shadow-sm text-center p-4"
                             style="border-radius:15px;
                                    background:linear-gradient(135deg,#0d6efd,#003da5);">
                            <i class="bi bi-book-half display-4 text-white opacity-75 mb-3"></i>
                            <h6 class="fw-bold text-white mb-2">
                                Yêu thích đọc sách?
                            </h6>
                            <p class="text-white opacity-75 small mb-3">
                                Khám phá hàng ngàn đầu sách tại cửa hàng của chúng tôi
                            </p>
                            <a href="MainController?action=view"
                               class="btn btn-light rounded-pill px-4 fw-bold">
                                <i class="bi bi-shop me-2"></i>Mua sắm ngay
                            </a>
                        </div>

                        <%-- Banner đăng nhập nếu chưa login --%>
                        <c:if test="${empty sessionScope.LOGIN_USER}">
                            <div class="card border-0 shadow-sm text-center p-4 mt-4"
                                 style="border-radius:15px;">
                                <i class="bi bi-person-circle display-5 text-primary mb-3"></i>
                                <h6 class="fw-bold mb-2">Tham gia cùng chúng tôi!</h6>
                                <p class="text-muted small mb-3">
                                    Đăng ký để nhận ưu đãi và theo dõi đơn hàng
                                </p>
                                <div class="d-grid gap-2">
                                    <a href="MainController?action=register"
                                       class="btn btn-primary btn-sm rounded-pill">
                                        <i class="bi bi-person-plus me-1"></i>Đăng ký
                                    </a>
                                    <a href="MainController?action=login"
                                       class="btn btn-outline-primary btn-sm rounded-pill">
                                        <i class="bi bi-box-arrow-in-right me-1"></i>
                                        Đăng nhập
                                    </a>
                                </div>
                            </div>
                        </c:if>

                    </div>
                </div>

            </div>
        </div>

        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>