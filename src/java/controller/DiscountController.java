package controller;

import dao.DiscountDAO;
import dto.DiscountDTO;
import dto.UserDTO;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DiscountController", urlPatterns = {"/DiscountController"})
public class DiscountController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // ✅ Đọc action từ attribute (do MainController set) hoặc parameter
        String action = (String) request.getAttribute("action");
        if (action == null) {
            action = request.getParameter("action");
        }

        DiscountDAO dao = new DiscountDAO();
        HttpSession session = request.getSession();

        // Kiểm tra quyền: chỉ Admin (1) hoặc Manager (2)
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền quản lý mã giảm giá!");
            response.sendRedirect("MainController?action=login");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                List<DiscountDTO> list = dao.readAll();
                request.setAttribute("DISCOUNT_LIST", list);
                request.getRequestDispatcher("/WEB-INF/views/admin/manage-discounts.jsp").forward(request, response);

            } else if (action.equals("add")) {
                String code = request.getParameter("code");
                int percentage = Integer.parseInt(request.getParameter("percentage"));
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");

                DiscountDTO dto = new DiscountDTO();
                dto.setCode(code);
                dto.setDiscount_percent(percentage);
                dto.setStart_date(Date.valueOf(startDate));
                dto.setEnd_date(Date.valueOf(endDate));
                dto.setStatus("Active");

                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Tạo mã giảm giá mới thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Mã này đã tồn tại hoặc lỗi hệ thống.");
                }
                response.sendRedirect("MainController?action=manageDiscounts");

            } else if (action.equals("edit")) {
                int id = Integer.parseInt(request.getParameter("id"));
                DiscountDTO discount = dao.readById(id);
                if (discount != null) {
                    request.setAttribute("DISCOUNT_DETAIL", discount);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-discount.jsp").forward(request, response);
                }

            } else if (action.equals("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String code = request.getParameter("code");
                int percentage = Integer.parseInt(request.getParameter("percentage"));
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("endDate");
                String status = request.getParameter("status");

                DiscountDTO dto = new DiscountDTO(id, code, percentage,
                        Date.valueOf(startDate), Date.valueOf(endDate), status);

                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật mã thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại.");
                }
                response.sendRedirect("MainController?action=manageDiscounts");

            } else if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa mã giảm giá.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa mã đã được sử dụng.");
                }
                response.sendRedirect("MainController?action=manageDiscounts");
            }

        } catch (Exception e) {
            log("Error at DiscountController: " + e.toString());
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