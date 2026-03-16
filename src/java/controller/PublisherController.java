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

        // 1. KIỂM TRA QUYỀN (ADMIN MỚI ĐƯỢC QUẢN LÝ NXB)
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null || user.getRoleId() != 1) { // Giả sử Role 1 là Admin
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // MẶC ĐỊNH HOẶC XEM DANH SÁCH
            if (action == null || action.equals("managePublishers")) {
                List<PublisherDTO> list = dao.readAll();
                request.setAttribute("PUBLISHER_LIST", list);
                request.getRequestDispatcher("admin/manage-publishers.jsp").forward(request, response);

            } else if (action.equals("addPublisher")) {
                // THÊM MỚI: Map "address" từ giao diện vào "description" của DTO
                String name = request.getParameter("name");
                String address = request.getParameter("address");

                // PublisherDTO(id, name, description)
                // Truyền 'address' vào vị trí 'description' để khớp với DAO
                PublisherDTO dto = new PublisherDTO(0, name, address);

                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm nhà xuất bản thành công!");
                }
                response.sendRedirect("MainController?action=managePublishers");

            } else if (action.equals("editPublisher")) {
                // 3. LẤY DỮ LIỆU ĐỂ SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                PublisherDTO publisher = dao.readById(id);
                if (publisher != null) {
                    request.setAttribute("PUBLISHER_DETAIL", publisher);
                    request.getRequestDispatcher("admin/edit-publisher.jsp").forward(request, response);
                } else {
                    response.sendRedirect("MainController?action=managePublishers");
                }

            } else if (action.equals("updatePublisher")) {
                // 4. CẬP NHẬT THAY ĐỔI
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");

                PublisherDTO dto = new PublisherDTO(id, name, action);
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật NXB thành công!");
                }
                response.sendRedirect("MainController?action=managePublishers");

            } else if (action.equals("deletePublisher")) {
                // 5. XÓA NXB
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa nhà xuất bản.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa (có thể do ràng buộc dữ liệu).");
                }
                response.sendRedirect("MainController?action=managePublishers");
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
