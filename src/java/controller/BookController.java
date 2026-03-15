package controller;

import dao.BookDAO;
import dao.CategoryDAO;
import dto.BookDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BookController", urlPatterns = {"/BookController"})
public class BookController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        HttpSession session = request.getSession();

        try {
            // 1. DÀNH CHO KHÁCH HÀNG (VIEW/SEARCH/DETAIL)
            if (action == null || action.equals("view")) {
                String catIdRaw = request.getParameter("categoryId");
                List<BookDTO> list = (catIdRaw != null && !catIdRaw.isEmpty()) 
                    ? bookDao.getBooksByCategory(Integer.parseInt(catIdRaw)) 
                    : bookDao.readAll();
                
                request.setAttribute("BOOK_LIST", list);
                request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                request.getRequestDispatcher("shop.jsp").forward(request, response);

            } else if (action.equals("search")) {
                String keyword = request.getParameter("keyword");
                List<BookDTO> list = bookDao.searchBooks(keyword);
                request.setAttribute("BOOK_LIST", list);
                request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                request.getRequestDispatcher("shop.jsp").forward(request, response);

            } else if (action.equals("detail")) {
                int id = Integer.parseInt(request.getParameter("id"));
                BookDTO book = bookDao.readById(id);
                if (book != null) {
                    request.setAttribute("BOOK_DETAIL", book);
                    request.getRequestDispatcher("book-detail.jsp").forward(request, response);
                }

            // 2. DÀNH CHO QUẢN TRỊ (ADMIN/MANAGER)
            } else {
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                if (loginUser == null || (loginUser.getRoleId() != 1 && loginUser.getRoleId() != 2)) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (action.equals("manage")) {
                    List<BookDTO> list = bookDao.readAll();
                    request.setAttribute("BOOK_LIST", list);
                    request.getRequestDispatcher("admin/manage-books.jsp").forward(request, response);

                } else if (action.equals("add")) {
                    // FIX: Khớp với các field trong BookDTO
                    BookDTO dto = new BookDTO();
                    dto.setTitle(request.getParameter("title"));
                    dto.setAuthor(request.getParameter("author"));
                    // Chuyển sang Double.parseDouble vì DTO price là double
                    dto.setPrice(Double.parseDouble(request.getParameter("price")));
                    dto.setStock(Integer.parseInt(request.getParameter("stock")));
                    dto.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                    dto.setPublisherId(Integer.parseInt(request.getParameter("publisherId")));

                    if (bookDao.create(dto)) {
                        session.setAttribute("MSG_SUCCESS", "Thêm sách thành công!");
                    }
                    response.sendRedirect("BookController?action=manage");

                } else if (action.equals("update")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    BookDTO dto = bookDao.readById(id);
                    if (dto != null) {
                        dto.setTitle(request.getParameter("title"));
                        dto.setAuthor(request.getParameter("author"));
                        dto.setPrice(Double.parseDouble(request.getParameter("price")));
                        dto.setStock(Integer.parseInt(request.getParameter("stock")));
                        dto.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                        dto.setPublisherId(Integer.parseInt(request.getParameter("publisherId")));
                        
                        if (bookDao.update(dto)) {
                            session.setAttribute("MSG_SUCCESS", "Cập nhật thành công!");
                        }
                    }
                    response.sendRedirect("BookController?action=manage");

                } else if (action.equals("delete")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    if (bookDao.delete(id)) {
                        session.setAttribute("MSG_SUCCESS", "Đã xóa sách khỏi kho.");
                    } else {
                        session.setAttribute("MSG_ERROR", "Không thể xóa (dữ liệu đang được liên kết).");
                    }
                    response.sendRedirect("BookController?action=manage");
                }
            }
        } catch (Exception e) {
            log("Error at BookController: " + e.toString());
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