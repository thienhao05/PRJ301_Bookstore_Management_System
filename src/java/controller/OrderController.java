package controller;

import dao.BookDAO;
import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.CartItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.UserDTO;
import dto.BookDTO; // Đảm bảo đã import BookDTO
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        OrderDAO orderDao = new OrderDAO();
        OrderDetailDAO detailDao = new OrderDetailDAO();
        CartItemDAO cartItemDao = new CartItemDAO();
        BookDAO bookDao = new BookDAO();
        HttpSession session = request.getSession();

        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. XỬ LÝ ĐẶT HÀNG (CHECKOUT)
            if ("checkout".equals(action)) {
                int addressId = Integer.parseInt(request.getParameter("addressId"));
                int shippingId = Integer.parseInt(request.getParameter("shippingId"));
                int total = Integer.parseInt(request.getParameter("totalAmount"));

                OrderDTO order = new OrderDTO();
                order.setUser_id(user.getUserId());
                order.setAddress_id(addressId);
                order.setShipping_provider_id(shippingId);
                order.setTotal_amount(total);
                order.setStatus("Pending");

                if (orderDao.create(order)) {
                    // Lấy đơn hàng mới nhất của User vừa tạo
                    List<OrderDTO> userOrders = orderDao.getOrdersByUserId(user.getUserId());
                    int currentOrderId = userOrders.get(0).getOrder_id();

                    int cartId = (int) session.getAttribute("CART_ID");
                    List<CartItemDTO> cartItems = cartItemDao.getItemsByCartId(cartId);

                    for (CartItemDTO item : cartItems) {
                        // 1. Lấy thông tin sách để có giá hiện tại
                        BookDTO book = bookDao.readById(item.getBook_id());

                        // Nếu tìm thấy sách thì lấy giá (double), không thì để là 0
                        double bookPrice = (book != null) ? book.getPrice() : 0.0;

                        OrderDetailDTO detail = new OrderDetailDTO();
                        detail.setOrder_id(currentOrderId);
                        detail.setBook_id(item.getBook_id());
                        detail.setQuantity(item.getQuantity());

                        // FIX: Ép kiểu từ double sang int để khớp với hàm setPrice(int) của bạn
                        // Và nhớ xóa dấu ngoặc thừa nhé: (int) bookPrice
                        detail.setPrice((int) bookPrice);

                        // 2. Lưu vào bảng OrderDetail
                        detailDao.create(detail);

                        // 3. Cập nhật lại kho hàng
                        bookDao.updateStock(item.getBook_id(), item.getQuantity());
                    }

                    // Xóa giỏ hàng sau khi đặt thành công (Nên thực hiện)
                    // cartItemDao.clearCart(cartId); 
                    response.sendRedirect("PaymentController?action=process&orderId=" + currentOrderId);
                }

            } // 2. XEM LỊCH SỬ (CHO USER)
            else if ("history".equals(action)) {
                List<OrderDTO> list = orderDao.getOrdersByUserId(user.getUserId());
                request.setAttribute("ORDER_HISTORY", list);
                request.getRequestDispatcher("order-history.jsp").forward(request, response);
            } // 3. THỐNG KÊ DASHBOARD (CHO ADMIN)
            else if ("dashboard".equals(action)) {
                if (user.getRoleId() == 1) {
                    Map<String, Object> stats = orderDao.getDashboardStats();
                    request.setAttribute("TOTAL_REVENUE", stats.get("TOTAL_REVENUE"));
                    request.setAttribute("TOTAL_ORDERS", stats.get("TOTAL_ORDERS"));
                    request.setAttribute("TOTAL_USERS", stats.get("TOTAL_USERS"));
                    request.setAttribute("TOTAL_BOOKS", stats.get("TOTAL_BOOKS"));
                    request.setAttribute("RECENT_ORDERS", stats.get("RECENT_ORDERS"));
                    request.getRequestDispatcher("admin/dashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("error-403.jsp");
                }
            } // 4. QUẢN LÝ DANH SÁCH ĐƠN HÀNG (CHO ADMIN)
            else if ("manageOrders".equals(action)) {
                if (user.getRoleId() == 1) {
                    List<OrderDTO> allOrders = orderDao.readAll();
                    request.setAttribute("ORDER_LIST", allOrders);
                    request.getRequestDispatcher("admin/manage-orders.jsp").forward(request, response);
                }
            } // 5. CHI TIẾT ĐƠN HÀNG (CHO ADMIN)
            else if ("manageOrderDetail".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("id"));
                OrderDTO order = orderDao.readById(orderId);
                List<OrderDetailDTO> details = detailDao.getOrderDetailsByOrderId(orderId);

                request.setAttribute("ORDER", order);
                request.setAttribute("DETAILS", details);
                request.getRequestDispatcher("admin/admin-order-detail.jsp").forward(request, response);
            } // 6. CẬP NHẬT TRẠNG THÁI
            else if ("updateStatus".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                if (orderDao.updateStatus(orderId, status)) {
                    session.setAttribute("MSG_SUCCESS", "Đã cập nhật đơn hàng #" + orderId);
                }
                response.sendRedirect("MainController?action=manageOrderDetail&id=" + orderId);
            }

        } catch (Exception e) {
            log("Error at OrderController: " + e.toString());
            response.sendRedirect("error-500.jsp");
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
