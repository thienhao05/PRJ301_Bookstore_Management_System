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

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String REGISTER_PAGE = "register.jsp";
    private static final String PROFILE_PAGE = "WEB-INF/views/user/profile.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        UserDAO userDao = new UserDAO();
        HttpSession session = request.getSession();

        try {
            // 1. ACTION: ĐĂNG NHẬP (LOGIN)
            if ("login".equals(action)) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                
                // Bước 1: Băm mật khẩu người dùng vừa nhập
                String hashedInput = PasswordUtil.hashPassword(password);
                
                // Bước 2: Truyền password đã băm xuống DAO để so khớp
                UserDTO user = userDao.login(email, hashedInput);

                if (user != null) {
                    session.setAttribute("LOGIN_USER", user);
                    
                    // Phân quyền điều hướng dựa trên roleId
                    if (user.getRoleId() == 1 || user.getRoleId() == 2) { // Admin/Manager
                        response.sendRedirect("admin/dashboard.jsp");
                    } else {
                        response.sendRedirect("index.jsp");
                    }
                } else {
                    request.setAttribute("MSG_ERROR", "Email hoặc mật khẩu không đúng!");
                    request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                }

            // 2. ACTION: ĐĂNG KÝ (REGISTER)
            } else if ("register".equals(action)) {
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String confirm = request.getParameter("confirm");

                if (!password.equals(confirm)) {
                    request.setAttribute("MSG_ERROR", "Mật khẩu xác nhận không khớp!");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                    return;
                }

                if (userDao.checkEmailExist(email)) {
                    request.setAttribute("MSG_ERROR", "Email này đã được sử dụng!");
                    request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                } else {
                    UserDTO newUser = new UserDTO();
                    newUser.setUsername(fullName);
                    newUser.setEmail(email);
                    
                    // BẢO MẬT: Băm mật khẩu trước khi lưu vào DB
                    newUser.setPassword(PasswordUtil.hashPassword(password));
                    
                    newUser.setRoleId(4); // Mặc định: Customer
                    newUser.setStatus(true);

                    boolean isCreated = userDao.create(newUser);
                    if (isCreated) {
                        // Tự động tạo Giỏ hàng cho khách mới
                        UserDTO createdUser = userDao.getUserByEmail(email);
                        if (createdUser != null) {
                            new CartDAO().create(new CartDTO(0, createdUser.getUserId(), null));
                        }
                        
                        session.setAttribute("MSG_SUCCESS", "Đăng ký thành công! Hãy đăng nhập nhé.");
                        response.sendRedirect(LOGIN_PAGE);
                    }
                }

            // 3. ACTION: XEM THÔNG TIN CÁ NHÂN (PROFILE)
            } else if ("profile".equals(action)) {
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                if (loginUser == null) {
                    response.sendRedirect(LOGIN_PAGE);
                    return;
                }
                request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);

            // 4. ACTION: ĐĂNG XUẤT (LOGOUT)
            } else if ("logout".equals(action)) {
                session.invalidate(); // Xóa sạch session
                response.sendRedirect("index.jsp");
            }

        } catch (Exception e) {
            log("Error at UserController: " + e.toString());
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