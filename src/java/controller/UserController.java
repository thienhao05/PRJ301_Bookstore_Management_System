package controller;

import dao.CartDAO;
import dao.UserDAO;
import dto.CartDTO;
import dto.UserDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.PasswordUtil;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    private static final String LOGIN_VIEW = "/WEB-INF/views/web/login.jsp";
    private static final String LOGIN_URL = "MainController?action=login";
    private static final String REGISTER_PAGE = "/WEB-INF/views/web/register.jsp";
    private static final String PROFILE_PAGE = "/WEB-INF/views/user/profile.jsp";

    private final UserDAO userDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

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
                    doForgotPassword(request, response); // THÊM
                    break;
                case "resetPassword":
                    doResetPassword(request, response);  // THÊM
                    break;
                default:
                    request.getRequestDispatcher(LOGIN_VIEW)
                            .forward(request, response);
                    break;
            }
        } catch (Exception e) {
            log("Error at UserController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp")
                    .forward(request, response);
        }
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

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String hashedPassword = PasswordUtil.hashPassword(password);
        UserDTO user = userDAO.login(email, hashedPassword);

        if (user == null) {
            request.setAttribute("MSG_ERROR",
                    "Email hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa!");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }

        session.setAttribute("LOGIN_USER", user);
        session.setAttribute("MSG_SUCCESS", "Chào mừng " + user.getUsername() + "!");

        // PHÂN LUỒNG ĐÚNG THEO ROLE THẬT
        // roleId = 1 (Admin), 2 (Manager), 3 (Staff) -> Dashboard
        // roleId = 4 (Customer) -> Trang chủ
        if (user.getRoleId() == 1 || user.getRoleId() == 2 || user.getRoleId() == 3) {
            response.sendRedirect("MainController?action=dashboard");
        } else {
            // roleId = 4 -> Customer
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

    private void doRegister(HttpServletRequest request, HttpServletResponse response,
            HttpSession session) throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm"); // khớp với name="confirm" trong form

        // Debug tạm - xóa sau khi fix xong
        log("DEBUG register - password: " + password + ", confirm: " + confirm);

        // Validate null
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
        newUser.setRoleId(4); // Customer
        newUser.setStatus(true);

        boolean isCreated = userDAO.create(newUser);
        if (isCreated) {
            UserDTO createdUser = userDAO.getUserByEmail(email);
            if (createdUser != null) {
                new CartDAO().create(new CartDTO(0, createdUser.getUserId(), null));
            }
            session.setAttribute("MSG_SUCCESS",
                    "Đăng ký thành công! Hãy đăng nhập để mua sắm nhé.");
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

    private static final String FORGOT_PASSWORD_PAGE = "/WEB-INF/views/web/forgot-password.jsp";

// ----------------------------------------------------------------
// FORGOT PASSWORD - Hiện form nhập email + mật khẩu mới
// ----------------------------------------------------------------
    private void doForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }

        // POST -> chuyển sang resetPassword
        response.sendRedirect("MainController?action=forgotPassword");
    }

// ----------------------------------------------------------------
// RESET PASSWORD - Xử lý đặt lại mật khẩu
// ----------------------------------------------------------------
    private void doResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirm = request.getParameter("confirm");

        // Validate
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("MSG_ERROR", "Vui lòng nhập email!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }

        // Kiểm tra email có tồn tại không
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

        // Lấy user theo email
        UserDTO user = userDAO.getUserByEmail(email);
        if (user == null) {
            request.setAttribute("MSG_ERROR", "Không tìm thấy tài khoản!");
            request.getRequestDispatcher(FORGOT_PASSWORD_PAGE).forward(request, response);
            return;
        }

        // Cập nhật mật khẩu mới
        user.setPassword(PasswordUtil.hashPassword(newPassword));
        boolean isUpdated = userDAO.update(user);

        if (isUpdated) {
            HttpSession session = request.getSession();
            session.setAttribute("MSG_SUCCESS",
                    "Đặt lại mật khẩu thành công! Hãy đăng nhập với mật khẩu mới.");
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
