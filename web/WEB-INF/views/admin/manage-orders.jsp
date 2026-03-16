<%-- 
    Document   : manage-orders
    Created on : Mar 12, 2026, 4:45:10 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản Lý Đơn Hàng - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', sans-serif;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }
            .status-badge {
                padding: 5px 12px;
                border-radius: 20px;
                font-weight: 500;
                font-size: 0.85rem;
            }
            .table-hover tbody tr:hover {
                background-color: #f1f3f5;
                transition: 0.2s;
            }
        </style>
    </head>
    <body>

        <div class="container-fluid py-4 px-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold text-dark mb-0">Quản Lý Đơn Hàng</h2>
                    <p class="text-muted">Theo dõi và cập nhật trạng thái đơn hàng của khách</p>
                </div>
                <div>
                    <a href="MainController?action=dashboard" class="btn btn-outline-secondary shadow-sm">
                        <i class="bi bi-speedometer2 me-1"></i> Dashboard
                    </a>
                </div>
            </div>

            <div class="card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">Mã Đơn</th>
                                    <th>Khách Hàng (ID)</th>
                                    <th>Ngày Đặt</th>
                                    <th>Tổng Tiền</th>
                                    <th class="text-center">Trạng Thái</th>
                                    <th class="text-end pe-4">Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${ORDER_LIST}">
                                    <tr>
                                        <td class="ps-4 fw-bold">#${o.order_id}</td>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <div class="avatar-sm bg-light text-primary rounded-circle p-2 me-2">
                                                    <i class="bi bi-person"></i>
                                                </div>
                                                <span>User ID: ${o.user_id}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${o.order_date}" pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td class="fw-bold text-dark">
                                            <fmt:formatNumber value="${o.total_amount}" pattern="#,###"/> đ
                                        </td>
                                        <td class="text-center">
                                            <c:set var="st" value="${o.status}"/>
                                            <span class="status-badge
                                                  ${st == 'Pending' ? 'bg-warning-subtle text-warning border border-warning' : 
                                                    st == 'Processing' ? 'bg-info-subtle text-info border border-info' : 
                                                    st == 'Shipping' ? 'bg-primary-subtle text-primary border border-primary' : 
                                                    st == 'Delivered' ? 'bg-success-subtle text-success border border-success' : 
                                                    'bg-danger-subtle text-danger border border-danger'}">
                                                      ${st}
                                                  </span>
                                            </td>
                                            <td class="text-end pe-4">
                                                <a href="MainController?action=manageOrderDetail&id=${o.order_id}" 
                                                   class="btn btn-sm btn-primary shadow-sm">
                                                    <i class="bi bi-eye me-1"></i> Xem chi tiết
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty ORDER_LIST}">
                                        <tr>
                                            <td colspan="6" class="text-center py-5 text-muted">
                                                Chưa có đơn hàng nào trong hệ thống.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>
    </html>