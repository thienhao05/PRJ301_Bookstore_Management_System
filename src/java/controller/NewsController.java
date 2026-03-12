package controller;

import dao.NewsDAO;
import dto.NewsDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class NewsController extends HttpServlet {

    NewsDAO dao = new NewsDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) action = "list";

        switch (action) {

            case "delete":

                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteNews(id);
                response.sendRedirect("NewsController");

                break;

            default:

                List<NewsDTO> list = dao.getAllNews();
                request.setAttribute("list", list);
                request.getRequestDispatcher("manage-news.jsp")
                        .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        int staff_id = 1;

        if ("add".equals(action)) {

            NewsDTO n =
                    new NewsDTO(0, title, content, new Timestamp(System.currentTimeMillis()), staff_id);

            dao.addNews(n);

        } else if ("update".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));

            NewsDTO n =
                    new NewsDTO(id, title, content, new Timestamp(System.currentTimeMillis()), staff_id);

            dao.updateNews(n);
        }

        response.sendRedirect("NewsController");
    }
}