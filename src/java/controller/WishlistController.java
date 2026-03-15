package controller;

import dao.WishlistDAO;
import dto.UserDTO;
import dto.WishlistDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "WishlistController", urlPatterns = {"/WishlistController"})
public class WishlistController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        WishlistDAO dao = new WishlistDAO();
        HttpSession session = request.getSession();
        
        // 1. KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            session.setAttribute("MSG_ERROR", "Vui lòng đăng nhập để sử dụng tính năng này!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // ACTION: XEM DANH SÁCH (VIEW)
            if (action == null || action.equals("view")) {
                List<WishlistDTO> list = dao.getWishlistByUserId(user.getUserId());
                request.setAttribute("WISHLIST_DATA", list);
                request.getRequestDispatcher("WEB-INF/views/user/wishlist.jsp").forward(request, response);

            // ACTION: THÊM VÀO DANH SÁCH (ADD)
            } else if (action.equals("add")) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                WishlistDTO dto = new WishlistDTO();
                dto.setUser_id(user.getUserId());
                dto.setBook_id(bookId);
                
                boolean check = dao.create(dto);
                if (check) {
                    // Dùng Session để thông báo không bị mất khi Redirect
                    session.setAttribute("MSG_SUCCESS", "Đã thêm vào danh sách yêu thích!");
                } else {
                    session.setAttribute("MSG_ERROR", "Sách này đã có trong danh sách hoặc có lỗi xảy ra.");
                }
                
                // Quay lại trang trước đó (Shop hoặc Detail)
                String url = request.getHeader("referer");
                response.sendRedirect(url != null ? url : "BookController?action=view");

            // ACTION: XÓA KHỎI DANH SÁCH (DELETE)
            } else if (action.equals("delete")) {
                int wishlistId = Integer.parseInt(request.getParameter("wishlistId"));
                boolean check = dao.delete(wishlistId);
                
                if (check) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa mục này.");
                }
                response.sendRedirect("WishlistController?action=view");
            }
            
        } catch (Exception e) {
            log("Error at WishlistController: " + e.toString());
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