<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Kho Sách - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            :root {
                --sidebar-width: 250px;
            }
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            /* SIDEBAR STYLE (Khớp với Dashboard) */
            .sidebar {
                width: var(--sidebar-width);
                height: 100vh;
                position: fixed;
                background: #212529;
                color: white;
                padding-top: 20px;
                z-index: 1000;
            }
            .main-content {
                margin-left: var(--sidebar-width);
                padding: 20px;
            }
            .nav-link {
                color: #adb5bd;
                transition: 0.3s;
                padding: 10px 15px;
            }
            .nav-link:hover, .nav-link.active {
                color: white;
                background: #343a40;
                border-radius: 5px;
            }
            /* CARD & TABLE STYLE */
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .table thead {
                background-color: #f1f3f5;
            }
            .badge-stock {
                padding: 6px 12px;
                border-radius: 20px;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <div class="sidebar d-flex flex-column p-3">
            <h4 class="text-center mb-4"><i class="bi bi-book-half"></i> Admin Panel</h4>
            <ul class="nav nav-pills flex-column mb-auto">
                <li class="nav-item">
                    <a href="MainController?action=dashboard" class="nav-link"><i class="bi bi-speedometer2 me-2"></i> Dashboard</a>
                </li>
                <li>
                    <a href="MainController?action=manageBooks" class="nav-link active"><i class="bi bi-journal-text me-2"></i> Quản lý Sách</a>
                </li>
                <li><a href="MainController?action=manageCategories" class="nav-link"><i class="bi bi-tags me-2"></i> Chuyên mục</a></li>
                <li><a href="MainController?action=manageOrders" class="nav-link"><i class="bi bi-cart3 me-2"></i> Đơn hàng</a></li>
                <li><a href="MainController?action=manageDiscounts" class="nav-link"><i class="bi bi-percent me-2"></i> Khuyến mãi</a></li>
                <li><a href="MainController?action=manageNews" class="nav-link"><i class="bi bi-newspaper me-2"></i> Tin tức</a></li>
            </ul>
            <hr>
            <div class="dropdown">
                <a href="MainController?action=logout" class="btn btn-danger w-100"><i class="bi bi-box-arrow-left"></i> Đăng xuất</a>
            </div>
        </div>

        <div class="main-content">
            <header class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold mb-0">Danh Mục Kho Sách</h2>
                    <small class="text-muted">Quản lý nhập xuất và thông tin sách trong kho</small>
                </div>
                <div class="text-end">
                    <c:choose>
                        <c:when test="${not empty sessionScope.LOGIN_USER}">
                            <div class="mb-2">
                                Chào, <strong>${sessionScope.LOGIN_USER.fullName}</strong>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="mb-2">
                                <a href="MainController?action=login">Đăng nhập</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <button class="btn btn-primary shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addBookModal">
                        <i class="bi bi-plus-lg me-1"></i> Nhập Sách Mới
                    </button>
                </div>
            </header>

            <c:if test="${not empty sessionScope.MSG_SUCCESS}">
                <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.MSG_SUCCESS}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="MSG_SUCCESS" scope="session"/>
            </c:if>

            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Thông Tin Sách</th>
                                    <th>Tác Giả</th>
                                    <th>Giá Bán</th>
                                    <th class="text-center">Tồn Kho</th>
                                    <th class="text-center">Loại</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${BOOK_LIST}">
                                    <tr>
                                        <td class="ps-4 text-muted fw-bold">#${book.bookId}</td>
                                        <td>
                                            <div class="fw-bold text-dark">${book.title}</div>
                                            <div class="small text-muted">NXB ID: ${book.publisherId}</div>
                                        </td>
                                        <td>${book.author}</td>
                                        <td class="fw-bold text-primary">
                                            <fmt:formatNumber value="${book.price}" pattern="#,###"/> đ
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${book.stock <= 5}">
                                                    <span class="badge bg-danger text-white border-0 rounded-pill">Sắp hết: ${book.stock}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success text-white border-0 rounded-pill">${book.stock} cuốn</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <span class="badge bg-light text-dark border">CAT-${book.categoryId}</span>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="btn-group shadow-sm">
                                                <a href="MainController?action=editBook&id=${book.bookId}" class="btn btn-sm btn-white text-info border">
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>
                                                <a href="MainController?action=deleteBook&id=${book.bookId}" 
                                                   class="btn btn-sm btn-white text-danger border"
                                                   onclick="return confirm('Bạn có chắc muốn xóa cuốn sách này khỏi kho?')">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty BOOK_LIST}">
                                    <tr>
                                        <td colspan="7" class="text-center py-5 text-muted">Kho hàng đang trống. Hãy nhập thêm sách!</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="addBookModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered border-0">
                <form action="MainController" method="POST" class="modal-content shadow-lg">
                    <input type="hidden" name="action" value="addBook">
                    <div class="modal-header bg-dark text-white">
                        <h5 class="modal-title fw-bold">Nhập Sách Mới</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label class="form-label fw-bold">Tên Sách</label>
                                <input type="text" name="title" class="form-control" placeholder="Tên cuốn sách..." required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Tác Giả</label>
                                <input type="text" name="author" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Giá Bán (VNĐ)</label>
                                <div class="input-group">
                                    <input type="number" name="price" class="form-control" required>
                                    <span class="input-group-text">đ</span>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Số Lượng</label>
                                <input type="number" name="stock" class="form-control" value="10" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Thể Loại</label>
                                <select name="categoryId" class="form-select" required>
                                    <c:forEach var="cat" items="${CATEGORY_LIST}">
                                        <option value="${cat.id}">${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-bold">Mã Nhà Xuất Bản</label>
                                <input type="number" name="publisherId" class="form-control" required>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-dark px-4">Lưu Thông Tin</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card-footer bg-white border-0 pb-4">
            <c:set var="targetAction" value="manageBooks" scope="request"/>
           
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>