package controller;

import dao.CategoryDAO;
import dto.CategoryDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CategoryController", urlPatterns = {"/CategoryController"})
public class CategoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // ✅ Đọc action từ attribute (do MainController set) hoặc parameter
        String action = (String) request.getAttribute("action");
        if (action == null) {
            action = request.getParameter("action");
        }

        CategoryDAO dao = new CategoryDAO();
        HttpSession session = request.getSession();

        // Kiểm tra quyền: chỉ Admin (1) hoặc Manager (2)
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("MainController?action=login");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                List<CategoryDTO> list = dao.readAll();
                request.setAttribute("CATEGORY_LIST", list);
                request.getRequestDispatcher("/WEB-INF/views/admin/manage-categories.jsp").forward(request, response);

            } else if (action.equals("add")) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int status = Integer.parseInt(request.getParameter("status"));

                CategoryDTO dto = new CategoryDTO(0, name, description, status);
                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm chuyên mục mới thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Thêm thất bại, vui lòng thử lại.");
                }
                response.sendRedirect("MainController?action=manageCategories");

            } else if (action.equals("edit")) {
                int id = Integer.parseInt(request.getParameter("id"));
                CategoryDTO category = dao.readById(id);
                if (category != null) {
                    request.setAttribute("CATEGORY_DETAIL", category);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-category.jsp").forward(request, response);
                }

            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int status = Integer.parseInt(request.getParameter("status"));

                CategoryDTO dto = new CategoryDTO(id, name, description, status);
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật chuyên mục thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại.");
                }
                response.sendRedirect("MainController?action=manageCategories");

            } else if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa chuyên mục thành công.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa chuyên mục đang có sách liên kết!");
                }
                response.sendRedirect("MainController?action=manageCategories");
            }

        } catch (Exception e) {
            log("Error at CategoryController: " + e.toString());
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