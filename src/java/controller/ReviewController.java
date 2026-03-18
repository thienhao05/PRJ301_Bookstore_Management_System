package controller;

import dao.ReviewDAO;
import dto.ReviewDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ReviewController", urlPatterns = {"/ReviewController"})
public class ReviewController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        ReviewDAO dao = new ReviewDAO();
        HttpSession session = request.getSession();
        
        // 1. KIỂM TRA ĐĂNG NHẬP (Chỉ user đã login mới được review hoặc quản lý)
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            session.setAttribute("MSG_ERROR", "Bạn cần đăng nhập để thực hiện thao tác này!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if ("add".equals(action)) {
                // 2. THÊM ĐÁNH GIÁ MỚI
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = request.getParameter("comment");

                ReviewDTO dto = new ReviewDTO();
                dto.setUser_id(user.getUserId());
                dto.setBook_id(bookId);
                dto.setRating(rating);
                dto.setComment(comment);

                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cảm ơn bạn đã để lại đánh giá!");
                } else {
                    session.setAttribute("MSG_ERROR", "Gửi đánh giá thất bại. Vui lòng thử lại!");
                }
                
                // Quay lại trang chi tiết cuốn sách vừa review
                String url = request.getHeader("referer");
                response.sendRedirect(url != null ? url : "BookController?action=detail&bookId=" + bookId);

            } else if ("delete".equals(action)) {
                // 3. XÓA ĐÁNH GIÁ (Admin hoặc chính chủ mới có quyền)
                int reviewId = Integer.parseInt(request.getParameter("reviewId"));
                ReviewDTO currentReview = dao.readById(reviewId);
                
                if (currentReview != null) {
                    // Check quyền: Phải là Admin (Role 1) hoặc người viết Review đó mới được xóa
                    if (user.getRoleId() == 1 || user.getUserId() == currentReview.getUser_id()) {
                        if (dao.delete(reviewId)) {
                            session.setAttribute("MSG_SUCCESS", "Đã xóa đánh giá.");
                        }
                    } else {
                        session.setAttribute("MSG_ERROR", "Bạn không có quyền xóa đánh giá này!");
                    }
                }
                
                String url = request.getHeader("referer");
                response.sendRedirect(url != null ? url : "index.jsp");

            } else if ("list".equals(action)) {
                // 4. ADMIN: XEM TOÀN BỘ ĐÁNH GIÁ ĐỂ QUẢN LÝ
                if (user.getRoleId() == 1) {
                    List<ReviewDTO> list = dao.readAll();
                    request.setAttribute("REVIEW_LIST", list);
                    request.getRequestDispatcher("admin/manage-reviews.jsp").forward(request, response);
                } else {
                    response.sendRedirect("index.jsp");
                }
            }
            
        } catch (Exception e) {
            log("Error at ReviewController: " + e.toString());
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