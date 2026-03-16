<%-- 
    Document   : web-footer
    Created on : Mar 12, 2026, 4:52:24 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<footer class="bg-dark text-white pt-5 pb-4 mt-5">
    <div class="container text-center text-md-start">
        <div class="row text-center text-md-start">

            <div class="col-md-3 col-lg-3 col-xl-3 mx-auto mt-3">
                <h5 class="text-uppercase mb-4 font-weight-bold text-warning">
                    <i class="bi bi-book-half me-2"></i>Hào's Bookstore
                </h5>
                <p class="small text-secondary">
                    Nơi kết nối những tâm hồn yêu sách. Chúng tôi cung cấp hàng ngàn đầu sách đa dạng từ văn học, kinh tế đến kỹ thuật công nghệ với chất lượng tốt nhất.
                </p>
            </div>

            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mt-3">
                <h6 class="text-uppercase mb-4 font-weight-bold text-white">Khám phá</h6>
                <p><a href="MainController?action=category&id=1" class="text-secondary text-decoration-none small">Sách Văn Học</a></p>
                <p><a href="MainController?action=category&id=2" class="text-secondary text-decoration-none small">Kinh Tế - Kỹ Năng</a></p>
                <p><a href="MainController?action=category&id=3" class="text-secondary text-decoration-none small">Sách CNTT (FPT Poly)</a></p>
                <p><a href="MainController?action=category&id=4" class="text-secondary text-decoration-none small">Ngoại Ngữ</a></p>
            </div>

            <div class="col-md-3 col-lg-2 col-xl-2 mx-auto mt-3">
                <h6 class="text-uppercase mb-4 font-weight-bold text-white">Hỗ trợ</h6>
                <p><a href="#" class="text-secondary text-decoration-none small">Tài khoản của bạn</a></p>
                <p><a href="#" class="text-secondary text-decoration-none small">Chính sách đổi trả</a></p>
                <p><a href="#" class="text-secondary text-decoration-none small">Phí vận chuyển</a></p>
                <p><a href="#" class="text-secondary text-decoration-none small">Hướng dẫn mua hàng</a></p>
            </div>

            <div class="col-md-4 col-lg-3 col-xl-3 mx-auto mt-3">
                <h6 class="text-uppercase mb-4 font-weight-bold text-white">Liên hệ</h6>
                <p class="small text-secondary"><i class="bi bi-house-door-fill me-2"></i> Lô E2a-7, Hiệp Phú, TP. Thủ Đức (FPTU)</p>
                <p class="small text-secondary"><i class="bi bi-envelope-fill me-2"></i> support@haobookstore.vn</p>
                <p class="small text-secondary"><i class="bi bi-telephone-fill me-2"></i> +84 123 456 789</p>
                <p class="small text-secondary"><i class="bi bi-printer-fill me-2"></i> 028 7300 5588</p>
            </div>
        </div>

        <hr class="mb-4 mt-4 bg-secondary">

        <div class="row align-items-center">
            <div class="col-md-7 col-lg-8 text-center text-md-start">
                <p class="small mb-0 text-secondary">
                    © 2026 Copyright: <strong class="text-warning">Thien Hao - FPT University</strong>
                </p>
            </div>

            <div class="col-md-5 col-lg-4">
                <div class="text-center text-md-end">
                    <ul class="list-unstyled list-inline mb-0">
                        <li class="list-inline-item">
                            <a href="#" class="btn-floating btn-sm text-white" style="font-size: 23px;"><i class="bi bi-facebook"></i></a>
                        </li>
                        <li class="list-inline-item">
                            <a href="#" class="btn-floating btn-sm text-white" style="font-size: 23px;"><i class="bi bi-twitter-x"></i></a>
                        </li>
                        <li class="list-inline-item">
                            <a href="#" class="btn-floating btn-sm text-white" style="font-size: 23px;"><i class="bi bi-google"></i></a>
                        </li>
                        <li class="list-inline-item">
                            <a href="#" class="btn-floating btn-sm text-white" style="font-size: 23px;"><i class="bi bi-youtube"></i></a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
