package controller;

import dao.CartDAO;
import dao.UserDAO;
import dto.CartDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.PasswordUtil;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private static final String LOGIN_VIEW       = "/WEB-INF/views/web/login.jsp";
    private static final String LOGIN_URL        = "MainController?action=login";
    private static final String REGISTER_PAGE    = "/WEB-INF/views/web/register.jsp";
    private static final String PROFILE_PAGE     = "/WEB-INF/views/user/profile.jsp";
    private static final String MANAGE_USERS_PAGE = "/WEB-INF/views/admin/manage-users.jsp";
    private static final String FORGOT_PASSWORD_PAGE = "/WEB-INF/views/web/forgot-password.jsp";

    private final UserDAO userDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "";

        HttpSession session = request.getSession();

        try {
            switch (action) {
                case "login":
                    doLogin(request, response, session);
                    break;
                case "logout":
                    doLogout(request, response, session);
                    break;
                case "register":
                    doRegister(request, response, session);
                    break;
                case "profile":
                    doProfile(request, response, session);
                    break;
                case "forgotPassword":
                    doForgotPassword(request, response);
                    break;
                case "resetPassword":
                    doResetPassword(request, response);
                    break;

                // ✅ THÊM: Quản lý người dùng
                case "manageUsers":
                case "list":
                    doManageUsers(request, response, session);
                    break;
                case "addUser":
                    doAddUser(request, response, session);
                    break;
                case "editUser":
                    doEditUser(request, response, session);
                    break;
                case "deleteUser":
                    doDeleteUser(request, response, session);
                    break;

                default:
                    request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
                    break;
            }
        } catch (Exception e) {
            log("Error at UserController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp").forward(request, response);
        }
    }

    // ----------------------------------------------------------------
    // MANAGE USERS (Admin/Manager only)
    // ----------------------------------------------------------------
    private void doManageUsers(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect(LOGIN_URL);
            return;
        }
        List<UserDTO> list = userDAO.readAll();
        request.setAttribute("USER_LIST", list);
        request.getRequestDispatcher(MANAGE_USERS_PAGE).forward(request, response);
    }

    private void doAddUser(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || loginUser.getRoleId() != 1) {
            response.sendRedirect(LOGIN_URL);
            return;
        }
        String fullName = request.getParameter("fullName") != null
                ? request.getParameter("fullName") : request.getParameter("username");
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        int roleId      = Integer.parseInt(request.getParameter("roleId"));

        UserDTO newUser = new UserDTO();
        newUser.setUsername(fullName);
        newUser.setEmail(email);
        newUser.setPassword(PasswordUtil.hashPassword(password));
        newUser.setRoleId(roleId);
        newUser.setStatus(true);

        if (userDAO.create(newUser)) {
            session.setAttribute("MSG_SUCCESS", "Tạo tài khoản mới thành công!");
        } else {
            session.setAttribute("MSG_ERROR", "Tạo thất bại, email có thể đã tồn tại.");
        }
        response.sendRedirect("MainController?action=manageUsers");
    }

    private void doEditUser(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || loginUser.getRoleId() != 1) {
            response.sendRedirect(LOGIN_URL);
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        UserDTO user = userDAO.readById(id);
        if (user != null) {
            request.setAttribute("EDIT_USER", user);
            request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);
        } else {
            response.sendRedirect("MainController?action=manageUsers");
        }
    }

    private void doDeleteUser(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || loginUser.getRoleId() != 1) {
            response.sendRedirect(LOGIN_URL);
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        if (userDAO.delete(id)) {
            session.setAttribute("MSG_SUCCESS", "Đã xóa tài khoản.");
        } else {
            session.setAttribute("MSG_ERROR", "Không thể xóa tài khoản này.");
        }
        response.sendRedirect("MainController?action=manageUsers");
    }

    // ----------------------------------------------------------------
    // LOGIN
    // ----------------------------------------------------------------
    private void doLogin(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = PasswordUtil.hashPassword(password);
        UserDTO user = userDAO.login(email, hashedPassword);

        if (user == null) {
            request.setAttribute("MSG_ERROR", "Email hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa!");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }
        session.setAttribute("LOGIN_USER", user);
        session.setAttribute("MSG_SUCCESS", "Chào mừng " + user.getUsername() + "!");
        if (user.getRoleId() == 1 || user.getRoleId() == 2 || user.getRoleId() == 3) {
            response.sendRedirect("MainController?action=dashboard");
        } else {
            response.sendRedirect("MainController?action=home");
        }
    }

    // ----------------------------------------------------------------
    // LOGOUT
    // ----------------------------------------------------------------
    private void doLogout(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws IOException {
        session.invalidate();
        response.sendRedirect("MainController?action=home");
    }

    // ----------------------------------------------------------------
    // REGISTER
    // ----------------------------------------------------------------
    private void doRegister(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        String fullName = request.getParameter("fullName");
        String email    = request.getParameter("email");
        String phone    = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirm  = request.getParameter("confirm");

        if (password == null || confirm == null) {
            request.setAttribute("MSG_ERROR", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        if (password.length() < 6) {
            request.setAttribute("MSG_ERROR", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        if (!password.equals(confirm)) {
            request.setAttribute("MSG_ERROR", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        if (userDAO.checkEmailExist(email)) {
            request.setAttribute("MSG_ERROR", "Email này đã được sử dụng!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        UserDTO newUser = new UserDTO();
        newUser.setUsername(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPassword(PasswordUtil.hashPassword(password));
        newUser.setRoleId(4);
        newUser.setStatus(true);

        boolean isCreated = userDAO.create(newUser);
        if (isCreated) {
            UserDTO createdUser = userDAO.getUserByEmail(email);
            if (createdUser != null) {
                new CartDAO().create(new CartDTO(0, createdUser.getUserId(), null));
            }
            session.setAttribute("MSG_SUCCESS", "Đăng ký thành công! Hãy đăng nhập để mua sắm nhé.");
            response.sendRedirect(LOGIN_URL);
        } else {
            request.setAttribute("MSG_ERROR", "Đăng ký thất bại, vui lòng thử lại!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
        }
    }

    // ----------------------------------------------------------------
    // PROFILE
    // ----------------------------------------------------------------
    private void doProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect(LOGIN_URL);
            return;
        }
        request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
    }

    // ----------------------------------------------------------------
    // FORGOT / RESET PASSWORD
    // ----------------------------------------------------------------
    private void doForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        response.sendRedirect("MainController?action=forgotPassword");
    }

    private void doResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email       = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirm     = request.getParameter("confirm");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("MSG_ERROR", "Vui lòng nhập email!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        if (!userDAO.checkEmailExist(email)) {
            request.setAttribute("MSG_ERROR", "Email này không tồn tại trong hệ thống!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("MSG_ERROR", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        if (!newPassword.equals(confirm)) {
            request.setAttribute("MSG_ERROR", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        UserDTO user = userDAO.getUserByEmail(email);
        if (user == null) {
            request.setAttribute("MSG_ERROR", "Không tìm thấy tài khoản!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }
        user.setPassword(PasswordUtil.hashPassword(newPassword));
        if (userDAO.update(user)) {
            HttpSession session = request.getSession();
            session.setAttribute("MSG_SUCCESS", "Đặt lại mật khẩu thành công! Hãy đăng nhập với mật khẩu mới.");
            response.sendRedirect("MainController?action=login");
        } else {
            request.setAttribute("MSG_ERROR", "Có lỗi xảy ra, vui lòng thử lại!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
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