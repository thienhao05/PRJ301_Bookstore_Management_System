<%-- 
    Document   : admin-order-detail
    Created on : Mar 16, 2026, 12:40:39 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi Tiết Đơn Hàng #${ORDER.order_id}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Chi Tiết Đơn Hàng <span class="text-primary">#${ORDER.order_id}</span></h2>
                <a href="MainController?action=manageOrders" class="btn btn-secondary">Quay lại danh sách</a>
            </div>

            <div class="row">
                <div class="col-md-4">
                    <div class="card mb-4 shadow-sm">
                        <div class="card-header bg-dark text-white">Thông tin đơn hàng</div>
                        <div class="card-body">
                            <p><strong>Khách hàng ID:</strong> ${ORDER.user_id}</p>
                            <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${ORDER.order_date}" pattern="dd/MM/yyyy HH:mm"/></p>
                            <p><strong>Tổng tiền:</strong> <span class="text-danger fw-bold"><fmt:formatNumber value="${ORDER.total_amount}" type="currency" currencySymbol="đ"/></span></p>
                            <hr>

                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="orderId" value="${ORDER.order_id}">
                                <label class="form-label"><strong>Trạng thái:</strong></label>
                                <select name="status" class="form-select mb-3">
                                    <option value="Pending" ${ORDER.status == 'Pending' ? 'selected' : ''}>Chờ xử lý</option>
                                    <option value="Processing" ${ORDER.status == 'Processing' ? 'selected' : ''}>Đang chuẩn bị</option>
                                    <option value="Shipping" ${ORDER.status == 'Shipping' ? 'selected' : ''}>Đang giao</option>
                                    <option value="Delivered" ${ORDER.status == 'Delivered' ? 'selected' : ''}>Đã giao</option>
                                    <option value="Canceled" ${ORDER.status == 'Canceled' ? 'selected' : ''}>Đã hủy</option>
                                </select>
                                <button type="submit" class="btn btn-primary w-100">Cập nhật trạng thái</button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-8">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">Sản phẩm đã đặt</div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Sách ID</th>
                                        <th>Tên sách</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Đơn giá</th>
                                        <th class="text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${DETAILS}">
                                        <tr>
                                            <td>${item.book_id}</td>
                                            <td><strong>${item.book_title}</strong></td> <td class="text-center">${item.quantity}</td>
                                            <td class="text-end"><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="đ"/></td>
                                            <td class="text-end fw-bold">
                                                <fmt:formatNumber value="${item.price * item.quantity}" type="currency" currencySymbol="đ"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>