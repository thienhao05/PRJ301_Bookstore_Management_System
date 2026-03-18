package controller;

import dao.StaffDAO;
import dao.UserDAO;
import dao.ShiftDAO;
import dto.StaffDTO;
import dto.UserDTO;
import dto.ShiftDTO;
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
        ShiftDAO shiftDao = new ShiftDAO();
        HttpSession session = request.getSession();

        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("MainController?action=login");
            return;
        }

        try {
            if (action == null || "manageStaffs".equals(action)) {
                List<StaffDTO> staffList = staffDao.readAll();
                List<ShiftDTO> shiftList = shiftDao.readAll();
                request.setAttribute("STAFF_LIST", staffList);
                request.setAttribute("SHIFT_LIST", shiftList);
                request.getRequestDispatcher("/WEB-INF/views/admin/manage-staff.jsp").forward(request, response);

            } else if ("addStaff".equals(action)) {
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                double salary = Double.parseDouble(request.getParameter("salary"));
                String hireDate = request.getParameter("hireDate");

                UserDTO userStaff = new UserDTO();
                userStaff.setUsername(fullName);
                userStaff.setEmail(email);
                userStaff.setPassword(PasswordUtil.hashPassword(password));
                userStaff.setRoleId(3);
                userStaff.setStatus(true);

                if (userDao.create(userStaff)) {
                    UserDTO createdUser = userDao.getUserByEmail(email);
                    if (createdUser != null) {
                        StaffDTO staff = new StaffDTO(0, createdUser.getUserId(), Date.valueOf(hireDate), salary, "Active");
                        staffDao.create(staff);
                        session.setAttribute("MSG_SUCCESS", "Nhân viên " + fullName + " đã sẵn sàng làm việc!");
                    }
                } else {
                    session.setAttribute("MSG_ERROR", "Email này đã có người dùng rồi!");
                }
                response.sendRedirect("MainController?action=manageStaffs");

            } else if ("editStaff".equals(action)) {
                int staffId = Integer.parseInt(request.getParameter("id"));
                StaffDTO staff = staffDao.readById(staffId);
                request.setAttribute("STAFF_DETAIL", staff);
                request.getRequestDispatcher("/WEB-INF/views/admin/edit-staff.jsp").forward(request, response);

            } else if ("updateStaff".equals(action)) {
                int staffId = Integer.parseInt(request.getParameter("staffId"));
                double salary = Double.parseDouble(request.getParameter("salary"));
                String hireDateStr = request.getParameter("hireDate");
                String status = request.getParameter("status");

                StaffDTO staff = staffDao.readById(staffId);
                if (staff != null) {
                    staff.setSalary(salary);
                    staff.setHire_date(Date.valueOf(hireDateStr));
                    staff.setStatus(status);
                    if (staffDao.update(staff)) {
                        session.setAttribute("MSG_SUCCESS", "Đã cập nhật hồ sơ nhân sự.");
                    }
                }
                response.sendRedirect("MainController?action=manageStaffs");

            } else if ("deleteStaff".equals(action)) {
                int staffId = Integer.parseInt(request.getParameter("id"));
                if (staffDao.delete(staffId)) {
                    session.setAttribute("MSG_SUCCESS", "Nhân viên đã nghỉ việc.");
                }
                response.sendRedirect("MainController?action=manageStaffs");
            }

        } catch (Exception e) {
            log("Error at StaffController: " + e.toString());
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