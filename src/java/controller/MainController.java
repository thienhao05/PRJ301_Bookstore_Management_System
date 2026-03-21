package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String ERROR        = "/WEB-INF/views/web/error-404.jsp";
    private static final String HOME         = "BookController?action=view";
    private static final String USER         = "UserController";
    private static final String BOOK         = "BookController";
    private static final String CATEGORY     = "CategoryController";
    private static final String CART         = "CartController";
    private static final String ORDER        = "OrderController";
    private static final String PAYMENT      = "PaymentController";
    private static final String ADDRESS      = "AddressController";
    private static final String DISCOUNT     = "DiscountController";
    private static final String NEWS         = "NewsController";
    private static final String NOTIFICATION = "NotificationController";
    private static final String STAFF        = "StaffController";
    private static final String SHIFT        = "ShiftController";
    private static final String PUBLISHER    = "PublisherController";
    private static final String REVIEW       = "ReviewController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = ERROR;

        try {
            if (action == null || action.isEmpty()) {
                url = HOME;

            // NHÓM: TÀI KHOẢN
            } else if ("login".equals(action) || "logout".equals(action)
                    || "register".equals(action) || "profile".equals(action)
                    || "updateProfile".equals(action)
                    || "forgotPassword".equals(action) || "resetPassword".equals(action)
                    || "manageUsers".equals(action) || "addUser".equals(action)
                    || "editUser".equals(action) || "deleteUser".equals(action)) {
                mapAction(request, action, "manageUsers", "list");
                url = USER;

            // NHÓM: SÁCH
            } else if ("home".equals(action) || "view".equals(action)
                    || "viewBooks".equals(action) || "search".equals(action)
                    || "detail".equals(action) || "bookDetail".equals(action)
                    || "manageBooks".equals(action) || "addBook".equals(action)
                    || "editBook".equals(action) || "updateBook".equals(action)
                    || "deleteBook".equals(action)) {
                mapAction(request, action, "manageBooks", "list");
                url = BOOK;

            // NHÓM: ĐƠN HÀNG & DASHBOARD
            // ✅ ĐÃ THÊM "placeOrder" vào nhóm này
            } else if ("checkout".equals(action) || "placeOrder".equals(action)
                    || "history".equals(action) || "dashboard".equals(action)
                    || "manageOrders".equals(action) || "manageOrderDetail".equals(action)
                    || "updateStatus".equals(action) || "update_status".equals(action)
                    || "myOrders".equals(action) || "myOrderDetail".equals(action)
                    || "updateOrderStatus".equals(action)) {
                url = ORDER;

            // NHÓM: TIN TỨC
            } else if ("viewNews".equals(action) || "newsDetail".equals(action)
                    || "manageNews".equals(action) || "addNews".equals(action)
                    || "editNews".equals(action) || "updateNews".equals(action)
                    || "deleteNews".equals(action)) {
                mapAction(request, action, "manageNews", "list");
                url = NEWS;

            // NHÓM: GIỎ HÀNG
            } else if (action.contains("Cart") || "addToCart".equals(action)
                    || "removeFromCart".equals(action) || "updateCartQuantity".equals(action)
                    || "viewCart".equals(action) || "clearCart".equals(action)) {
                url = CART;

            // NHÓM: CHUYÊN MỤC
            } else if ("manageCategories".equals(action) || "addCategory".equals(action)
                    || "editCategory".equals(action) || "updateCategory".equals(action)
                    || "deleteCategory".equals(action) || action.contains("Category")) {
                mapAction(request, action, "manageCategories", "list");
                mapAction(request, action, "addCategory",      "add");
                mapAction(request, action, "editCategory",     "edit");
                mapAction(request, action, "updateCategory",   "update");
                mapAction(request, action, "deleteCategory",   "delete");
                url = CATEGORY;

            // NHÓM: KHUYẾN MÃI
            } else if ("manageDiscounts".equals(action) || "addDiscount".equals(action)
                    || "editDiscount".equals(action) || "updateDiscount".equals(action)
                    || "deleteDiscount".equals(action) || action.contains("Discount")) {
                mapAction(request, action, "manageDiscounts", "list");
                mapAction(request, action, "addDiscount",     "add");
                mapAction(request, action, "editDiscount",    "edit");
                mapAction(request, action, "updateDiscount",  "update");
                mapAction(request, action, "deleteDiscount",  "delete");
                url = DISCOUNT;

            // NHÓM: NHÀ XUẤT BẢN
            } else if (action.contains("Publisher")) {
                mapAction(request, action, "managePublishers", "list");
                mapAction(request, action, "addPublisher",     "add");
                mapAction(request, action, "editPublisher",    "edit");
                mapAction(request, action, "updatePublisher",  "update");
                mapAction(request, action, "deletePublisher",  "delete");
                url = PUBLISHER;

            // NHÓM: YÊU THÍCH
            } else if (action.contains("Wishlist") || "viewWishlist".equals(action)
                    || "addWishlist".equals(action) || "removeWishlist".equals(action)) {
                url = "WishlistController";

            // NHÓM: ĐÁNH GIÁ
            } else if (action.contains("Review")) {
                url = REVIEW;

            // NHÓM: NHÂN VIÊN
            } else if ("manageStaffs".equals(action) || "addStaff".equals(action)
                    || "editStaff".equals(action) || "updateStaff".equals(action)
                    || "deleteStaff".equals(action)) {
                url = STAFF;

            // NHÓM: CA TRỰC
            } else if (action.contains("Shift")) {
                mapAction(request, action, "manageShifts", "list");
                mapAction(request, action, "addShift",     "add");
                mapAction(request, action, "editShift",    "edit");
                mapAction(request, action, "updateShift",  "update");
                mapAction(request, action, "deleteShift",  "delete");
                url = SHIFT;

            // NHÓM: THANH TOÁN
            } else if (action.contains("Payment") || "vnpay_return".equals(action)) {
                url = PAYMENT;

            // NHÓM: ĐỊA CHỈ
            } else if (action.contains("Address") || "setDefault".equals(action)
                    || "listAddress".equals(action)) {
                url = ADDRESS;

            // NHÓM: THÔNG BÁO
            } else if (action.contains("Noti") || "sendNotification".equals(action)
                    || "viewNoti".equals(action) || "viewNotifications".equals(action)
                    || "readNoti".equals(action) || "deleteNoti".equals(action)) {
                mapAction(request, action, "sendNotification", "viewSendPage");
                url = NOTIFICATION;
            }

        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private void mapAction(HttpServletRequest request, String action,
                           String actionName, String mappedValue) {
        if (actionName.equals(action)) {
            request.setAttribute("action", mappedValue);
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