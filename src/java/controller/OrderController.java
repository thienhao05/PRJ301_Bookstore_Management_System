package controller;

import dao.BookDAO;
import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO; // Bạn nhớ tạo DAO này nhé
import dto.CartItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
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
            if ("checkout".equals(action)) {
                // 1. XỬ LÝ ĐẶT HÀNG (TRỌNG TÂM)
                int addressId = Integer.parseInt(request.getParameter("addressId"));
                int shippingId = Integer.parseInt(request.getParameter("shippingId"));
                int total = Integer.parseInt(request.getParameter("totalAmount"));
                
                // Tạo đối tượng Order
                OrderDTO order = new OrderDTO();
                order.setUser_id(user.getUserId());
                order.setAddress_id(addressId);
                order.setShipping_provider_id(shippingId);
                order.setTotal_amount(total);
                order.setStatus("Pending");

                // Bước A: Lưu Order vào DB
                boolean isOrderCreated = orderDao.create(order);
                
                if (isOrderCreated) {
                    // Lấy ID đơn hàng vừa tạo (giả định bạn có hàm lấy LastID hoặc lấy theo User)
                    List<OrderDTO> userOrders = orderDao.getOrdersByUserId(user.getUserId());
                    int currentOrderId = userOrders.get(0).getOrder_id(); 

                    // Bước B: Chuyển toàn bộ CartItems sang OrderDetails
                    int cartId = (int) session.getAttribute("CART_ID");
                    List<CartItemDTO> cartItems = cartItemDao.getItemsByCartId(cartId);

                    for (CartItemDTO item : cartItems) {
                        // Lưu chi tiết đơn hàng
                        OrderDetailDTO detail = new OrderDetailDTO();
                        detail.setOrder_id(currentOrderId);
                        detail.setBook_id(item.getBook_id());
                        detail.setQuantity(item.getQuantity());
                        // detail.setPrice(...); // Nên lấy giá thực tế của sách lúc đó
                        detailDao.create(detail);

                        // Bước C: Trừ số lượng tồn kho
                        bookDao.updateStock(item.getBook_id(), item.getQuantity());
                    }

                    // Chuyển sang bước thanh toán
                    response.sendRedirect("PaymentController?action=process&orderId=" + currentOrderId);
                }

            } else if ("history".equals(action)) {
                // 2. XEM LỊCH SỬ MUA HÀNG
                List<OrderDTO> list = orderDao.getOrdersByUserId(user.getUserId());
                request.setAttribute("ORDER_HISTORY", list);
                request.getRequestDispatcher("order-history.jsp").forward(request, response);

            } else if ("manage".equals(action)) {
                // 3. ADMIN: QUẢN LÝ ĐƠN HÀNG
                if (user.getRoleId() == 1 || user.getRoleId() == 2) {
                    List<OrderDTO> allOrders = orderDao.readAll();
                    request.setAttribute("ALL_ORDERS", allOrders);
                    request.getRequestDispatcher("admin/manage-orders.jsp").forward(request, response);
                }

            } else if ("update_status".equals(action)) {
                // 4. CẬP NHẬT TRẠNG THÁI (Dành cho Admin)
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");
                if (orderDao.updateOrderStatus(orderId, status)) {
                    session.setAttribute("MSG_SUCCESS", "Đã cập nhật trạng thái đơn hàng!");
                }
                response.sendRedirect("OrderController?action=manage");
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