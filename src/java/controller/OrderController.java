package controller;

import dao.AddressDAO;
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
import java.util.ArrayList;
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

    private static final String DASHBOARD        = "/WEB-INF/views/admin/dashboard.jsp";
    private static final String MANAGE_ORDERS    = "/WEB-INF/views/admin/manage-orders.jsp";
    private static final String ORDER_DETAIL     = "/WEB-INF/views/admin/admin-order-detail.jsp";
    private static final String MY_ORDERS        = "/WEB-INF/views/user/my-orders.jsp";
    private static final String MY_ORDER_DETAIL  = "/WEB-INF/views/user/my-order-detail.jsp";
    private static final String CHECKOUT_PAGE    = "/WEB-INF/views/web/checkout.jsp";
    private static final String CHECKOUT_SUCCESS = "/WEB-INF/views/web/checkout-success.jsp"; // ✅ THÊM
    private static final String ERROR_500        = "/WEB-INF/views/web/error-500.jsp";
    private static final String LOGIN_URL        = "MainController?action=login";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        OrderDAO       orderDao   = new OrderDAO();
        OrderDetailDAO detailDao  = new OrderDetailDAO();
        CartItemDAO    cartItemDao = new CartItemDAO();
        BookDAO        bookDao    = new BookDAO();
        AddressDAO     addressDao = new AddressDAO();
        HttpSession    session    = request.getSession();

        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            response.sendRedirect(LOGIN_URL);
            return;
        }

        try {
            switch (action) {

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

                case "manageOrders": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    List<OrderDTO> allOrders = orderDao.readAll();
                    String filterStatus = request.getParameter("status");
                    if (filterStatus != null && !filterStatus.trim().isEmpty()) {
                        List<OrderDTO> filtered = new ArrayList<>();
                        for (OrderDTO o : allOrders) {
                            if (filterStatus.equalsIgnoreCase(o.getStatus())) filtered.add(o);
                        }
                        allOrders = filtered;
                        request.setAttribute("CURRENT_STATUS", filterStatus);
                    }
                    request.setAttribute("ORDER_LIST", allOrders);
                    request.getRequestDispatcher(MANAGE_ORDERS).forward(request, response);
                    break;
                }

                case "manageOrderDetail": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    int orderId = Integer.parseInt(request.getParameter("id"));
                    OrderDTO order = orderDao.readById(orderId);
                    List<OrderDetailDTO> details = detailDao.getOrderDetailsByOrderId(orderId);
                    request.setAttribute("ORDER", order);
                    request.setAttribute("DETAILS", details);
                    request.getRequestDispatcher(ORDER_DETAIL).forward(request, response);
                    break;
                }

                case "updateStatus":
                case "updateOrderStatus": {
                    if (!hasAdminAccess(user)) {
                        response.sendRedirect(LOGIN_URL);
                        return;
                    }
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    String newStatus = request.getParameter("newStatus");
                    if (newStatus == null) newStatus = request.getParameter("status");
                    if (orderDao.updateStatus(orderId, newStatus)) {
                        session.setAttribute("MSG_SUCCESS", "Đã cập nhật trạng thái đơn hàng #" + orderId);
                    } else {
                        session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                    }
                    response.sendRedirect("MainController?action=manageOrderDetail&id=" + orderId);
                    break;
                }

                // ✅ BƯỚC 1: Hiển thị trang nhập thông tin giao hàng
                case "checkout": {
                    List<CartItemDTO> cart = (List<CartItemDTO>) session.getAttribute("CART");
                    if (cart == null || cart.isEmpty()) {
                        session.setAttribute("MSG_ERROR", "Giỏ hàng của bạn đang trống!");
                        response.sendRedirect("MainController?action=viewCart");
                        return;
                    }
                    request.getRequestDispatcher(CHECKOUT_PAGE).forward(request, response);
                    break;
                }

                // ✅ BƯỚC 2: Tạo Address → Tạo Order → Forward sang trang thành công
                case "placeOrder": {
                    String receiverName = request.getParameter("receiverName");
                    String phone        = request.getParameter("phone");
                    String address      = request.getParameter("address");
                    String totalStr     = request.getParameter("totalAmount");

                    // Validate
                    if (receiverName == null || receiverName.trim().isEmpty()
                            || phone == null || phone.trim().isEmpty()
                            || address == null || address.trim().isEmpty()
                            || totalStr == null || totalStr.trim().isEmpty()) {
                        session.setAttribute("MSG_ERROR", "Vui lòng điền đầy đủ thông tin giao hàng!");
                        response.sendRedirect("MainController?action=checkout");
                        return;
                    }

                    int total = 0;
                    try {
                        total = Integer.parseInt(totalStr.trim());
                    } catch (NumberFormatException e) {
                        session.setAttribute("MSG_ERROR", "Tổng tiền không hợp lệ!");
                        response.sendRedirect("MainController?action=checkout");
                        return;
                    }

                    // Ghép địa chỉ đầy đủ
                    String fullAddress = receiverName.trim() + " - " + phone.trim() + " - " + address.trim();

                    // Tạo bản ghi Address mới → lấy address_id hợp lệ
                    int newAddressId = addressDao.createAndGetId(user.getUserId(), fullAddress, "", "");
                    if (newAddressId == -1) {
                        session.setAttribute("MSG_ERROR", "Không thể lưu địa chỉ, vui lòng thử lại!");
                        response.sendRedirect("MainController?action=checkout");
                        return;
                    }

                    // Tạo đơn hàng
                    OrderDTO order = new OrderDTO();
                    order.setUser_id(user.getUserId());
                    order.setAddress_id(newAddressId);
                    order.setTotal_amount(total);
                    order.setStatus("Pending");

                    if (orderDao.create(order)) {
                        List<OrderDTO> userOrders = orderDao.getOrdersByUserId(user.getUserId());
                        int currentOrderId = userOrders.get(0).getOrder_id();

                        // Lưu OrderDetails
                        Integer cartId = (Integer) session.getAttribute("CART_ID");
                        if (cartId != null) {
                            List<CartItemDTO> cartItems = cartItemDao.getItemsByCartId(cartId);
                            for (CartItemDTO item : cartItems) {
                                BookDTO book = bookDao.readById(item.getBook_id());
                                double bookPrice = (book != null) ? book.getPrice() : 0.0;

                                OrderDetailDTO detail = new OrderDetailDTO();
                                detail.setOrder_id(currentOrderId);
                                detail.setBook_id(item.getBook_id());
                                detail.setQuantity(item.getQuantity());
                                detail.setPrice((int) bookPrice);
                                detailDao.create(detail);
                                bookDao.updateStock(item.getBook_id(), item.getQuantity());
                            }
                            cartItemDao.deleteByCartId(cartId);
                        }

                        // ✅ Set ORDER_ID để hiển thị trên trang thành công
                        request.setAttribute("ORDER_ID", currentOrderId);
                        session.setAttribute("MSG_SUCCESS", "Đặt hàng thành công! Cảm ơn bạn đã mua sắm.");

                        // ✅ KHÔNG xóa CART và CART_BOOKS ngay - để checkout-success.jsp hiển thị được
                        // Sẽ xóa sau khi forward xong (dùng forward thay vì redirect)
                        request.getRequestDispatcher(CHECKOUT_SUCCESS).forward(request, response);

                        // ✅ Xóa giỏ hàng sau khi forward
                        session.removeAttribute("CART");
                        session.removeAttribute("CART_BOOKS");
                        session.removeAttribute("TOTAL_PRICE");
                        session.removeAttribute("TOTAL_ITEMS");

                    } else {
                        session.setAttribute("MSG_ERROR", "Đặt hàng thất bại, vui lòng thử lại!");
                        response.sendRedirect("MainController?action=viewCart");
                    }
                    break;
                }

                case "myOrders": {
                    List<OrderDTO> list = orderDao.getOrdersByUserId(user.getUserId());
                    request.setAttribute("ORDER_LIST", list);
                    request.getRequestDispatcher(MY_ORDERS).forward(request, response);
                    break;
                }

                case "myOrderDetail": {
                    String orderIdStr = request.getParameter("orderId");
                    if (orderIdStr == null) {
                        response.sendRedirect("MainController?action=myOrders");
                        return;
                    }
                    int orderId = Integer.parseInt(orderIdStr);
                    OrderDTO order = orderDao.readById(orderId);
                    if (order == null || order.getUser_id() != user.getUserId()) {
                        session.setAttribute("MSG_ERROR", "Bạn không có quyền xem đơn hàng này!");
                        response.sendRedirect("MainController?action=myOrders");
                        return;
                    }
                    List<OrderDetailDTO> details = detailDao.getOrderDetailsByOrderId(orderId);
                    request.setAttribute("ORDER", order);
                    request.setAttribute("DETAILS", details);
                    request.getRequestDispatcher(MY_ORDER_DETAIL).forward(request, response);
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

    private boolean hasAdminAccess(UserDTO user) {
        if (user == null) return false;
        return user.getRoleId() == 1 || user.getRoleId() == 2 || user.getRoleId() == 3;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}