package controller;

import dao.StaffDAO;
import dao.UserDAO;
import dto.StaffDTO;
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
import utils.PasswordUtil;

@WebServlet(name = "StaffController", urlPatterns = {"/StaffController"})
public class StaffController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        StaffDAO staffDao = new StaffDAO();
        UserDAO userDao = new UserDAO();
        HttpSession session = request.getSession();

        // BẢO MẬT: Chỉ Admin (Role 1) mới được vào khu vực quản lý nhân sự
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || loginUser.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 1. XEM DANH SÁCH NHÂN VIÊN
                List<StaffDTO> list = staffDao.readAll();
                request.setAttribute("STAFF_LIST", list);
                request.getRequestDispatcher("admin/manage-staff.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 2. TẠO MỚI NHÂN VIÊN (Quy trình 2 bước)
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                double salary = Double.parseDouble(request.getParameter("salary"));
                String hireDate = request.getParameter("hireDate");

                // Bước A: Tạo User với Role = 3 (Staff)
                UserDTO userStaff = new UserDTO();
                userStaff.setUsername(fullName);
                userStaff.setEmail(email);
                userStaff.setPassword(PasswordUtil.hashPassword(password));
                userStaff.setRoleId(3); // Giả định Role 3 là Staff
                userStaff.setStatus(true);

                if (userDao.create(userStaff)) {
                    // Bước B: Lấy lại User vừa tạo để có UserID, sau đó tạo Staff record
                    UserDTO createdUser = userDao.getUserByEmail(email);
                    if (createdUser != null) {
                        StaffDTO staff = new StaffDTO();
                        staff.setUser_id(createdUser.getUserId());
                        staff.setSalary(salary);
                        staff.setHire_date(Date.valueOf(hireDate));
                        staff.setStatus("Active");
                        
                        staffDao.create(staff);
                        session.setAttribute("MSG_SUCCESS", "Hợp đồng mới đã được ký! Chào mừng nhân viên mới.");
                    }
                } else {
                    session.setAttribute("MSG_ERROR", "Email đã tồn tại hoặc lỗi hệ thống.");
                }
                response.sendRedirect("StaffController?action=list");

            } else if (action.equals("edit")) {
                // 3. LẤY THÔNG TIN ĐỂ SỬA
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                StaffDTO staff = staffDao.readById(staffId);
                request.setAttribute("STAFF_DETAIL", staff);
                request.getRequestDispatcher("admin/edit-staff.jsp").forward(request, response);

            } else if (action.equals("update")) {
                // 4. CẬP NHẬT (Lương, Ngày vào làm, Trạng thái)
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                double salary = Double.parseDouble(request.getParameter("salary"));
                String hireDate = request.getParameter("hireDate");
                String status = request.getParameter("status");

                StaffDTO staff = staffDao.readById(staffId);
                if (staff != null) {
                    staff.setSalary(salary);
                    staff.setHire_date(Date.valueOf(hireDate));
                    staff.setStatus(status);
                    
                    if (staffDao.update(staff)) {
                        session.setAttribute("MSG_SUCCESS", "Cập nhật thành công!");
                    }
                }
                response.sendRedirect("StaffController?action=list");

            } else if (action.equals("delete")) {
                // 5. CHO NGHỈ VIỆC
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                if (staffDao.delete(staffId)) {
                    session.setAttribute("MSG_SUCCESS", "Nhân viên đã nghỉ việc.");
                }
                response.sendRedirect("StaffController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at StaffController: " + e.toString());
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