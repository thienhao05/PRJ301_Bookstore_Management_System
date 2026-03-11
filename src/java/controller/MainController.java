package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        // Lấy action từ form (ví dụ: action=Login, action=Logout)
        String action = request.getParameter("action");
        String url = "login.jsp"; // Mặc định nếu không có action thì đá về trang login

        try {
            if (action == null) {
                url = "login.jsp";
            } // Nếu action thuộc nhóm User -> Chuyển trạm sang UserController
            else if (action.equals("Login") || action.equals("Logout") || action.equals("Register")) {
                url = "UserController";
            }
            // Sau này ông có thêm Book thì làm như sau:
            // else if (action.equals("SearchBook") || action.equals("ViewBook")) {
            //     url = "BookController";
            // }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Chuyển tiếp (forward) request và response sang đúng Controller tương ứng
            request.getRequestDispatcher(url).forward(request, response);
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
