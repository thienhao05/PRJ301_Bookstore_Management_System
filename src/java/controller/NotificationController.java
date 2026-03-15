package controller;

import dao.NotificationDAO;
import dto.NotificationDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "NotificationController", urlPatterns = {"/NotificationController"})
public class NotificationController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        NotificationDAO dao = new NotificationDAO();
        HttpSession session = request.getSession();
        
        // 1. KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            session.setAttribute("MSG_ERROR", "Vui lòng đăng nhập để xem thông báo!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 2. XEM DANH SÁCH THÔNG BÁO CỦA USER
                List<NotificationDTO> list = dao.getNotificationsByUserId(user.getUserId());
                request.setAttribute("NOTI_LIST", list);
                request.getRequestDispatcher("WEB-INF/views/user/notifications.jsp").forward(request, response);

            } else if (action.equals("markRead")) {
                // 3. ĐÁNH DẤU LÀ ĐÃ ĐỌC (Single)
                int notiId = Integer.parseInt(request.getParameter("notiId"));
                
                // Bảo mật: Check xem thông báo này có đúng là của User đang login không
                NotificationDTO noti = dao.readById(notiId);
                if (noti != null && noti.getUser_id() == user.getUserId()) {
                    dao.markAsRead(notiId);
                }
                
                // Quay lại trang danh sách hoặc trang trước đó (referer)
                String url = request.getHeader("referer");
                response.sendRedirect(url != null ? url : "NotificationController?action=list");

            } else if (action.equals("markAllRead")) {
                // 4. ĐÁNH DẤU TẤT CẢ LÀ ĐÃ ĐỌC
                dao.markAllAsRead(user.getUserId());
                session.setAttribute("MSG_SUCCESS", "Đã đánh dấu đọc tất cả!");
                response.sendRedirect("NotificationController?action=list");

            } else if (action.equals("delete")) {
                // 5. XÓA THÔNG BÁO
                int notiId = Integer.parseInt(request.getParameter("notiId"));
                NotificationDTO noti = dao.readById(notiId);
                
                if (noti != null && noti.getUser_id() == user.getUserId()) {
                    dao.delete(notiId);
                }
                response.sendRedirect("NotificationController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at NotificationController: " + e.toString());
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