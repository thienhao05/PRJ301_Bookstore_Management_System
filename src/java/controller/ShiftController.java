package controller;

import dao.ShiftDAO;
import dao.StaffDAO;
import dto.ShiftDTO;
import dto.StaffDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ShiftController", urlPatterns = {"/ShiftController"})
public class ShiftController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Ưu tiên lấy action từ Attribute (do MainController map) trước, sau đó mới lấy từ Parameter
        String action = (String) request.getAttribute("action");
        if (action == null) {
            action = request.getParameter("action");
        }

        ShiftDAO shiftDao = new ShiftDAO();
        StaffDAO staffDao = new StaffDAO();
        HttpSession session = request.getSession();

        // Kiểm tra quyền đăng nhập
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền quản lý lịch làm việc!");
            response.sendRedirect("MainController?action=login");
            return;
        }

        try {
            // Trường hợp: Xem danh sách ca làm (manageShifts)
            if (action == null || "list".equals(action) || "manageShifts".equals(action)) {
                List<ShiftDTO> list = shiftDao.readAll();
                request.setAttribute("SHIFT_LIST", list);
                // Đảm bảo đường dẫn file JSP này chính xác trong thư mục của bạn
                request.getRequestDispatcher("/WEB-INF/views/admin/manage-shifts.jsp").forward(request, response);

            } else if ("prepare_add".equals(action)) {
                List<StaffDTO> staffList = staffDao.readAll();
                request.setAttribute("STAFF_LIST", staffList);
                request.getRequestDispatcher("/WEB-INF/views/admin/add-shift.jsp").forward(request, response);

            } else if ("add".equals(action)) {
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String shiftDate = request.getParameter("shiftDate");

                // Giả định ShiftDTO có constructor phù hợp
                ShiftDTO dto = new ShiftDTO(0, staffId, startTime, endTime, java.sql.Date.valueOf(shiftDate));
                if (shiftDao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Phân ca thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Lỗi: Nhân viên đã có ca trùng vào thời gian này.");
                }
                response.sendRedirect("MainController?action=manageShifts");

            } else if ("edit".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    ShiftDTO shift = shiftDao.readById(id);
                    List<StaffDTO> staffList = staffDao.readAll();
                    request.setAttribute("SHIFT_DETAIL", shift);
                    request.setAttribute("STAFF_LIST", staffList);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-shift.jsp").forward(request, response);
                }

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String shiftDate = request.getParameter("shiftDate");

                ShiftDTO dto = new ShiftDTO(id, staffId, startTime, endTime, java.sql.Date.valueOf(shiftDate));
                if (shiftDao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật lịch trực thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                }
                response.sendRedirect("MainController?action=manageShifts");

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (shiftDao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa ca trực.");
                }
                response.sendRedirect("MainController?action=manageShifts");
            }

        } catch (Exception e) {
            log("Error at ShiftController: " + e.toString());
            // Nếu có trang error-500.jsp thì dùng, không thì bỏ dòng này để thấy lỗi chi tiết trên console
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