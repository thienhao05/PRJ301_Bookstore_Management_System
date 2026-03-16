<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${NEWS.title} - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body { background-color: #f4f7f6; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .order-card { border: none; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
            .status-badge { padding: 6px 12px; border-radius: 20px; font-weight: 600; font-size: 0.85rem; }
            .btn-back { transition: 0.3s; }
            .btn-back:hover { transform: translateX(-5px); }
            
            /* Style riêng cho nội dung tin tức */
            .news-content { line-height: 1.8; font-size: 1.1rem; color: #333; }
            .news-image { width: 100%; border-radius: 12px; margin-bottom: 25px; object-fit: cover; max-height: 450px; }
            .sidebar-title { border-left: 4px solid #0d6efd; padding-left: 10px; font-weight: bold; }
        </style>
    </head>
    <body>

        <div class="container py-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <a href="MainController?action=viewNews" class="btn btn-outline-secondary btn-sm btn-back mb-2">
                        <i class="bi bi-arrow-left"></i> Quay lại tin tức
                    </a>
                    <h2 class="fw-bold text-dark mb-0">${NEWS.title}</h2>
                    <div class="text-muted mt-2 small">
                        <i class="bi bi-person-circle me-1"></i> Tác giả: <strong>${NEWS.author_name}</strong> | 
                        <i class="bi bi-calendar-event me-1"></i> Ngày đăng: 
                        <fmt:formatDate value="${NEWS.publish_date}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>
                </div>
                
                <span class="status-badge bg-primary text-white">
                    <i class="bi bi-tag-fill me-1"></i> ${NEWS.category_name}
                </span>
            </div>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="MSG_SUCCESS" scope="session"/>
            </c:if>

            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="card order-card">
                        <div class="card-body p-4 p-md-5">
                            <c:if test="${not empty NEWS.image_url}">
                                <img src="${NEWS.image_url}" class="news-image shadow-sm" alt="${NEWS.title}">
                            </c:if>

                            <div class="news-content">
                                ${NEWS.content}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="card order-card mb-4">
                        <div class="card-body p-4">
                            <h5 class="sidebar-title mb-4">Tin liên quan</h5>
                            <div class="list-group list-group-flush">
                                <c:forEach var="related" items="${RELATED_NEWS}">
                                    <a href="MainController?action=newsDetail&id=${related.news_id}" 
                                       class="list-group-item list-group-item-action border-0 px-0 py-3 bg-transparent">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h6 class="mb-1 fw-bold text-dark">${related.title}</h6>
                                        </div>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${related.publish_date}" pattern="dd/MM/yyyy"/>
                                        </small>
                                    </a>
                                </c:forEach>
                                
                                <c:if test="${empty RELATED_NEWS}">
                                    <p class="text-muted small">Không có tin tức liên quan nào.</p>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="card order-card bg-primary text-white">
                        <div class="card-body p-4 text-center">
                            <i class="bi bi-book-half display-4 mb-3"></i>
                            <h5>Yêu thích sách này?</h5>
                            <p class="small opacity-75">Hãy ghé thăm cửa hàng của Hao để tìm những tựa sách mới nhất nhé!</p>
                            <a href="MainController?action=home" class="btn btn-light btn-sm fw-bold">Mua sắm ngay</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>