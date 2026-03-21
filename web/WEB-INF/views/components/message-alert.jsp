<%-- 
    Document   : message-alert
    Created on : Mar 12, 2026, 4:52:46 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="message-container mt-3 mb-3">

    <c:if test="${not empty sessionScope.MSG_SUCCESS}">
        <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm" role="alert">
            <div class="d-flex align-items-center">
                <i class="bi bi-check-circle-fill fs-5 me-2"></i>
                <div>
                    <strong>Thành công!</strong> ${sessionScope.MSG_SUCCESS}
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <%-- Xóa thông báo sau khi hiển thị để không lặp lại khi F5 --%>
        <c:remove var="MSG_SUCCESS" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.MSG_ERROR}">
        <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm" role="alert">
            <div class="d-flex align-items-center">
                <i class="bi bi-exclamation-octagon-fill fs-5 me-2"></i>
                <div>
                    <strong>Lỗi rồi!</strong> ${sessionScope.MSG_ERROR}
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="MSG_ERROR" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.MSG_WARNING}">
        <div class="alert alert-warning alert-dismissible fade show border-0 shadow-sm" role="alert">
            <div class="d-flex align-items-center">
                <i class="bi bi-exclamation-triangle-fill fs-5 me-2"></i>
                <div>
                    <strong>Lưu ý:</strong> ${sessionScope.MSG_WARNING}
                </div>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="MSG_WARNING" scope="session"/>
    </c:if>

</div>

<script>
    setTimeout(function () {
        let alerts = document.querySelectorAll('.alert');
        alerts.forEach(function (alert) {
            let bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 4000);
</script>
