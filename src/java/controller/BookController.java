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

    private static final String SHOP_PAGE        = "/WEB-INF/views/web/shop.jsp";
    private static final String BOOK_DETAIL_PAGE = "/WEB-INF/views/web/book-detail.jsp";
    private static final String HOME_PAGE        = "/WEB-INF/views/web/home.jsp";
    private static final String MANAGE_BOOKS     = "/WEB-INF/views/admin/manage-books.jsp";
    private static final String EDIT_BOOK        = "/WEB-INF/views/admin/edit-book.jsp";
    private static final String ERROR_404        = "/WEB-INF/views/web/error-404.jsp";
    private static final String ERROR_500        = "/WEB-INF/views/web/error-500.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "home";

        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        HttpSession session = request.getSession();

        try {
            switch (action) {

                // ================================================
                // DÀNH CHO TẤT CẢ (kể cả chưa đăng nhập)
                // ================================================

                case "home": {
                    List<BookDTO> list = bookDao.readAll();
                    request.setAttribute("BOOK_LIST", list);
                    request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                    request.getRequestDispatcher(HOME_PAGE).forward(request, response);
                    break;
                }

                case "view":
                case "viewBooks": {
                    String catIdStr = request.getParameter("categoryId");
                    List<BookDTO> list;
                    if (catIdStr != null && !catIdStr.isEmpty()) {
                        list = bookDao.getBooksByCategory(Integer.parseInt(catIdStr));
                    } else {
                        list = bookDao.readAll();
                    }
                    request.setAttribute("BOOK_LIST", list);
                    request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                    request.getRequestDispatcher(SHOP_PAGE).forward(request, response);
                    break;
                }

                case "search": {
                    String keyword = request.getParameter("query");
                    if (keyword == null || keyword.trim().isEmpty()) {
                        keyword = request.getParameter("keyword");
                    }
                    if (keyword == null) keyword = "";

                    List<BookDTO> list = keyword.trim().isEmpty()
                            ? bookDao.readAll()
                            : bookDao.searchBooks(keyword.trim());

                    request.setAttribute("BOOK_LIST", list);
                    request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                    request.setAttribute("KEYWORD", keyword);
                    request.getRequestDispatcher(SHOP_PAGE).forward(request, response);
                    break;
                }

                case "detail":
                case "bookDetail": {
                    String idStr = request.getParameter("id");
                    if (idStr == null) idStr = request.getParameter("bookId");
                    int id = Integer.parseInt(idStr);
                    BookDTO book = bookDao.readById(id);
                    if (book != null) {
                        request.setAttribute("BOOK", book);
                        request.getRequestDispatcher(BOOK_DETAIL_PAGE)
                               .forward(request, response);
                    } else {
                        request.getRequestDispatcher(ERROR_404).forward(request, response);
                    }
                    break;
                }

                // ================================================
                // DÀNH CHO ADMIN / MANAGER / STAFF (roleId 1,2,3)
                // ================================================

                case "manageBooks": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    List<BookDTO> list = bookDao.readAll();
                    request.setAttribute("BOOK_LIST", list);
                    request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                    request.getRequestDispatcher(MANAGE_BOOKS).forward(request, response);
                    break;
                }

                case "addBook": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    BookDTO dto = new BookDTO();
                    dto.setTitle(request.getParameter("title"));
                    dto.setAuthor(request.getParameter("author"));
                    dto.setPrice(Double.parseDouble(request.getParameter("price")));
                    dto.setStock(Integer.parseInt(request.getParameter("stock")));
                    dto.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
                    dto.setPublisherId(Integer.parseInt(request.getParameter("publisherId")));

                    if (bookDao.create(dto)) {
                        session.setAttribute("MSG_SUCCESS", "Thêm sách thành công!");
                    } else {
                        session.setAttribute("MSG_ERROR", "Thêm sách thất bại!");
                    }
                    response.sendRedirect("MainController?action=manageBooks");
                    break;
                }

                case "editBook": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    BookDTO book = bookDao.readById(id);
                    if (book != null) {
                        request.setAttribute("BOOK", book);
                        request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                        request.getRequestDispatcher(EDIT_BOOK).forward(request, response);
                    } else {
                        response.sendRedirect("MainController?action=manageBooks");
                    }
                    break;
                }

                case "updateBook": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
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
                            session.setAttribute("MSG_SUCCESS", "Cập nhật sách thành công!");
                        } else {
                            session.setAttribute("MSG_ERROR", "Cập nhật thất bại!");
                        }
                    }
                    response.sendRedirect("MainController?action=manageBooks");
                    break;
                }

                case "deleteBook": {
                    if (!hasAdminAccess(session)) {
                        response.sendRedirect("MainController?action=login");
                        return;
                    }
                    int id = Integer.parseInt(request.getParameter("id"));
                    if (bookDao.delete(id)) {
                        session.setAttribute("MSG_SUCCESS", "Đã xóa sách khỏi kho.");
                    } else {
                        session.setAttribute("MSG_ERROR", "Không thể xóa sách này.");
                    }
                    response.sendRedirect("MainController?action=manageBooks");
                    break;
                }

                default:
                    request.getRequestDispatcher(ERROR_404).forward(request, response);
            }

        } catch (Exception e) {
            log("Error at BookController: " + e.toString());
            request.getRequestDispatcher(ERROR_500).forward(request, response);
        }
    }

    // ---- Helper: kiểm tra quyền admin/manager/staff ----
    private boolean hasAdminAccess(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user == null) return false;
        int role = user.getRoleId();
        return role == 1 || role == 2 || role == 3;
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