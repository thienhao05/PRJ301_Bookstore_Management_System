<%-- 
    Document   : admin-footer
    Created on : Mar 12, 2026, 4:52:02 AM
    Author     : PC (Hào)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<footer class="footer mt-auto py-4 bg-white border-top">
    <div class="container-fluid px-4">
        <div class="d-flex align-items-center justify-content-between small">
            <div class="text-muted">
                Copyright &copy; **Hào's Bookstore 2026** <span class="mx-2">|</span> 
                <span class="badge bg-light text-dark border">Version 1.0.4 - Stable</span>
            </div>
            <div>
                <a href="#" class="text-decoration-none text-muted me-3">Chính sách bảo mật</a>
                <a href="#" class="text-decoration-none text-muted">Điều khoản sử dụng</a>
            </div>
        </div>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Tự động đóng các Alert sau 5 giây để không choáng chỗ giao diện
    window.setTimeout(function () {
        var alerts = document.querySelectorAll('.alert-dismissible');
        alerts.forEach(function (alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
</script>