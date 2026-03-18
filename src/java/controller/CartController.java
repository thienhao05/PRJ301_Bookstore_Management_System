package controller;

import dao.BookDAO;
import dao.CartItemDAO;
import dto.CartItemDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        CartItemDAO itemDao = new CartItemDAO();
        BookDAO bookDao = new BookDAO();
        HttpSession session = request.getSession();

        // 1. KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            session.setAttribute("MSG_ERROR", "Vui lòng đăng nhập để mua hàng!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Giả sử CART_ID được lưu vào session khi User đăng nhập thành công
            int cartId = (int) session.getAttribute("CART_ID");

            if (action == null || action.equals("view")) {
                // 2. XEM GIỎ HÀNG
                List<CartItemDTO> cartItems = itemDao.getItemsByCartId(cartId);
                request.setAttribute("CART_ITEMS", cartItems);
                request.getRequestDispatcher("cart.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 3. THÊM SÁCH VÀO GIỎ
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int quantity = 1; // Mặc định mỗi lần nhấn là thêm 1
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }

                // Kiểm tra xem sách đã có trong giỏ chưa
                CartItemDTO existingItem = itemDao.getItem(cartId, bookId);
                if (existingItem != null) {
                    // Nếu có rồi thì tăng số lượng
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    itemDao.update(existingItem);
                } else {
                    // Nếu chưa có thì tạo mới
                    CartItemDTO newItem = new CartItemDTO(0, cartId, bookId, quantity);
                    itemDao.create(newItem);
                }
                
                session.setAttribute("MSG_SUCCESS", "Đã thêm vào giỏ hàng!");
                String url = request.getHeader("referer");
                response.sendRedirect(url != null ? url : "BookController?action=view");

            } else if (action.equals("update")) {
                // 4. CẬP NHẬT SỐ LƯỢNG (Dành cho trang Cart)
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                
                if (newQty > 0) {
                    CartItemDTO item = itemDao.readById(itemId);
                    if (item != null) {
                        item.setQuantity(newQty);
                        itemDao.update(item);
                    }
                } else {
                    itemDao.delete(itemId); // Nếu qty = 0 thì xóa luôn
                }
                response.sendRedirect("CartController?action=view");

            } else if (action.equals("remove")) {
                // 5. XÓA KHỎI GIỎ
                int itemId = Integer.parseInt(request.getParameter("itemId"));
                itemDao.delete(itemId);
                response.sendRedirect("CartController?action=view");
            }
            
        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp").forward(request, response);
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