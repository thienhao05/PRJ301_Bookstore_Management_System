package controller;

import dao.PublisherDAO;
import dto.PublisherDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PublisherController", urlPatterns = {"/PublisherController"})
public class PublisherController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        PublisherDAO dao = new PublisherDAO();
        HttpSession session = request.getSession();

        // BẢO MẬT: Chỉ Admin hoặc Manager mới có quyền quản lý Nhà xuất bản
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 1. XEM DANH SÁCH NHÀ XUẤT BẢN
                List<PublisherDTO> list = dao.readAll();
                request.setAttribute("PUBLISHER_LIST", list);
                request.getRequestDispatcher("admin/manage-publishers.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 2. THÊM MỚI
                String name = request.getParameter("name");
                String description = request.getParameter("description"); // Map vào cột address trong DB

                PublisherDTO dto = new PublisherDTO(0, name, description);
                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm nhà xuất bản thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Có lỗi xảy ra, vui lòng thử lại.");
                }
                response.sendRedirect("PublisherController?action=list");

            } else if (action.equals("edit")) {
                // 3. LẤY THÔNG TIN ĐỂ SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                PublisherDTO publisher = dao.readById(id);
                if (publisher != null) {
                    request.setAttribute("PUBLISHER_DETAIL", publisher);
                    request.getRequestDispatcher("admin/edit-publisher.jsp").forward(request, response);
                }

            } else if (action.equals("update")) {
                // 4. CẬP NHẬT
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");

                PublisherDTO dto = new PublisherDTO(id, name, description);
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại.");
                }
                response.sendRedirect("PublisherController?action=list");

            } else if (action.equals("delete")) {
                // 5. XÓA
                int id = Integer.parseInt(request.getParameter("id"));
                // Chú ý: Nếu có sách thuộc NXB này, SQL sẽ chặn xóa (Foreign Key)
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa nhà xuất bản.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa nhà xuất bản đang có sách liên kết!");
                }
                response.sendRedirect("PublisherController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at PublisherController: " + e.toString());
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