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

        // KIỂM TRA QUYỀN: Admin (1) hoặc Manager (2)
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null || (user.getRoleId() != 1 && user.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("MainController?action=login"); // ✅ SỬA
            return;
        }

        try {
            if (action == null || action.equals("managePublishers")) {
                List<PublisherDTO> list = dao.readAll();
                request.setAttribute("PUBLISHER_LIST", list);
                request.getRequestDispatcher("/WEB-INF/views/admin/manage-publishers.jsp").forward(request, response);

            } else if (action.equals("addPublisher")) {
                String name = request.getParameter("name");
                String address = request.getParameter("address");

                PublisherDTO dto = new PublisherDTO(0, name, address);
                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm nhà xuất bản thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Thêm thất bại, vui lòng thử lại.");
                }
                response.sendRedirect("MainController?action=managePublishers");

            } else if (action.equals("editPublisher")) {
                int id = Integer.parseInt(request.getParameter("id"));
                PublisherDTO publisher = dao.readById(id);
                if (publisher != null) {
                    request.setAttribute("PUBLISHER_DETAIL", publisher);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-publisher.jsp").forward(request, response);
                } else {
                    response.sendRedirect("MainController?action=managePublishers");
                }

            } else if (action.equals("updatePublisher")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String address = request.getParameter("address"); // ✅ SỬA (trước dùng nhầm 'action')

                PublisherDTO dto = new PublisherDTO(id, name, address);
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật NXB thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại.");
                }
                response.sendRedirect("MainController?action=managePublishers");

            } else if (action.equals("deletePublisher")) {
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