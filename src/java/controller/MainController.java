package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    // Danh sách các Controller đích (Destination)
    private static final String ERROR = "error-404.jsp";
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String url = ERROR; // Mặc định nếu không khớp action nào sẽ về trang lỗi

        try {
            if (action == null || action.isEmpty()) {
                url = HOME;
            } // 1. NHÓM TÀI KHOẢN (USER)
            else if ("login".equals(action) || "logout".equals(action) || "register".equals(action) || "profile".equals(action)) {
                url = USER;
            } // 2. NHÓM SÁCH & DANH MỤC
            else if ("viewBooks".equals(action) || "search".equals(action) || "detail".equals(action)) {
                url = BOOK;
            } else if ("manageCategories".equals(action) || "addCategory".equals(action) || "editCategory".equals(action)) {
                url = CATEGORY;
            } // 3. NHÓM GIỎ HÀNG (CART)
            else if ("viewCart".equals(action) || "addCart".equals(action) || "updateCart".equals(action) || "removeCart".equals(action)) {
                url = CART;
            } // 4. NHÓM ĐƠN HÀNG & THANH TOÁN (ORDER & PAYMENT)
            else if ("checkout".equals(action) || "history".equals(action) || "manageOrders".equals(action)) {
                url = ORDER;
            } else if ("processPayment".equals(action) || "vnpay_return".equals(action)) {
                url = PAYMENT;
            } // 5. NHÓM ĐỊA CHỈ & GIẢM GIÁ (ADDRESS & DISCOUNT)
            else if ("viewAddress".equals(action) || "addAddress".equals(action) || "setDefault".equals(action)) {
                url = ADDRESS;
            } else if ("manageDiscounts".equals(action) || "applyDiscount".equals(action)) {
                url = DISCOUNT;
            } // 6. NHÓM TIN TỨC & THÔNG BÁO (NEWS & NOTI)
            else if ("viewNews".equals(action) || "manageNews".equals(action)) {
                url = NEWS;
            } else if ("viewNoti".equals(action) || "markRead".equals(action)) {
                url = NOTIFICATION;
            }

        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            // Chuyển tiếp yêu cầu (Forward) kèm theo toàn bộ tham số ban đầu
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
