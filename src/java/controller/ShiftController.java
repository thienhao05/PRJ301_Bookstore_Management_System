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
        
        String action = request.getParameter("action");
        ShiftDAO shiftDao = new ShiftDAO();
        StaffDAO staffDao = new StaffDAO();
        HttpSession session = request.getSession();

        // BẢO MẬT: Chỉ Admin hoặc Manager mới được quyền sắp xếp ca làm việc
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền quản lý lịch làm việc!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 1. XEM DANH SÁCH CA TRỰC
                List<ShiftDTO> list = shiftDao.readAll();
                request.setAttribute("SHIFT_LIST", list);
                request.getRequestDispatcher("admin/manage-shifts.jsp").forward(request, response);

            } else if (action.equals("prepare_add")) {
                // 2. CHUẨN BỊ FORM THÊM (Cần list Staff để chọn)
                List<StaffDTO> staffList = staffDao.readAll();
                request.setAttribute("STAFF_LIST", staffList);
                request.getRequestDispatcher("admin/add-shift.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 3. XỬ LÝ THÊM CA MỚI
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String shiftDate = request.getParameter("shiftDate");

                ShiftDTO dto = new ShiftDTO(0, staffId, startTime, endTime, java.sql.Date.valueOf(shiftDate));
                if (shiftDao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Phân ca thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Lỗi: Nhân viên đã có ca trùng vào thời gian này.");
                }
                response.sendRedirect("ShiftController?action=list");

            } else if (action.equals("edit")) {
                // 4. LẤY THÔNG TIN ĐỂ SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                ShiftDTO shift = shiftDao.readById(id);
                List<StaffDTO> staffList = staffDao.readAll();
                
                request.setAttribute("SHIFT_DETAIL", shift);
                request.setAttribute("STAFF_LIST", staffList);
                request.getRequestDispatcher("admin/edit-shift.jsp").forward(request, response);

            } else if (action.equals("update")) {
                // 5. CẬP NHẬT CA TRỰC
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
                response.sendRedirect("ShiftController?action=list");

            } else if (action.equals("delete")) {
                // 6. XÓA CA TRỰC
                int id = Integer.parseInt(request.getParameter("id"));
                if (shiftDao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa ca trực.");
                }
                response.sendRedirect("ShiftController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at ShiftController: " + e.toString());
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