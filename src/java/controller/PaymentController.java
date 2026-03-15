package controller;

import dao.CartItemDAO;
import dao.OrderDAO;
import dto.OrderDTO;
import dto.UserDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PaymentController", urlPatterns = {"/PaymentController"})
public class PaymentController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        OrderDAO orderDao = new OrderDAO();
        CartItemDAO cartItemDao = new CartItemDAO();
        HttpSession session = request.getSession();

        // 1. KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if ("process".equals(action)) {
                // 2. NHẬN THÔNG TIN THANH TOÁN
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String paymentMethod = request.getParameter("paymentMethod");
                
                // Lấy đơn hàng để kiểm tra bảo mật (tránh khách thanh toán đơn của người khác)
                OrderDTO order = orderDao.readById(orderId);
                
                if (order != null && order.getUser_id() == user.getUserId()) {
                    
                    // Giả lập logic: Nếu là COD thì Pending, nếu khác thì coi như đã trả (Paid)
                    String status = "COD".equals(paymentMethod) ? "Pending" : "Paid";
                    
                    // Cập nhật thông tin vào Database
                    // Note: Hào nhớ bổ sung hàm updatePaymentStatus vào OrderDAO nhé
                    boolean isUpdated = orderDao.updatePaymentStatus(orderId, paymentMethod, status);
                    
                    if (isUpdated) {
                        // 3. QUAN TRỌNG: DỌN DẸP GIỎ HÀNG
                        // Lấy cart_id từ session (đã lưu lúc login)
                        Integer cartId = (Integer) session.getAttribute("CART_ID");
                        if (cartId != null) {
                            cartItemDao.deleteByCartId(cartId);
                        }
                        
                        session.setAttribute("MSG_SUCCESS", "Thanh toán thành công! Cảm ơn bạn đã ủng hộ.");
                        response.sendRedirect("order-success.jsp?orderId=" + orderId);
                    } else {
                        session.setAttribute("MSG_ERROR", "Lỗi hệ thống khi cập nhật thanh toán.");
                        response.sendRedirect("checkout.jsp");
                    }
                } else {
                    response.sendRedirect("index.jsp");
                }

            } else if ("cancel".equals(action)) {
                // 4. HỦY THANH TOÁN
                session.setAttribute("MSG_ERROR", "Bạn đã hủy quá trình thanh toán.");
                response.sendRedirect("cart.jsp");
            }
            
        } catch (Exception e) {
            log("Error at PaymentController: " + e.toString());
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