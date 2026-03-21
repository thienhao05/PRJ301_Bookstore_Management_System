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

    private static final String NOTI_PAGE     = "/WEB-INF/views/user/notifications.jsp";
    private static final String SEND_PAGE     = "/WEB-INF/views/admin/send-notification.jsp";
    private static final String ERROR_500     = "/WEB-INF/views/web/error-500.jsp";
    private static final String LOGIN_URL     = "MainController?action=login";

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
                    response.sendRedirect(LOGIN_URL);
                    return;
                }
                if ("GET".equalsIgnoreCase(request.getMethod()) || "viewSendPage".equals(action)) {
                    request.getRequestDispatcher(SEND_PAGE).forward(request, response);
                    return;
                }
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
                response.sendRedirect(LOGIN_URL);
                return;
            }

            // ✅ SỬA: Xử lý cả "viewNotifications", "viewNoti", "list", null
            if (action == null || "list".equals(action)
                    || "viewNoti".equals(action)
                    || "viewNotifications".equals(action)) {

                List<NotificationDTO> list = dao.getNotificationsByUserId(user.getUserId());
                // ✅ SỬA: Set cả 2 tên để JSP không bị lỗi dù dùng tên nào
                request.setAttribute("NOTI_LIST", list);
                request.setAttribute("NOTIFICATIONS_LIST", list);
                request.getRequestDispatcher(NOTI_PAGE).forward(request, response);

            } else if ("markRead".equals(action) || "readNoti".equals(action)) {
                String notiIdStr = request.getParameter("notiId");
                if (notiIdStr == null) notiIdStr = request.getParameter("id");
                if (notiIdStr != null) {
                    dao.markAsRead(Integer.parseInt(notiIdStr));
                }
                response.sendRedirect("MainController?action=viewNotifications");

            } else if ("deleteNoti".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    dao.delete(Integer.parseInt(idStr));
                }
                response.sendRedirect("MainController?action=viewNotifications");
            }

        } catch (Exception e) {
            log("Error at NotificationController: " + e.toString());
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