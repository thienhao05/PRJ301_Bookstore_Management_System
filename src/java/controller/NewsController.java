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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        NewsDAO dao = new NewsDAO();
        HttpSession session = request.getSession();

        try {
            // 1. DÀNH CHO KHÁCH HÀNG: XEM DANH SÁCH TIN TỨC
            if (action == null || action.equals("view")) {
                List<NewsDTO> list = dao.readAll();
                request.setAttribute("NEWS_LIST", list);
                request.getRequestDispatcher("news.jsp").forward(request, response);

                // 2. XEM CHI TIẾT MỘT BÀI VIẾT
            } else if (action.equals("detail")) {
                int id = Integer.parseInt(request.getParameter("id"));
                NewsDTO news = dao.readById(id);
                if (news != null) {
                    request.setAttribute("NEWS_DETAIL", news);
                    request.getRequestDispatcher("news-detail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("error-404.jsp");
                }

            } else {
                // KIỂM TRA QUYỀN: Chỉ Admin (1) hoặc Manager (2) mới được quản lý
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (action.equals("manage")) {
                    // 3. ADMIN: QUẢN LÝ TIN TỨC
                    List<NewsDTO> list = dao.readAll();
                    request.setAttribute("NEWS_LIST", list);
                    request.getRequestDispatcher("admin/manage-news.jsp").forward(request, response);

                } else if (action.equals("add")) {
                    // 4. THÊM BÀI VIẾT MỚI
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    NewsDTO dto = new NewsDTO();
                    dto.setTitle(title);
                    dto.setContent(content);
                    // Set staff_id từ User đang đăng nhập
                    dto.setStaff_id(loginUser.getUserId());

                    if (dao.create(dto)) {
                        session.setAttribute("MSG_SUCCESS", "Đăng bài viết mới thành công!");
                    } else {
                        session.setAttribute("MSG_ERROR", "Không thể đăng bài viết.");
                    }
                    response.sendRedirect("NewsController?action=manage");

                } else if (action.equals("edit")) {
                    // 5. LẤY DỮ LIỆU ĐỂ HIỂN THỊ FORM SỬA
                    int id = Integer.parseInt(request.getParameter("id"));
                    NewsDTO news = dao.readById(id);
                    if (news != null) {
                        request.setAttribute("NEWS_DETAIL", news);
                        request.getRequestDispatcher("admin/edit-news.jsp").forward(request, response);
                    }

                } else if (action.equals("update")) {
                    // 6. CẬP NHẬT BÀI VIẾT
                    int id = Integer.parseInt(request.getParameter("id"));
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    NewsDTO dto = dao.readById(id);
                    if (dto != null) {
                        dto.setTitle(title);
                        dto.setContent(content);
                        // Giữ nguyên staff_id cũ hoặc cập nhật người sửa mới nhất tùy bạn
                        dto.setStaff_id(loginUser.getUserId());

                        if (dao.update(dto)) {
                            session.setAttribute("MSG_SUCCESS", "Cập nhật bài viết thành công!");
                        } else {
                            session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                        }
                    }
                    response.sendRedirect("NewsController?action=manage");

                } else if (action.equals("delete")) {
                    // 7. XÓA BÀI VIẾT
                    int id = Integer.parseInt(request.getParameter("id"));
                    if (dao.delete(id)) {
                        session.setAttribute("MSG_SUCCESS", "Đã xóa bài viết.");
                    } else {
                        session.setAttribute("MSG_ERROR", "Xóa bài viết thất bại.");
                    }
                    response.sendRedirect("NewsController?action=manage");
                } else if ("updateNews".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    NewsDTO dto = dao.readById(id); // Lấy đối tượng cũ lên
                    if (dto != null) {
                        dto.setTitle(title);
                        dto.setContent(content);

                        if (dao.update(dto)) {
                            session.setAttribute("MSG_SUCCESS", "Cập nhật bài viết thành công!");
                        } else {
                            session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                        }
                    }
                    response.sendRedirect("MainController?action=manageNews");
                } else if ("updateNews".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");

                    NewsDTO dto = dao.readById(id); // Lấy đối tượng cũ lên
                    if (dto != null) {
                        dto.setTitle(title);
                        dto.setContent(content);

                        if (dao.update(dto)) {
                            session.setAttribute("MSG_SUCCESS", "Cập nhật bài viết thành công!");
                        } else {
                            session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                        }
                    }
                    response.sendRedirect("MainController?action=manageNews");
                }
            }

        } catch (Exception e) {
            log("Error at NewsController: " + e.toString());
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
