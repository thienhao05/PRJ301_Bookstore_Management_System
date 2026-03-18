package controller;

import dao.BookDAO;
import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.CartItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.UserDTO;
import dto.BookDTO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    // ---- Khai báo đường dẫn JSP ----
    private static final String DASHBOARD     = "/WEB-INF/views/admin/dashboard.jsp";
    private static final String MANAGE_ORDERS = "/WEB-INF/views/admin/manage-orders.jsp";
    private static final String ORDER_DETAIL  = "/WEB-INF/views/admin/admin-order-detail.jsp";
    private static final String MY_ORDERS     = "/WEB-INF/views/user/my-orders.jsp";
    private static final String MY_ORDER_DETAIL = "/WEB-INF/views/user/my-order-detail.jsp";
    private static final String ERROR_500     = "/WEB-INF/views/web/error-500.jsp";
    private static final String LOGIN_URL     = "MainController?action=login";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        OrderDAO orderDao     = new OrderDAO();
        OrderDetailDAO detailDao = new OrderDetailDAO();
        CartItemDAO cartItemDao  = new CartItemDAO();
        BookDAO bookDao          = new BookDAO();
        HttpSession session      = request.getSession();

        // Kiểm tra đăng nhập
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            response.sendRedirect(LOGIN_URL);
            return;
        }

        try {
            switch (action) {

                // ================================================
                // 1. DASHBOARD
                // ================================================
                case "dashboard": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect("MainController?action=home");
                        return;
                    }
                    Map<String, Object> stats = orderDao.getDashboardStats();
                    request.setAttribute("TOTAL_REVENUE", stats.get("TOTAL_REVENUE"));
                    request.setAttribute("TOTAL_ORDERS",  stats.get("TOTAL_ORDERS"));
                    request.setAttribute("TOTAL_USERS",   stats.get("TOTAL_USERS"));
                    request.setAttribute("TOTAL_BOOKS",   stats.get("TOTAL_BOOKS"));
                    request.setAttribute("RECENT_ORDERS", stats.get("RECENT_ORDERS"));
                    request.getRequestDispatcher(DASHBOARD).forward(request, response);
                    break;
                }

                // ================================================
                // 2. QUẢN LÝ ĐƠN HÀNG (Admin/Manager/Staff)
                // ================================================
                case "manageOrders": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    List<OrderDTO> allOrders = orderDao.readAll();
                    request.setAttribute("ORDER_LIST", allOrders);
                    request.getRequestDispatcher(MANAGE_ORDERS).forward(request, response);
                    break;
                }

                // ================================================
                // 3. CHI TIẾT ĐƠN HÀNG (Admin/Manager/Staff)
                // ================================================
                case "manageOrderDetail": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    int orderId = Integer.parseInt(request.getParameter("id"));
                    OrderDTO order = orderDao.readById(orderId);
                    List<OrderDetailDTO> details =
                        detailDao.getOrderDetailsByOrderId(orderId);

                    request.setAttribute("ORDER", order);
                    request.setAttribute("DETAILS", details);
                    request.getRequestDispatcher(ORDER_DETAIL).forward(request, response);
                    break;
                }

                // ================================================
                // 4. CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
                // ================================================
                case "updateStatus":
                case "updateOrderStatus": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    int orderId   = Integer.parseInt(request.getParameter("orderId"));
                    String newStatus = request.getParameter("newStatus");
                    if (newStatus == null) {
                        newStatus = request.getParameter("status");
                    }

                    if (orderDao.updateStatus(orderId, newStatus)) {
                        session.setAttribute("MSG_SUCCESS",
                            "Đã cập nhật trạng thái đơn hàng #" + orderId);
                    } else {
                        session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                    }
                    response.sendRedirect(
                        "MainController?action=manageOrderDetail&id=" + orderId);
                    break;
                }

                // ================================================
                // 5. ĐẶT HÀNG (CHECKOUT)
                // ================================================
                case "checkout": {
                    String addressIdStr = request.getParameter("addressId");
                    String totalStr     = request.getParameter("totalAmount");

                    if (addressIdStr == null || totalStr == null) {
                        session.setAttribute("MSG_ERROR",
                            "Thiếu thông tin đặt hàng!");
                        response.sendRedirect("MainController?action=viewCart");
                        return;
                    }

                    int addressId = Integer.parseInt(addressIdStr);
                    int total     = Integer.parseInt(totalStr);

                    OrderDTO order = new OrderDTO();
                    order.setUser_id(user.getUserId());
                    order.setAddress_id(addressId);
                    order.setTotal_amount(total);
                    order.setStatus("Pending");

                    if (orderDao.create(order)) {
                        List<OrderDTO> userOrders =
                            orderDao.getOrdersByUserId(user.getUserId());
                        int currentOrderId = userOrders.get(0).getOrder_id();

                        Integer cartId = (Integer) session.getAttribute("CART_ID");
                        if (cartId != null) {
                            List<CartItemDTO> cartItems =
                                cartItemDao.getItemsByCartId(cartId);

                            for (CartItemDTO item : cartItems) {
                                BookDTO book = bookDao.readById(item.getBook_id());
                                double bookPrice = (book != null) ? book.getPrice() : 0.0;

                                OrderDetailDTO detail = new OrderDetailDTO();
                                detail.setOrder_id(currentOrderId);
                                detail.setBook_id(item.getBook_id());
                                detail.setQuantity(item.getQuantity());
                                detail.setPrice((int) bookPrice);
                                detailDao.create(detail);

                                // Trừ kho
                                bookDao.updateStock(
                                    item.getBook_id(), item.getQuantity());
                            }

                            // Xóa giỏ hàng sau khi đặt
                            cartItemDao.deleteByCartId(cartId);
                            session.removeAttribute("CART");
                            session.removeAttribute("CART_BOOKS");
                            session.removeAttribute("TOTAL_PRICE");
                            session.removeAttribute("TOTAL_ITEMS");
                        }

                        session.setAttribute("MSG_SUCCESS",
                            "Đặt hàng thành công! Cảm ơn bạn đã mua sắm.");
                        response.sendRedirect(
                            "MainController?action=myOrderDetail&orderId="
                            + currentOrderId);
                    } else {
                        session.setAttribute("MSG_ERROR",
                            "Đặt hàng thất bại, vui lòng thử lại!");
                        response.sendRedirect("MainController?action=viewCart");
                    }
                    break;
                }

                // ================================================
                // 6. LỊCH SỬ MUA HÀNG CỦA USER
                // ================================================
                case "myOrders": {
                    List<OrderDTO> list =
                        orderDao.getOrdersByUserId(user.getUserId());
                    request.setAttribute("ORDER_LIST", list);
                    request.getRequestDispatcher(MY_ORDERS).forward(request, response);
                    break;
                }

                // ================================================
                // 7. CHI TIẾT ĐƠN HÀNG CỦA USER
                // ================================================
                case "myOrderDetail": {
                    String orderIdStr = request.getParameter("orderId");
                    if (orderIdStr == null) {
                        response.sendRedirect("MainController?action=myOrders");
                        return;
                    }
                    int orderId = Integer.parseInt(orderIdStr);
                    OrderDTO order = orderDao.readById(orderId);

                    // Bảo mật: chỉ xem đơn của mình
                    if (order == null || order.getUser_id() != user.getUserId()) {
                        session.setAttribute("MSG_ERROR",
                            "Bạn không có quyền xem đơn hàng này!");
                        response.sendRedirect("MainController?action=myOrders");
                        return;
                    }

                    List<OrderDetailDTO> details =
                        detailDao.getOrderDetailsByOrderId(orderId);
                    request.setAttribute("ORDER", order);
                    request.setAttribute("DETAILS", details);
                    request.getRequestDispatcher(MY_ORDER_DETAIL)
                           .forward(request, response);
                    break;
                }

                default:
                    response.sendRedirect("MainController?action=home");
            }

        } catch (Exception e) {
            log("Error at OrderController: " + e.toString());
            request.getRequestDispatcher(ERROR_500).forward(request, response);
        }
    }

    // ---- Helper: Admin(1), Manager(2), Staff(3) ----
    private boolean hasAdminAccess(UserDTO user) {
        if (user == null) return false;
        return user.getRoleId() == 1
            || user.getRoleId() == 2
            || user.getRoleId() == 3;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}