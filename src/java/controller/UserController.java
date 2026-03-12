package controller;

import dao.UserDAO;
import dto.UserDTO;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String url = "WEB-INF/views/web/login.jsp";

        try {

            if ("login".equals(action)) {

                String username = request.getParameter("username");
                String password = request.getParameter("password");

                UserDAO dao = new UserDAO();
                UserDTO user = dao.login(username, password);

                if (user != null) {

                    HttpSession session = request.getSession();
                    session.setAttribute("LOGIN_USER", user);

                    url = "WEB-INF/views/web/home.jsp";

                } else {

                    request.setAttribute("ERROR", "Invalid username or password");
                }
            }

        } catch (Exception e) {
            log("Error at UserController: " + e.toString());
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}