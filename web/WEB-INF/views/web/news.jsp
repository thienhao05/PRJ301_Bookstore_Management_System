<%-- 
    Document   : news
    Created on : Mar 12, 2026, 4:49:55 AM
    Author     : PC (Hào)
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tin Tức & Sự Kiện - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Segoe UI', sans-serif;
            }
            .order-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
                background: #fff;
                height: 100%;
                display: flex;
                flex-direction: column;
            }
            .order-card:hover {
                transform: translateY(-10px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .news-thumb {
                height: 200px;
                width: 100%;
                object-fit: cover;
                border-radius: 12px 12px 0 0;
            }
            .category-badge {
                position: absolute;
                top: 20px;
                left: 20px;
                font-size: 0.75rem;
                font-weight: 600;
                padding: 5px 12px;
            }
            .line-clamp {
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="bg-primary text-white py-5 mb-5 shadow-sm">
            <div class="container text-center py-3">
                <h1 class="fw-bold display-5">Tin Tức & Blog</h1>
                <p class="lead opacity-75">Cập nhật những xu hướng đọc sách và sự kiện mới nhất tại Hào's Bookstore</p>
            </div>
        </div>

        <div class="container pb-5">
            <div class="row mb-5">
                <div class="col-md-8 mx-auto">
                    <div class="card order-card p-3 shadow-sm border-0">
                        <form action="MainController" method="GET" class="row g-2">
                            <input type="hidden" name="action" value="searchNews">
                            <div class="col-md-8">
                                <div class="input-group">
                                    <span class="input-group-text bg-transparent border-end-0"><i class="bi bi-search"></i></span>
                                    <input type="text" name="query" class="form-control border-start-0" placeholder="Tìm bài viết...">
                                </div>
                            </div>
                            <div class="col-md-4">
                                <button type="submit" class="btn btn-primary w-100 fw-bold">Tìm kiếm</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <c:forEach var="n" items="${NEWS_LIST}">
                    <div class="col-md-6 col-lg-4">
                        <div class="card order-card position-relative">
                            <img src="${not empty n.image_url ? n.image_url : 'https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&q=80&w=800'}" 
                                 class="news-thumb" alt="${n.title}">

                            <span class="badge bg-primary category-badge rounded-pill shadow-sm">${n.category_name}</span>

                            <div class="card-body p-4 d-flex flex-column">
                                <div class="text-muted small mb-2">
                                    <i class="bi bi-calendar3 me-1"></i> <fmt:formatDate value="${n.publish_date}" pattern="dd/MM/yyyy"/>
                                </div>
                                <h5 class="fw-bold text-dark mb-3 line-clamp">${n.title}</h5>
                                <p class="text-secondary small mb-4 line-clamp">
                                    ${n.summary != null ? n.summary : 'Nhấn để đọc thêm chi tiết về bài viết này tại Hào Bookstore...'}
                                </p>
                                <div class="mt-auto pt-3 border-top d-flex justify-content-between align-items-center">
                                    <span class="small text-muted">Bởi <strong>${n.author_name}</strong></span>
                                    <a href="MainController?action=newsDetail&id=${n.news_id}" class="btn btn-outline-primary btn-sm rounded-pill px-3 fw-bold">
                                        Đọc tiếp <i class="bi bi-arrow-right ms-1"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty NEWS_LIST}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-journal-x display-1 text-muted opacity-25"></i>
                        <h4 class="mt-3 text-muted">Chưa có bài viết nào được đăng tải.</h4>
                        <a href="MainController?action=home" class="btn btn-primary mt-3">Quay về trang chủ</a>
                    </div>
                </c:if>
            </div>

            <c:if test="${totalPages > 1}">
                <div class="mt-5">
                    <c:set var="targetAction" value="viewNews" scope="request"/>
                    <%@include file="pagination.jsp" %>
                </div>
            </c:if>
        </div>

        <%@include file="web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>