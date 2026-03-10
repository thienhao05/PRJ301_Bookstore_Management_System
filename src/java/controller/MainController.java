package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.UserDAO;
import model.UserDTO;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("Login".equals(action)) {

            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");

            if (email == null || password == null
                    || email.trim().isEmpty() || password.trim().isEmpty()) {

                request.setAttribute("ERROR", "Email and Password are required!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            UserDAO dao = new UserDAO();
            UserDTO user = dao.login(email.trim(), password.trim());

            if (user != null) {

                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", user);

                response.sendRedirect("home.jsp");

            } else {

                request.setAttribute("ERROR", "Invalid email or password!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } else if ("Logout".equals(action)) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect("login.jsp");
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
