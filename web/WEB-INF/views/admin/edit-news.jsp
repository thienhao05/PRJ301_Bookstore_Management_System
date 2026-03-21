<%-- 
    Document   : edit-news
    Created on : Mar 16, 2026, 1:46:23 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh Sửa Bài Viết - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .edit-card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            }
            .form-label {
                font-weight: 600;
                color: #495057;
            }
        </style>
    </head>
    <body>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <a href="MainController?action=manageNews" class="btn btn-link text-decoration-none mb-3 p-0">
                        <i class="bi bi-arrow-left"></i> Quay lại danh sách tin tức
                    </a>

                    <div class="card edit-card">
                        <div class="card-header bg-white border-0 pt-4 px-4">
                            <h3 class="fw-bold text-primary mb-0">Chỉnh Sửa Bài Viết</h3>
                            <p class="text-muted small">ID bài viết: #${NEWS_DETAIL.news_id}</p>
                        </div>

                        <div class="card-body p-4">
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="updateNews">
                                <input type="hidden" name="id" value="${NEWS_DETAIL.news_id}">

                                <div class="mb-4">
                                    <label class="form-label">Tiêu đề bài viết</label>
                                    <input type="text" name="title" class="form-control form-control-lg" 
                                           value="${NEWS_DETAIL.title}" required>
                                </div>

                                <div class="mb-4">
                                    <label class="form-label">Nội dung chi tiết</label>
                                    <textarea name="content" class="form-control" rows="12" required>${NEWS_DETAIL.content}</textarea>
                                    <div class="form-text mt-2">
                                        <i class="bi bi-info-circle me-1"></i> Tác giả gốc (Staff ID): ${NEWS_DETAIL.staff_id}
                                    </div>
                                </div>

                                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                    <a href="MainController?action=manageNews" class="btn btn-light px-4 me-md-2">Hủy bỏ</a>
                                    <button type="submit" class="btn btn-primary px-5 shadow-sm">
                                        <i class="bi bi-save me-2"></i> Lưu thay đổi
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>