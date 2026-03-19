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
        if (action == null) action = (String) request.getAttribute("action");

        NotificationDAO dao = new NotificationDAO();
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");

        try {
            // Trang gửi thông báo (Admin/Manager)
            if ("sendNotification".equals(action) || "viewSendPage".equals(action)) {
                if (user == null || (user.getRoleId() != 1 && user.getRoleId() != 2)) {
                    response.sendRedirect("MainController?action=login"); // ✅ SỬA
                    return;
                }
                if ("GET".equalsIgnoreCase(request.getMethod()) || "viewSendPage".equals(action)) {
                    request.getRequestDispatcher("/WEB-INF/views/admin/send-notification.jsp").forward(request, response); // ✅ SỬA path
                    return;
                }
                // POST: xử lý gửi
                String title   = request.getParameter("title");
                String content = request.getParameter("content");
                String target  = request.getParameter("targetUser");

                NotificationDTO noti = new NotificationDTO();
                noti.setContent("[" + title + "] " + content);

                if ("all".equals(target)) {
                    noti.setUser_id(0);
                } else {
                    int receiverId = Integer.parseInt(request.getParameter("receiverId"));
                    noti.setUser_id(receiverId);
                }

                if (dao.create(noti)) {
                    session.setAttribute("MSG_SUCCESS", "Đã gửi thông báo thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Gửi thông báo thất bại!");
                }
                response.sendRedirect("MainController?action=sendNotification");
                return;
            }

            // Xem thông báo của user
            if (user == null) {
                response.sendRedirect("MainController?action=login"); // ✅ SỬA
                return;
            }

            if (action == null || "list".equals(action) || "viewNoti".equals(action)) {
                List<NotificationDTO> list = dao.getNotificationsByUserId(user.getUserId());
                request.setAttribute("NOTI_LIST", list);
                request.getRequestDispatcher("/WEB-INF/views/user/notifications.jsp").forward(request, response); // ✅ SỬA path

            } else if ("markRead".equals(action)) {
                int notiId = Integer.parseInt(request.getParameter("notiId"));
                dao.markAsRead(notiId);
                response.sendRedirect("MainController?action=viewNoti");
            }

        } catch (Exception e) {
            log("Error at NotificationController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp").forward(request, response); // ✅ SỬA path
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