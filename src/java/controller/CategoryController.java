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
        
        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();
        HttpSession session = request.getSession();

        // 1. BẢO MẬT: Chỉ Admin (1) hoặc Manager (2) mới được quản lý chuyên mục
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 2. XEM DANH SÁCH (READ)
                List<CategoryDTO> list = dao.readAll(); // Đổi tên hàm cho khớp chuẩn ICRUD
                request.setAttribute("CATEGORY_LIST", list);
                request.getRequestDispatcher("admin/manage-categories.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 3. THÊM MỚI (CREATE)
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                int status = Integer.parseInt(request.getParameter("status"));

                CategoryDTO dto = new CategoryDTO(0, name, description, status);
                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm chuyên mục mới thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Thêm thất bại, vui lòng thử lại.");
                }
                response.sendRedirect("CategoryController?action=list");

            } else if (action.equals("edit")) {
                // 4. LẤY DỮ LIỆU ĐỂ SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                CategoryDTO category = dao.readById(id);
                if (category != null) {
                    request.setAttribute("CATEGORY_DETAIL", category);
                    request.getRequestDispatcher("admin/edit-category.jsp").forward(request, response);
                }

            } else if (action.equals("update")) {
                // 5. CẬP NHẬT (UPDATE)
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
                response.sendRedirect("CategoryController?action=list");

            } else if (action.equals("delete")) {
                // 6. XÓA (DELETE)
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa chuyên mục thành công.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa chuyên mục đang có sách liên kết!");
                }
                response.sendRedirect("CategoryController?action=list");
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