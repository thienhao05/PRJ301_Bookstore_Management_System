package controller;

import dao.BookDAO;
import dao.CartDAO;
import dao.CartItemDAO;
import dto.BookDTO;
import dto.CartDTO;
import dto.CartItemDTO;
import dto.UserDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    private static final String CART_PAGE = "/WEB-INF/views/web/cart.jsp";
    private static final String LOGIN_URL = "MainController?action=login";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            session.setAttribute("MSG_ERROR", "Vui lòng đăng nhập để sử dụng giỏ hàng!");
            response.sendRedirect(LOGIN_URL);
            return;
        }

        CartDAO cartDAO = new CartDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();
        BookDAO bookDAO = new BookDAO();

        try {
            // Lấy hoặc tạo giỏ hàng cho user
            CartDTO cart = cartDAO.getCartByUserId(loginUser.getUserId());
            if (cart == null) {
                cartDAO.create(new CartDTO(0, loginUser.getUserId(), null));
                cart = cartDAO.getCartByUserId(loginUser.getUserId());
            }
            int cartId = cart.getCart_id();

            switch (action != null ? action : "viewCart") {

                // ---- XEM GIỎ HÀNG ----
                case "viewCart": {
                    loadCartToSession(session, cartItemDAO, bookDAO, cartId);
                    request.getRequestDispatcher(CART_PAGE).forward(request, response);
                    break;
                }

                // ---- THÊM VÀO GIỎ HÀNG ----
                case "addToCart": {
                    String bookIdStr  = request.getParameter("bookId");
                    String quantityStr = request.getParameter("quantity");

                    if (bookIdStr == null) {
                        response.sendRedirect("MainController?action=view");
                        return;
                    }

                    int bookId   = Integer.parseInt(bookIdStr);
                    int quantity = (quantityStr != null) ? Integer.parseInt(quantityStr) : 1;

                    // Kiểm tra sách có tồn tại và còn hàng không
                    BookDTO book = bookDAO.readById(bookId);
                    if (book == null || book.getStock() <= 0) {
                        session.setAttribute("MSG_ERROR", "Sách này hiện đã hết hàng!");
                        response.sendRedirect("MainController?action=view");
                        return;
                    }

                    // Kiểm tra số lượng muốn thêm có vượt tồn kho không
                    if (quantity > book.getStock()) {
                        session.setAttribute("MSG_ERROR",
                            "Chỉ còn " + book.getStock() + " cuốn trong kho!");
                        response.sendRedirect("MainController?action=bookDetail&id=" + bookId);
                        return;
                    }

                    // Kiểm tra sách đã có trong giỏ chưa
                    CartItemDTO existingItem = cartItemDAO.getItemByCartAndBook(cartId, bookId);
                    if (existingItem != null) {
                        // Đã có -> cộng dồn số lượng
                        int newQuantity = existingItem.getQuantity() + quantity;
                        if (newQuantity > book.getStock()) {
                            newQuantity = book.getStock(); // giới hạn theo tồn kho
                        }
                        existingItem.setQuantity(newQuantity);
                        cartItemDAO.update(existingItem);
                        session.setAttribute("MSG_SUCCESS",
                            "Đã cập nhật số lượng \"" + book.getTitle() + "\" trong giỏ hàng!");
                    } else {
                        // Chưa có -> thêm mới
                        CartItemDTO newItem = new CartItemDTO();
                        newItem.setCart_id(cartId);
                        newItem.setBook_id(bookId);
                        newItem.setQuantity(quantity);
                        cartItemDAO.create(newItem);
                        session.setAttribute("MSG_SUCCESS",
                            "Đã thêm \"" + book.getTitle() + "\" vào giỏ hàng!");
                    }

                    // Cập nhật session giỏ hàng
                    loadCartToSession(session, cartItemDAO, bookDAO, cartId);

                    // Quay lại trang trước
                    String referer = request.getHeader("referer");
                    response.sendRedirect(referer != null ? referer : "MainController?action=view");
                    break;
                }

                // ---- XÓA KHỎI GIỎ HÀNG ----
                case "removeFromCart": {
                    String bookIdStr = request.getParameter("bookId");
                    if (bookIdStr != null) {
                        int bookId = Integer.parseInt(bookIdStr);
                        CartItemDTO item = cartItemDAO.getItemByCartAndBook(cartId, bookId);
                        if (item != null) {
                            cartItemDAO.delete(item.getCart_item_id());
                            session.setAttribute("MSG_SUCCESS", "Đã xóa sách khỏi giỏ hàng!");
                        }
                    }
                    loadCartToSession(session, cartItemDAO, bookDAO, cartId);
                    response.sendRedirect("MainController?action=viewCart");
                    break;
                }

                // ---- CẬP NHẬT SỐ LƯỢNG ----
                case "updateCartQuantity": {
                    int bookId   = Integer.parseInt(request.getParameter("bookId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    BookDTO book = bookDAO.readById(bookId);
                    CartItemDTO item = cartItemDAO.getItemByCartAndBook(cartId, bookId);

                    if (item != null && book != null) {
                        if (quantity <= 0) {
                            // Số lượng = 0 -> xóa khỏi giỏ
                            cartItemDAO.delete(item.getCart_item_id());
                        } else if (quantity > book.getStock()) {
                            // Vượt tồn kho -> giới hạn
                            item.setQuantity(book.getStock());
                            cartItemDAO.update(item);
                            session.setAttribute("MSG_ERROR",
                                "Chỉ còn " + book.getStock() + " cuốn trong kho!");
                        } else {
                            item.setQuantity(quantity);
                            cartItemDAO.update(item);
                        }
                    }
                    loadCartToSession(session, cartItemDAO, bookDAO, cartId);
                    response.sendRedirect("MainController?action=viewCart");
                    break;
                }

                // ---- XÓA TOÀN BỘ GIỎ HÀNG ----
                case "clearCart": {
                    cartItemDAO.deleteByCartId(cartId);
                    session.removeAttribute("CART");
                    session.removeAttribute("TOTAL_PRICE");
                    session.removeAttribute("TOTAL_ITEMS");
                    session.setAttribute("MSG_SUCCESS", "Đã xóa toàn bộ giỏ hàng!");
                    response.sendRedirect("MainController?action=viewCart");
                    break;
                }

                default:
                    response.sendRedirect("MainController?action=viewCart");
            }

        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
            request.getRequestDispatcher("/WEB-INF/views/web/error-500.jsp")
                   .forward(request, response);
        }
    }

    // ----------------------------------------------------------------
    // Helper: load danh sách cart items + tính tổng tiền vào session
    // ----------------------------------------------------------------
    private void loadCartToSession(HttpSession session, CartItemDAO cartItemDAO,
                                   BookDAO bookDAO, int cartId) {
        List<CartItemDTO> items = cartItemDAO.getItemsByCartId(cartId);

        // Tạo list enriched chứa cả thông tin sách
        List<BookDTO> cartBooks = new ArrayList<>();
        int totalPrice = 0;
        int totalItems = 0;

        for (CartItemDTO item : items) {
            BookDTO book = bookDAO.readById(item.getBook_id());
            if (book != null) {
                // Gắn quantity vào book để JSP dùng
                book.setStock(item.getQuantity()); // tạm dùng stock để truyền quantity
                cartBooks.add(book);
                totalPrice += (int)(book.getPrice() * item.getQuantity());
                totalItems += item.getQuantity();
            }
        }

        session.setAttribute("CART", items);
        session.setAttribute("CART_BOOKS", cartBooks);
        session.setAttribute("TOTAL_PRICE", totalPrice);
        session.setAttribute("TOTAL_ITEMS", totalItems);
        session.setAttribute("CART_ID", cartId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response); }
}