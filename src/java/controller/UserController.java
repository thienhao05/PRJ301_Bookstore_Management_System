package controller;

import dao.UserDAO;  // Lưu ý: Nhớ import đúng package DAO của ông
import dto.UserDTO;  // Lưu ý: Nhớ import đúng package DTO của ông

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        try {
            if ("Login".equals(action)) {
                String email = request.getParameter("txtEmail");
                String password = request.getParameter("txtPassword");

                if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                    request.setAttribute("ERROR", "Email and Password are required!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                UserDAO dao = new UserDAO();
                // Nhớ đổi tên hàm login() hay checkLogin() cho khớp với file UserDAO của ông nhé
                UserDTO user = dao.checkLogin(email.trim(), password.trim());

                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("LOGIN_USER", user);
                    response.sendRedirect("home.jsp"); // Đăng nhập thành công -> về trang chủ
                } else {
                    request.setAttribute("ERROR", "Invalid email or password!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }

            } else if ("Logout".equals(action)) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate(); // Hủy session
                }
                response.sendRedirect("login.jsp"); // Đăng xuất xong -> về trang login
            }
            
            // Sau này code Đăng ký thì thêm: else if ("Register".equals(action)) { ... }
            
        } catch (Exception e) {
            e.printStackTrace();
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