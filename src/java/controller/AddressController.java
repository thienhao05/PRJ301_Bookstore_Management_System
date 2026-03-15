package controller;

import dao.AddressDAO;
import dto.AddressDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AddressController", urlPatterns = {"/AddressController"})
public class AddressController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        AddressDAO dao = new AddressDAO();
        HttpSession session = request.getSession();

        // 1. KIỂM TRA ĐĂNG NHẬP
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            if (action == null || action.equals("list")) {
                // 2. XEM DANH SÁCH ĐỊA CHỈ CỦA USER
                List<AddressDTO> list = dao.readAllByUserId(user.getUserId());
                request.setAttribute("ADDRESS_LIST", list);
                request.getRequestDispatcher("address-list.jsp").forward(request, response);

            } else if (action.equals("add")) {
                // 3. THÊM ĐỊA CHỈ MỚI
                String fullAddress = request.getParameter("full_address");
                String city = request.getParameter("city");
                String district = request.getParameter("district");
                boolean isDefault = request.getParameter("is_default") != null;

                AddressDTO dto = new AddressDTO();
                dto.setUser_id(user.getUserId());
                dto.setFull_address(fullAddress);
                dto.setCity(city);
                dto.setDistrict(district);
                dto.setIs_default(isDefault);

                if (dao.create(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Thêm địa chỉ mới thành công!");
                }
                response.sendRedirect("AddressController?action=list");

            } else if (action.equals("edit")) {
                // 4. LẤY DỮ LIỆU ĐỂ HIỂN THỊ FORM SỬA
                int id = Integer.parseInt(request.getParameter("id"));
                AddressDTO address = dao.readById(id);
                
                // Bảo mật: Chỉ cho phép sửa địa chỉ của chính mình
                if (address != null && address.getUser_id() == user.getUserId()) {
                    request.setAttribute("ADDRESS_DETAIL", address);
                    request.getRequestDispatcher("edit-address.jsp").forward(request, response);
                } else {
                    response.sendRedirect("AddressController");
                }

            } else if (action.equals("update")) {
                // 5. CẬP NHẬT ĐỊA CHỈ
                int id = Integer.parseInt(request.getParameter("id"));
                String fullAddress = request.getParameter("full_address");
                String city = request.getParameter("city");
                String district = request.getParameter("district");
                boolean isDefault = request.getParameter("is_default") != null;

                AddressDTO dto = new AddressDTO(id, user.getUserId(), fullAddress, city, district, isDefault);
                
                if (dao.update(dto)) {
                    session.setAttribute("MSG_SUCCESS", "Cập nhật địa chỉ thành công!");
                }
                response.sendRedirect("AddressController?action=list");

            } else if (action.equals("delete")) {
                // 6. XÓA ĐỊA CHỈ
                int id = Integer.parseInt(request.getParameter("id"));
                // Nên kiểm tra xem đây có phải địa chỉ mặc định không trước khi xóa
                if (dao.delete(id)) {
                    session.setAttribute("MSG_SUCCESS", "Đã xóa địa chỉ.");
                }
                response.sendRedirect("AddressController?action=list");

            } else if (action.equals("setDefault")) {
                // 7. THIẾT LẬP ĐỊA CHỈ MẶC ĐỊNH
                int id = Integer.parseInt(request.getParameter("id"));
                if (dao.setDefaultAddress(id, user.getUserId())) {
                    session.setAttribute("MSG_SUCCESS", "Đã thay đổi địa chỉ mặc định.");
                }
                response.sendRedirect("AddressController?action=list");
            }
            
        } catch (Exception e) {
            log("Error at AddressController: " + e.toString());
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