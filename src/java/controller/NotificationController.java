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
        
        // Lấy action từ request hoặc từ attribute do MainController map qua
        String action = request.getParameter("action");
        if (action == null) {
            action = (String) request.getAttribute("action");
        }
        
        NotificationDAO dao = new NotificationDAO();
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        String url = "error-404.jsp";

        try {
            // TRƯỜNG HỢP 1: Hiển thị trang Form gửi thông báo (Sửa lỗi trắng màn)
            if ("viewSendPage".equals(action)) {
                url = "send-notification.jsp"; 
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // TRƯỜNG HỢP 2: Xử lý khi nhấn nút "Phát hành thông báo"
            if ("sendNotification".equals(action)) {
                String title = request.getParameter("title");
                String content = request.getParameter("content");
                String target = request.getParameter("targetUser");
                
                NotificationDTO noti = new NotificationDTO();
                noti.setContent("[" + title + "] " + content);
                
                if ("all".equals(target)) {
                    // Logic gửi cho tất cả (giả sử user_id 0 là broadcast hoặc lặp list)
                    noti.setUser_id(0); 
                } else {
                    int receiverId = Integer.parseInt(request.getParameter("receiverId"));
                    noti.setUser_id(receiverId);
                }
                
                if (dao.create(noti)) {
                    session.setAttribute("MSG_SUCCESS", "Đã gửi thông báo thành công!");
                }
                response.sendRedirect("MainController?action=sendNotification");
                return;
            }

            // CÁC TRƯỜNG HỢP CŨ CỦA BẠN
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if (action == null || action.equals("list")) {
                List<NotificationDTO> list = dao.getNotificationsByUserId(user.getUserId());
                request.setAttribute("NOTI_LIST", list);
                url = "WEB-INF/views/user/notifications.jsp";
            } else if (action.equals("markRead")) {
                int notiId = Integer.parseInt(request.getParameter("notiId"));
                dao.markAsRead(notiId);
                url = "NotificationController?action=list";
                response.sendRedirect(url); return;
            }
            
            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception e) {
            log("Error at NotificationController: " + e.toString());
            response.sendRedirect("error-500.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}