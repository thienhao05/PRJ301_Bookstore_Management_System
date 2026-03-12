package controller;

import dao.ShippingProviderDAO;
import dto.ShippingProviderDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ShippingProviderController extends HttpServlet {

    ShippingProviderDAO dao = new ShippingProviderDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) action = "list";

        switch (action) {

            case "delete":

                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteShippingProvider(id);
                response.sendRedirect("ShippingProviderController");

                break;

            default:

                List<ShippingProviderDTO> list = dao.getAllShippingProviders();
                request.setAttribute("list", list);
                request.getRequestDispatcher("manage-shippingproviders.jsp")
                        .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        double fee = Double.parseDouble(request.getParameter("fee"));

        if ("add".equals(action)) {

            ShippingProviderDTO s =
                    new ShippingProviderDTO(0, name, phone, fee);

            dao.addShippingProvider(s);

        } else if ("update".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));

            ShippingProviderDTO s =
                    new ShippingProviderDTO(id, name, phone, fee);

            dao.updateShippingProvider(s);
        }

        response.sendRedirect("ShippingProviderController");
    }
}