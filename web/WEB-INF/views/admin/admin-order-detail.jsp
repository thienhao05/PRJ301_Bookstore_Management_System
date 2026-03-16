<%-- 
    Document   : admin-order-detail
    Created on : Mar 12, 2026, 4:55:12 AM
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
        <title>Chi tiết đơn hàng #${ORDER.order_id} - Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-style.css">
        <style>
            .status-banner {
                padding: 15px 25px;
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 25px;
            }
            .book-img-mini {
                width: 50px;
                height: 70px;
                object-fit: cover;
                border-radius: 5px;
                background: #eee;
            }
        </style>
    </head>
    <body>

        <%@include file="/WEB-INF/views/components/admin-sidebar.jsp" %>

        <div class="main-content">
            <header class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <a href="MainController?action=manageOrders" class="btn btn-sm btn-outline-secondary mb-2">
                        <i class="bi bi-arrow-left"></i> Quay lại danh sách
                    </a>
                    <h2 class="fw-bold text-dark">Chi tiết đơn hàng <span class="text-primary">#${ORDER.order_id}</span></h2>
                </div>
                <div class="user-info text-end">
                    <span class="small text-muted">Admin:</span> <br>
                    <strong>${sessionScope.LOGIN_USER.fullName}</strong>
                </div>
            </header>

            <c:set var="st" value="${ORDER.status}"/>
            <div class="status-banner 
                 ${st == 'Pending' ? 'bg-warning-subtle text-warning-emphasis border border-warning' : 
                   st == 'Processing' ? 'bg-info-subtle text-info-emphasis border border-info' : 
                   st == 'Shipping' ? 'bg-primary-subtle text-primary-emphasis border border-primary' : 
                   st == 'Delivered' ? 'bg-success-subtle text-success-emphasis border border-success' : 'bg-danger-subtle text-danger-emphasis border border-danger'}">
                <div>
                    <i class="bi bi-info-circle-fill me-2"></i>
                    Trạng thái hiện tại: <strong>${st}</strong>
                </div>
                <div class="small">
                    Ngày đặt: <fmt:formatDate value="${ORDER.order_date}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </div>

            <%@include file="/WEB-INF/views/components/message-alert.jsp" %>

            <div class="row g-4">
                <div class="col-lg-4">
                    <div class="admin-card mb-4">
                        <h5 class="fw-bold mb-4 border-bottom pb-2">Thông tin khách hàng</h5>
                        <div class="mb-3">
                            <label class="text-muted small text-uppercase fw-bold">Tên khách hàng</label>
                            <p class="mb-0 fw-bold text-dark">${ORDER.fullName}</p>
                            <p class="small text-muted mb-0">User ID: #${ORDER.user_id}</p>
                        </div>
                        <div class="mb-3">
                            <label class="text-muted small text-uppercase fw-bold">Liên hệ</label>
                            <p class="mb-0"><i class="bi bi-envelope me-2"></i>${ORDER.email}</p>
                            <p class="mb-0"><i class="bi bi-telephone me-2"></i>${ORDER.phone}</p>
                        </div>
                        <div class="mb-0">
                            <label class="text-muted small text-uppercase fw-bold">Địa chỉ giao hàng</label>
                            <p class="small mb-0">${ORDER.shippingAddress}</p>
                        </div>
                    </div>

                    <div class="admin-card">
                        <h5 class="fw-bold mb-4 border-bottom pb-2 text-primary">Cập nhật trạng thái</h5>
                        <form action="MainController" method="POST">
                            <input type="hidden" name="action" value="updateOrderStatus">
                            <input type="hidden" name="orderId" value="${ORDER.order_id}">
                            
                            <div class="mb-4">
                                <select name="newStatus" class="form-select border-primary shadow-none">
                                    <option value="Pending" ${st == 'Pending' ? 'selected' : ''}>⏳ Chờ xử lý</option>
                                    <option value="Processing" ${st == 'Processing' ? 'selected' : ''}>⚙️ Đang đóng gói</option>
                                    <option value="Shipping" ${st == 'Shipping' ? 'selected' : ''}>🚚 Đang giao hàng</option>
                                    <option value="Delivered" ${st == 'Delivered' ? 'selected' : ''}>✅ Đã giao thành công</option>
                                    <option value="Canceled" ${st == 'Canceled' ? 'selected' : ''}>❌ Hủy đơn hàng</option>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Ghi chú cho khách (nếu có)</label>
                                <textarea name="adminNote" class="form-control" rows="3" placeholder="Ví dụ: Đơn hàng đã được bàn giao cho bưu tá..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 fw-bold py-2 shadow-sm">
                                <i class="bi bi-save me-2"></i> Lưu thay đổi
                            </button>
                        </form>
                    </div>
                </div>

                <div class="col-lg-8">
                    <div class="admin-card">
                        <h5 class="fw-bold mb-4 border-bottom pb-2">Danh sách sách trong đơn</h5>
                        <div class="table-responsive">
                            <table class="table align-middle">
                                <thead>
                                    <tr>
                                        <th>Sách</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Đơn giá</th>
                                        <th class="text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${DETAILS}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <div class="book-img-mini me-3 d-flex align-items-center justify-content-center border">
                                                        <i class="bi bi-book text-muted opacity-50"></i>
                                                    </div>
                                                    <div>
                                                        <div class="fw-bold text-dark">${item.bookTitle}</div>
                                                        <small class="text-muted">Mã: #${item.bookId}</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center fw-bold">x${item.quantity}</td>
                                            <td class="text-end">
                                                <fmt:formatNumber value="${item.price}" pattern="#,###"/> đ
                                            </td>
                                            <td class="text-end fw-bold text-primary">
                                                <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/> đ
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr class="table-light">
                                        <td colspan="3" class="text-end fw-bold py-3">TỔNG CỘNG:</td>
                                        <td class="text-end fw-bold py-3 text-danger fs-5">
                                            <fmt:formatNumber value="${ORDER.total_amount}" pattern="#,###"/> đ
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        
                        <div class="mt-4 p-3 bg-light rounded border-start border-4 border-info">
                            <h6 class="fw-bold small text-uppercase mb-2">Ghi chú từ khách hàng:</h6>
                            <p class="small mb-0 text-secondary">
                                ${not empty ORDER.customerNote ? ORDER.customerNote : 'Không có ghi chú.'}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/admin-script.js"></script>
    </body>
</html>