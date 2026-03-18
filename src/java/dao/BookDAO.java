package dao;

import dto.BookDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements ICRUD<BookDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    // Bỏ qua created_at vì SQL tự động lấy GETDATE()
    private static final String INSERT_BOOK = "INSERT INTO Books (title, author, price, stock, category_id, publisher_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_BOOKS = "SELECT * FROM Books ORDER BY created_at DESC";
    private static final String SELECT_BOOK_BY_ID = "SELECT * FROM Books WHERE book_id = ?";
    private static final String UPDATE_BOOK = "UPDATE Books SET title = ?, author = ?, price = ?, stock = ?, category_id = ?, publisher_id = ? WHERE book_id = ?";
    private static final String DELETE_BOOK = "DELETE FROM Books WHERE book_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_CATEGORY = "SELECT * FROM Books WHERE category_id = ? ORDER BY created_at DESC";
    private static final String SEARCH_BOOKS = "SELECT * FROM Books WHERE title LIKE ? OR author LIKE ? ORDER BY created_at DESC";
    private static final String SELECT_TOP_NEW = "SELECT TOP (?) * FROM Books ORDER BY created_at DESC";
    private static final String UPDATE_STOCK = "UPDATE Books SET stock = stock - ? WHERE book_id = ? AND stock >= ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(BookDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_BOOK)) {

            pstm.setString(1, obj.getTitle());
            pstm.setString(2, obj.getAuthor());
            pstm.setDouble(3, obj.getPrice());
            pstm.setInt(4, obj.getStock());

            // Xử lý khóa ngoại có thể NULL (Nếu không chọn category hoặc publisher)
            if (obj.getCategoryId() > 0) {
                pstm.setInt(5, obj.getCategoryId());
            } else {
                pstm.setNull(5, java.sql.Types.INTEGER);
            }

            if (obj.getPublisherId() > 0) {
                pstm.setInt(6, obj.getPublisherId());
            } else {
                pstm.setNull(6, java.sql.Types.INTEGER);
            }

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BookDTO> readAll() {
        List<BookDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_BOOKS);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public BookDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BOOK_BY_ID)) {

            pstm.setInt(1, id);
            try ( ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(BookDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_BOOK)) {

            pstm.setString(1, obj.getTitle());
            pstm.setString(2, obj.getAuthor());
            pstm.setDouble(3, obj.getPrice());
            pstm.setInt(4, obj.getStock());

            if (obj.getCategoryId() > 0) {
                pstm.setInt(5, obj.getCategoryId());
            } else {
                pstm.setNull(5, java.sql.Types.INTEGER);
            }

            if (obj.getPublisherId() > 0) {
                pstm.setInt(6, obj.getPublisherId());
            } else {
                pstm.setNull(6, java.sql.Types.INTEGER);
            }

            pstm.setInt(7, obj.getBookId());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Lưu ý: Nếu sách đã có trong OrderDetail hoặc CartItem, không thể xóa trực tiếp.
        // Thực tế người ta thường thêm cột 'status' = 0 (Ngừng bán) thay vì DELETE.
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_BOOK)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // CÁC HÀM NGHIỆP VỤ (CUSTOM METHODS)
    // ==============================================================
    /**
     * Lọc danh sách sách theo ID Danh mục
     */
    public List<BookDTO> getBooksByCategory(int categoryId) {
        List<BookDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_CATEGORY)) {

            pstm.setInt(1, categoryId);
            try ( ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm sách theo từ khóa (khớp với tên sách hoặc tác giả)
     */
    public List<BookDTO> searchBooks(String keyword) {
        List<BookDTO> list = new ArrayList<>();

        // Nếu keyword rỗng thì trả về toàn bộ sách
        if (keyword == null || keyword.trim().isEmpty()) {
            return readAll();
        }

        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SEARCH_BOOKS)) {

            String searchPattern = "%" + keyword.trim() + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);

            try ( ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy N cuốn sách mới nhất (Dành cho trang chủ)
     */
    public List<BookDTO> getTopNewBooks(int limit) {
        List<BookDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_TOP_NEW)) {

            pstm.setInt(1, limit);
            try ( ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Trừ số lượng tồn kho sau khi khách đặt hàng thành công. Đảm bảo stock
     * không bị âm bằng điều kiện AND stock >= quantity
     */
    public boolean updateStock(int bookId, int quantityToReduce) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_STOCK)) {

            pstm.setInt(1, quantityToReduce);
            pstm.setInt(2, bookId);
            pstm.setInt(3, quantityToReduce);

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private BookDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        BookDTO dto = new BookDTO();

        dto.setBookId(rs.getInt("book_id"));
        dto.setTitle(rs.getString("title"));
        dto.setAuthor(rs.getString("author"));
        dto.setPrice(rs.getDouble("price"));
        dto.setStock(rs.getInt("stock"));

        // Sử dụng getInt() sẽ trả về 0 nếu cột trong DB là NULL
        dto.setCategoryId(rs.getInt("category_id"));
        dto.setPublisherId(rs.getInt("publisher_id"));
        dto.setCreatedAt(rs.getTimestamp("created_at"));

        return dto;
    }
}
