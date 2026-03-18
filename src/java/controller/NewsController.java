package controller;

import dao.NewsDAO;
import dto.NewsDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "NewsController", urlPatterns = {"/NewsController"})
public class NewsController extends HttpServlet {

    private static final String NEWS_PAGE = "/WEB-INF/views/web/news.jsp";
    private static final String NEWS_DETAIL_PAGE = "/WEB-INF/views/web/news-detail.jsp";
    private static final String MANAGE_NEWS = "/WEB-INF/views/admin/manage-news.jsp";
    private static final String EDIT_NEWS = "/WEB-INF/views/admin/edit-news.jsp";
    private static final String ERROR_404 = "/WEB-INF/views/web/error-404.jsp";
    private static final String ERROR_500 = "/WEB-INF/views/web/error-500.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "viewNews";
        }

        NewsDAO newsDao = new NewsDAO();
        HttpSession session = request.getSession();

        try {
            switch (action) {

                // ================================================
                // DÀNH CHO TẤT CẢ
                // ================================================
                case "viewNews": {
                    List<NewsDTO> list = newsDao.readAll();
                    request.setAttribute("NEWS_LIST", list);
                    request.getRequestDispatcher(NEWS_PAGE).forward(request, response);
                    break;
                }

                case "newsDetail": {
                    String idStr = request.getParameter("id");
                    if (idStr == null) {
                        response.sendRedirect("MainController?action=viewNews");
                        return;
                    }
                    int id = Integer.parseInt(idStr);
                    NewsDTO news = newsDao.readById(id);

                    if (news != null) {
                        // Lấy tin liên quan (trừ tin hiện tại)
                        List<NewsDTO> recentList = newsDao.getRecentNews(6);
                        recentList.removeIf(n -> n.getNews_id() == id);
                        if (recentList.size() > 4) {
                            recentList = recentList.subList(0, 4);
                        }
                        request.setAttribute("NEWS_DETAIL", news);
                        request.setAttribute("RELATED_NEWS", recentList);
                        request.getRequestDispatcher(NEWS_DETAIL_PAGE)
                                .forward(request, response);
                    } else {
                        request.getRequestDispatcher(ERROR_404).forward(request, response);
                    }
                    break;
                }

                // ================================================
                // DÀNH CHO ADMIN / MANAGER / STAFF
                // ================================================
                case "manageNews": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    List<NewsDTO> list = newsDao.readAll();
                    request.setAttribute("NEWS_LIST", list);
                    request.getRequestDispatcher(MANAGE_NEWS).forward(request, response);
                    break;
                }

                case "addNews": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");

                    NewsDTO dto = new NewsDTO();
                    dto.setTitle(title);
                    dto.setContent(content);
                    dto.setStaff_id(loginUser.getUserId());

                    if (newsDao.create(dto)) {
                        session.setAttribute("MSG_SUCCESS", "Đăng bài viết thành công!");
                    } else {
                        session.setAttribute("MSG_ERROR", "Đăng bài viết thất bại!");
                    }
                    response.sendRedirect("MainController?action=manageNews");
                    break;
                }

                case "editNews": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    NewsDTO news = newsDao.readById(id);
                    if (news != null) {
                        request.setAttribute("NEWS_DETAIL", news);
                        request.getRequestDispatcher(EDIT_NEWS).forward(request, response);
                    } else {
                        response.sendRedirect("MainController?action=manageNews");
                    }
                    break;
                }

                case "updateNews": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    NewsDTO dto = newsDao.readById(id);
                    if (dto != null) {
                        dto.setTitle(title);
                        dto.setContent(content);
                        if (newsDao.update(dto)) {
                            session.setAttribute("MSG_SUCCESS", "Cập nhật bài viết thành công!");
                        } else {
                            session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                        }
                    }
                    response.sendRedirect("MainController?action=manageNews");
                    break;
                }

                case "deleteNews": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }

                    int id = Integer.parseInt(request.getParameter("id"));
                    UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                    NewsDTO news = newsDao.readById(id);

                    if (news == null) {
                        session.setAttribute("MSG_ERROR", "Bài viết không tồn tại!");
                        response.sendRedirect("MainController?action=manageNews");
                        return;
                    }

                    // Staff (role 3) chỉ được xóa tin của mình
                    // Admin (1) và Manager (2) xóa được tất cả
                    if (loginUser.getRoleId() == 3
                            && news.getStaff_id() != loginUser.getUserId()) {
                        session.setAttribute("MSG_ERROR",
                                "Bạn chỉ được xóa bài viết do chính mình đăng!");
                        response.sendRedirect("MainController?action=manageNews");
                        return;
                    }

                    if (newsDao.delete(id)) {
                        session.setAttribute("MSG_SUCCESS", "Đã xóa bài viết.");
                    } else {
                        session.setAttribute("MSG_ERROR", "Xóa bài viết thất bại!");
                    }
                    response.sendRedirect("MainController?action=manageNews");
                    break;
                }

                default:
                    response.sendRedirect("MainController?action=viewNews");
            }

        } catch (Exception e) {
            log("Error at NewsController: " + e.toString());
            request.getRequestDispatcher(ERROR_500).forward(request, response);
        }
    }

    // ---- Helper: kiểm tra quyền admin/manager/staff ----
    private boolean hasAdminAccess(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            return false;
        }
        int role = user.getRoleId();
        return role == 1 || role == 2 || role == 3;
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
