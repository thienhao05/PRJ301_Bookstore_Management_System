/* Project: Bookstore Management System - Admin Logic
 Author: Hao (FPTU)
 Description: Handles sidebar toggle, image previews, and confirmation dialogs.
 */

document.addEventListener('DOMContentLoaded', function () {
    "use strict";

    // 1. Sidebar Toggle (Dành cho Mobile/Tablet)
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    // Nếu Hao muốn thêm nút Toggle ở Header Admin
    const toggleBtn = document.querySelector('#sidebarToggle');
    if (toggleBtn) {
        toggleBtn.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }

    // 2. Active Link Auto-Highlight
    // Tự động tô đậm menu bên trái dựa trên URL hiện tại
    const currentUrl = window.location.href;
    const navLinks = document.querySelectorAll('.sidebar .nav-link');

    navLinks.forEach(link => {
        if (currentUrl.includes(link.getAttribute('href'))) {
            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        }
    });

    // 3. Confirm Actions (Xác nhận trước khi Xóa/Khóa)
    // Hao chỉ cần thêm class "btn-confirm" vào các nút Xóa
    const confirmButtons = document.querySelectorAll('.btn-confirm');
    confirmButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            const message = this.getAttribute('data-message') || 'Bạn có chắc chắn muốn thực hiện thao tác này?';
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // 4. Image Preview (Xem trước ảnh bìa sách khi chọn file)
    const imageInput = document.querySelector('#bookImageInput');
    const imagePreview = document.querySelector('#bookImagePreview');

    if (imageInput && imagePreview) {
        imageInput.addEventListener('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imagePreview.src = e.target.result;
                    imagePreview.classList.remove('d-none');
                };
                reader.readAsDataURL(file);
            }
        });
    }

    // 5. Basic Table Search (Tìm kiếm nhanh tại Client)
    const searchInput = document.querySelector('#adminSearchInput');
    const dataTable = document.querySelector('.table tbody');

    if (searchInput && dataTable) {
        searchInput.addEventListener('keyup', function () {
            const value = this.value.toLowerCase();
            const rows = dataTable.querySelectorAll('tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(value) ? '' : 'none';
            });
        });
    }
});

// 6. Toast Notification Helper (Nếu Hao muốn dùng Toast của Bootstrap)
function showAdminToast(message, type = 'success') {
    // Logic hiển thị thông báo nhanh ở góc màn hình
    console.log(`[${type.toUpperCase()}]: ${message}`);
}
