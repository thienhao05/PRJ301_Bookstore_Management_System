package controller;

import dao.ShippingProviderDAO;
import dto.ShippingProviderDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ShippingProviderController", urlPatterns = {"/ShippingProviderController"})
public class ShippingProviderController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        ShippingProviderDAO dao = new ShippingProviderDAO();
        HttpSession session = request.getSession();
        
        // KIỂM TRA QUYỀN: Chỉ Admin (Role 1) hoặc Manager (Role 2) mới được quản lý đơn vị vận chuyển
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
            session.setAttribute("MSG_ERROR", "Bạn không có quyền truy cập khu vực này!");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 1. XEM DANH SÁCH
                List<ShippingProviderDTO> list = dao.readAll();
                request.setAttribute("SHIPPING_LIST", list);
                request.getRequestDispatcher("admin/manage-shipping.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 2. THÊM MỚI
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                double fee = Double.parseDouble(request.getParameter("fee"));

                ShippingProviderDTO dto = new ShippingProviderDTO(0, name, phone, fee);
                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm đơn vị vận chuyển thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Có lỗi xảy ra khi thêm mới.");
                }
                response.sendRedirect("ShippingProviderController?action=list");

            } else if (action.equals("edit")) {
                // 3. LẤY THÔNG TIN ĐỂ SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                ShippingProviderDTO provider = dao.readById(id);
                if (provider != null) {
                    request.setAttribute("SHIPPING_DETAIL", provider);
                    request.getRequestDispatcher("admin/edit-shipping.jsp").forward(request, response);
                }

            } else if (action.equals("update")) {
                // 4. CẬP NHẬT
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                double fee = Double.parseDouble(request.getParameter("fee"));

                ShippingProviderDTO dto = new ShippingProviderDTO(id, name, phone, fee);
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật thành công!");
                } else {
                    session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                }
                response.sendRedirect("ShippingProviderController?action=list");

            } else if (action.equals("delete")) {
                // 5. XÓA
                int id = Integer.parseInt(request.getParameter("id"));
                // Lưu ý: SQL sẽ báo lỗi nếu đơn vị này đã dính vào một đơn hàng cũ (Foreign Key)
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa đơn vị vận chuyển.");
                } else {
                    session.setAttribute("MSG_ERROR", "Không thể xóa đơn vị đang có đơn hàng liên kết!");
                }
                response.sendRedirect("ShippingProviderController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at ShippingProviderController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp").forward(request, response);
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