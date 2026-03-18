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

    // ---- Khai báo đường dẫn JSP đúng ----
    private static final String SHOP_PAGE = "/WEB-INF/views/web/shop.jsp";
    private static final String BOOK_DETAIL_PAGE = "/WEB-INF/views/web/book-detail.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/web/home.jsp";
    private static final String MANAGE_BOOKS = "/WEB-INF/views/admin/manage-books.jsp";
    private static final String ERROR_404 = "/WEB-INF/views/web/error-404.jsp";
    private static final String ERROR_500 = "/WEB-INF/views/web/error-500.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        BookDAO bookDao = new BookDAO();
        CategoryDAO categoryDao = new CategoryDAO();
        HttpSession session = request.getSession();

        try {
            // ---- DÀNH CHO KHÁCH HÀNG ----
            if (action == null || "view".equals(action) || "home".equals(action)) {
                List<BookDTO> list = bookDao.readAll();
                request.setAttribute("BOOK_LIST", list);
                request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                // action=home -> home.jsp, action=view/null -> shop.jsp
                if ("home".equals(action)) {
                    request.getRequestDispatcher(HOME_PAGE).forward(request, response);
                } else {
                    request.getRequestDispatcher(SHOP_PAGE).forward(request, response);
                }

            } else if ("search".equals(action)) {
                String keyword = request.getParameter("keyword");
                if (keyword == null) {
                    keyword = request.getParameter("query"); // fallback
                }
                List<BookDTO> list = bookDao.searchBooks(keyword != null ? keyword : "");
                request.setAttribute("BOOK_LIST", list);
                request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                request.setAttribute("KEYWORD", keyword);
                request.getRequestDispatcher(SHOP_PAGE).forward(request, response);

            } else if ("detail".equals(action) || "bookDetail".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr == null) {
                    idStr = request.getParameter("bookId");
                }
                int id = Integer.parseInt(idStr);
                BookDTO book = bookDao.readById(id);
                if (book != null) {
                    request.setAttribute("BOOK", book);
                    request.getRequestDispatcher(BOOK_DETAIL_PAGE).forward(request, response);
                } else {
                    request.getRequestDispatcher(ERROR_404).forward(request, response);
                }

                // ---- DÀNH CHO ADMIN ----
            } else {
                UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
                // Admin(1), Manager(2), Staff(3) mới được quản lý sách
                if (loginUser == null || (loginUser.getRoleId() != 1
                        && loginUser.getRoleId() != 2
                        && loginUser.getRoleId() != 3)) {
                    response.sendRedirect("MainController?action=login");
                    return;
                }

                if ("manageBooks".equals(action)) {
                    List<BookDTO> list = bookDao.readAll();
                    List categoryList = categoryDao.readAll();
                    request.setAttribute("BOOK_LIST", list);
                    request.setAttribute("CATEGORY_LIST", categoryList);
                    request.getRequestDispatcher(MANAGE_BOOKS).forward(request, response);

                } else if ("addBook".equals(action)) {
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

                } else if ("editBook".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    BookDTO book = bookDao.readById(id);
                    request.setAttribute("BOOK", book);
                    request.setAttribute("CATEGORY_LIST", categoryDao.readAll());
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-book.jsp")
                            .forward(request, response);

                } else if ("deleteBook".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    if (bookDao.delete(id)) {
                        session.setAttribute("MSG_SUCCESS", "Đã xóa sách khỏi kho.");
                    } else {
                        session.setAttribute("MSG_ERROR", "Không thể xóa sách này.");
                    }
                    response.sendRedirect("MainController?action=manageBooks");
                }
            }

        } catch (Exception e) {
            log("Error at BookController: " + e.toString());
            request.getRequestDispatcher(ERROR_500).forward(request, response);
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
