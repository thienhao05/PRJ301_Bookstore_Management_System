package controller;

import dao.CategoryDAO;
import dto.CategoryDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CategoryController extends HttpServlet {

    private CategoryDAO dao;

    @Override
    public void init() {
        dao = new CategoryDAO();
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
                    dao.deleteCategory(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect("CategoryController");
                break;

            case "list":
            default:
                List<CategoryDTO> list = dao.getAllCategories();
                request.setAttribute("list", list);
                request.getRequestDispatcher("manage-categories.jsp")
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
            int status = Integer.parseInt(request.getParameter("status"));

            if ("add".equals(action)) {

                CategoryDTO c = new CategoryDTO(0, name, description, status);
                dao.addCategory(c);

            } else if ("update".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));
                CategoryDTO c = new CategoryDTO(id, name, description, status);
                dao.updateCategory(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("CategoryController");
    }
}