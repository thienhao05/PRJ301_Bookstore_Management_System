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
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirm)) {
            request.setAttribute("MSG_ERROR", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }

        // Kiểm tra email đã tồn tại chưa
        if (userDAO.checkEmailExist(email)) {
            request.setAttribute("MSG_ERROR", "Email này đã được sử dụng!");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }

        // Tạo user mới
        UserDTO newUser = new UserDTO();
        newUser.setUsername(fullName);
        newUser.setEmail(email);
        newUser.setPassword(PasswordUtil.hashPassword(password));
        newUser.setRoleId(3); // 3 = Customer
        newUser.setStatus(true);

        boolean isCreated = userDAO.create(newUser);
        if (isCreated) {
            // Tự động tạo giỏ hàng cho user mới
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
