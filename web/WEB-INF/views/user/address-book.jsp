<%-- 
    Document   : address-book
    Created on : Mar 12, 2026, 4:47:41 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Sổ Địa Chỉ - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .address-card {
                border: 2px solid transparent;
                transition: 0.3s;
                border-radius: 12px;
                background: #fff;
            }
            .address-card.default {
                border-color: #0d6efd;
                background-color: #f0f7ff;
            }
            .address-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 10px 20px rgba(0,0,0,0.08);
            }
            .badge-default {
                background-color: #0d6efd;
                font-size: 0.7rem;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-9">

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h3 class="fw-bold mb-1">Sổ địa chỉ của bạn</h3>
                            <p class="text-muted small">Quản lý các địa chỉ nhận hàng để thanh toán nhanh hơn</p>
                        </div>
                        <button class="btn btn-dark shadow-sm px-4" data-bs-toggle="modal" data-bs-target="#addAddressModal">
                            <i class="bi bi-plus-lg me-1"></i> Thêm địa chỉ mới
                        </button>
                    </div>

                    <%@include file="message-alert.jsp" %>

                    <div class="row g-3">
                        <c:forEach var="addr" items="${ADDRESS_LIST}">
                            <div class="col-12">
                                <div class="card address-card p-3 shadow-sm ${addr.isDefault ? 'default' : ''}">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="d-flex align-items-center">
                                            <h6 class="fw-bold mb-0 me-2">${addr.receiverName}</h6>
                                            <c:if test="${addr.isDefault}">
                                                <span class="badge badge-default rounded-pill px-2">
                                                    <i class="bi bi-check-circle-fill me-1"></i>Mặc định
                                                </span>
                                            </c:if>
                                        </div>
                                        <div class="dropdown">
                                            <button class="btn btn-sm btn-light border" type="button" data-bs-toggle="dropdown">
                                                <i class="bi bi-three-dots-vertical"></i>
                                            </button>
                                            <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                                                <li><a class="dropdown-item" href="MainController?action=editAddress&id=${addr.id}"><i class="bi bi-pencil me-2"></i>Chỉnh sửa</a></li>
                                                <c:if test="${!addr.isDefault}">
                                                    <li><a class="dropdown-item text-primary" href="MainController?action=setDefaultAddress&id=${addr.id}"><i class="bi bi-star me-2"></i>Đặt làm mặc định</a></li>
                                                    <li><hr class="dropdown-divider"></li>
                                                    <li><a class="dropdown-item text-danger" href="MainController?action=deleteAddress&id=${addr.id}" onclick="return confirm('Xóa địa chỉ này?')"><i class="bi bi-trash me-2"></i>Xóa</a></li>
                                                </c:if>
                                            </ul>
                                        </div>
                                    </div>

                                    <div class="mt-2 text-secondary small">
                                        <div><i class="bi bi-telephone me-2"></i>${addr.phone}</div>
                                        <div class="mt-1"><i class="bi bi-geo-alt me-2"></i>${addr.addressDetail}</div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty ADDRESS_LIST}">
                            <div class="col-12 text-center py-5">
                                <img src="https://cdn-icons-png.flaticon.com/512/9374/9374944.png" width="100" class="opacity-25 mb-3" alt="Empty">
                                <p class="text-muted">Bạn chưa lưu địa chỉ nào. Hãy thêm một cái nhé!</p>
                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>

        <div class="modal fade" id="addAddressModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form action="MainController" method="POST" class="modal-content border-0 shadow-lg">
                    <input type="hidden" name="action" value="addAddress">
                    <div class="modal-header border-0 pb-0">
                        <h5 class="modal-title fw-bold">Địa chỉ nhận hàng mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-12">
                                <label class="form-label small fw-bold">Họ và tên người nhận</label>
                                <input type="text" name="receiverName" class="form-control" placeholder="Ví dụ: Nguyễn Văn A" required>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label small fw-bold">Số điện thoại</label>
                                <input type="tel" name="phone" class="form-control" placeholder="090xxxxxxx" required>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label small fw-bold">Địa chỉ chi tiết</label>
                                <textarea name="addressDetail" class="form-control" rows="3" placeholder="Số nhà, tên đường, phường/xã..." required></textarea>
                            </div>
                            <div class="col-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="isDefault" value="true" id="flexCheckDefault">
                                    <label class="form-check-label small" for="flexCheckDefault">
                                        Đặt làm địa chỉ mặc định
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer border-0">
                        <button type="button" class="btn btn-light px-4" data-bs-dismiss="modal">Trở lại</button>
                        <button type="submit" class="btn btn-dark px-4">Lưu địa chỉ</button>
                    </div>
                </form>
            </div>
        </div>

        <%@include file="web-footer.jsp" %>
    </body>
</html>