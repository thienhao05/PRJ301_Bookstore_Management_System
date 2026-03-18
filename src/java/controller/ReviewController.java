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

        // KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            session.setAttribute("MSG_ERROR", "Bạn cần đăng nhập để thực hiện thao tác này!");
            response.sendRedirect("MainController?action=login"); // ✅ SỬA
            return;
        }

        try {
            // ✅ THÊM CASE manageReviews
            if (action == null || "manageReviews".equals(action) || "list".equals(action)) {
                // Chỉ Admin (1) hoặc Manager (2) mới xem được
                if (user.getRoleId() == 1 || user.getRoleId() == 2) {
                    List<ReviewDTO> list = dao.readAll();
                    request.setAttribute("REVIEW_LIST", list);
                    request.getRequestDispatcher("/WEB-INF/views/admin/manage-reviews.jsp").forward(request, response);
                } else {
                    response.sendRedirect("MainController?action=home"); // ✅ SỬA
                }

            } else if ("add".equals(action)) {
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

                String referer = request.getHeader("referer");
                response.sendRedirect(referer != null ? referer : "MainController?action=bookDetail&bookId=" + bookId);

            } else if ("delete".equals(action)) {
                int reviewId = Integer.parseInt(request.getParameter("reviewId"));
                ReviewDTO currentReview = dao.readById(reviewId);

                if (currentReview != null) {
                    if (user.getRoleId() == 1 || user.getRoleId() == 2
                            || user.getUserId() == currentReview.getUser_id()) {
                        if (dao.delete(reviewId)) {
                            session.setAttribute("MSG_SUCCESS", "Đã xóa đánh giá.");
                        }
                    } else {
                        session.setAttribute("MSG_ERROR", "Bạn không có quyền xóa đánh giá này!");
                    }
                }

                String referer = request.getHeader("referer");
                response.sendRedirect(referer != null ? referer : "MainController?action=manageReviews"); // ✅ SỬA
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