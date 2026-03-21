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

    private static final String WISHLIST_PAGE = "/WEB-INF/views/user/wishlist.jsp"; // ✅ SỬA: thêm /
    private static final String LOGIN_URL     = "MainController?action=login";       // ✅ SỬA
    private static final String ERROR_500     = "/WEB-INF/views/web/error-500.jsp";

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
            response.sendRedirect(LOGIN_URL); // ✅ SỬA
            return;
        }

        try {
            // ✅ SỬA: Xử lý cả "viewWishlist" và "view" và null
            if (action == null || action.equals("view") || action.equals("viewWishlist")) {
                List<WishlistDTO> list = dao.getWishlistByUserId(user.getUserId());
                request.setAttribute("WISHLIST", list); // ✅ SỬA: đổi thành WISHLIST cho khớp JSP
                request.getRequestDispatcher(WISHLIST_PAGE).forward(request, response);

            // ✅ SỬA: Xử lý cả "addWishlist" và "add"
            } else if (action.equals("add") || action.equals("addWishlist")) {
                int bookId = Integer.parseInt(request.getParameter("bookId") != null
                        ? request.getParameter("bookId")
                        : request.getParameter("id"));

                WishlistDTO dto = new WishlistDTO();
                dto.setUser_id(user.getUserId());
                dto.setBook_id(bookId);

                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Đã thêm vào danh sách yêu thích!");
                } else {
                    session.setAttribute("MSG_ERROR", "Sách này đã có trong danh sách hoặc có lỗi xảy ra.");
                }

                // Quay lại trang trước
                String referer = request.getHeader("referer");
                response.sendRedirect(referer != null ? referer : "MainController?action=view");

            // ✅ SỬA: Xử lý cả "removeFromWishlist", "removeWishlist", "delete"
            } else if (action.equals("delete") || action.equals("removeWishlist")
                    || action.equals("removeFromWishlist")) {

                // Hỗ trợ cả 2 param: wishlistId hoặc id (bookId)
                String wishlistIdStr = request.getParameter("wishlistId");
                String bookIdStr     = request.getParameter("id");

                boolean result = false;
                if (wishlistIdStr != null) {
                    result = dao.delete(Integer.parseInt(wishlistIdStr));
                } else if (bookIdStr != null) {
                    result = dao.deleteByUserAndBook(user.getUserId(), Integer.parseInt(bookIdStr));
                }

                if (result) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa khỏi danh sách yêu thích!");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa mục này.");
                }
                response.sendRedirect("MainController?action=viewWishlist");
            }

        } catch (Exception e) {
            log("Error at WishlistController: " + e.toString());
            request.getRequestDispatcher(ERROR_500).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}