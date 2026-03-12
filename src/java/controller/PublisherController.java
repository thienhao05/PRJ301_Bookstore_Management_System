package controller;

import dao.PublisherDAO;
import dto.PublisherDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class PublisherController extends HttpServlet {

    private PublisherDAO dao;

    @Override
    public void init() {
        dao = new PublisherDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    dao.deletePublisher(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect("PublisherController");
                break;

            case "list":
            default:
                List<PublisherDTO> list = dao.getAllPublishers();
                request.setAttribute("list", list);
                request.getRequestDispatcher("manage-publishers.jsp")
                        .forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {

            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if ("add".equals(action)) {

                PublisherDTO p = new PublisherDTO(0, name, description);
                dao.addPublisher(p);

            } else if ("update".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                PublisherDTO p = new PublisherDTO(id, name, description);
                dao.updatePublisher(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("PublisherController");
    }
}