package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    // 1. Khai báo hằng số để dễ quản lý tên Controller
    // Trang 404 nằm trong WEB-INF nên phải forward đúng path
    private static final String ERROR = "/WEB-INF/views/web/error-404.jsp";
    // Trang home cho khách -> BookController action=view
    private static final String HOME = "BookController?action=view";

    private static final String USER = "UserController";
    private static final String BOOK = "BookController";
    private static final String CATEGORY = "CategoryController";
    private static final String CART = "CartController";
    private static final String ORDER = "OrderController";
    private static final String PAYMENT = "PaymentController";
    private static final String ADDRESS = "AddressController";
    private static final String DISCOUNT = "DiscountController";
    private static final String NEWS = "NewsController";
    private static final String NOTIFICATION = "NotificationController";
    private static final String STAFF = "StaffController";
    private static final String SHIFT = "ShiftController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = ERROR;

        try {
            // Trường hợp không có action -> Về trang chủ
            if (action == null || action.isEmpty()) {
                url = HOME;
            } // 2. PHÂN LUỒNG THEO NHÓM (DOMAIN)
            // NHÓM A: TÀI KHOẢN & NGƯỜI DÙNG
            else if ("login".equals(action) || "logout".equals(action)
                    || "register".equals(action) || "profile".equals(action)) {
                url = USER;
            } // NHÓM B: SÁCH & KHO (Gộp chung cả khách xem và Admin quản lý)
            else if ("home".equals(action)
                    || "viewBooks".equals(action) || "search".equals(action) || "detail".equals(action)
                    || "manageBooks".equals(action) || "addBook".equals(action)
                    || "editBook".equals(action) || "deleteBook".equals(action)) {
                url = BOOK;
            } // NHÓM C: ĐƠN HÀNG (Checkout, Lịch sử, Quản lý đơn, Thống kê Dashboard)
            else if ("checkout".equals(action) || "history".equals(action) || "dashboard".equals(action)
                    || "manageOrders".equals(action) || "manageOrderDetail".equals(action)
                    || "updateStatus".equals(action) || "update_status".equals(action)
                    || "updateOrderStatus".equals(action)) {   // <-- THÊM DÒNG NÀY
                url = ORDER;
            } // NHÓM D: TIN TỨC
            else if ("viewNews".equals(action) || "manageNews".equals(action) || "addNews".equals(action)
                    || "editNews".equals(action) || "updateNews".equals(action) || "deleteNews".equals(action)) {
                url = NEWS;
            } // NHÓM E: CÁC CHỨC NĂNG KHÁC (Giỏ hàng, Thanh toán, Địa chỉ...)
            else if (action.contains("Cart")) {
                url = CART;
            } else if (action.contains("Category")) {
                url = CATEGORY;
            } else if (action.contains("Payment") || "vnpay_return".equals(action)) {
                url = PAYMENT;
            } else if (action.contains("Address") || "setDefault".equals(action)) {
                url = ADDRESS;
            } else if (action.contains("Discount")) {
                url = DISCOUNT;
            } else if (action.contains("Noti")) {
                url = NOTIFICATION;
            } else if (action.contains("Publisher")) {
                url = "PublisherController";
            } else if (action.contains("Review")) {
                url = "ReviewController";
            } else if (action.contains("Shift")) {
                url = "ShiftController";
            } else if (action.contains("Shift")) {
                url = "ShiftController";
            } else if ("manageStaffs".equals(action) || "addStaff".equals(action)
                    || "editStaff".equals(action) || "updateStaff".equals(action)
                    || "deleteStaff".equals(action)) {

                url = STAFF; // Trong đó STAFF = "StaffController"
            } // Giữ lại nhóm ca trực nếu bạn tách riêng ShiftController
            else if (action != null && action.contains("Shift")) {
                url = SHIFT;
            } else if ("manageUsers".equals(action) || "addUser".equals(action)
                    || "editUser".equals(action) || "deleteUser".equals(action)) {
                url = USER;
            } else if ("sendNotification".equals(action) || "viewNoti".equals(action)) {
                url = NOTIFICATION;
            } else if (action.contains("Address")) {
                url = "AddressController";
            }

        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            // Luôn dùng Forward để giữ URL đẹp và chuyển tiếp tham số
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
