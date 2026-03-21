package dao;

import dto.WishlistDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO implements ICRUD<WishlistDTO> {

    private static final String INSERT_WISHLIST =
        "INSERT INTO Wishlist (user_id, book_id) VALUES (?, ?)";
    private static final String SELECT_ALL_WISHLIST =
        "SELECT * FROM Wishlist";
    private static final String SELECT_WISHLIST_BY_ID =
        "SELECT * FROM Wishlist WHERE wishlist_id = ?";
    private static final String UPDATE_WISHLIST =
        "UPDATE Wishlist SET user_id = ?, book_id = ? WHERE wishlist_id = ?";
    private static final String DELETE_WISHLIST_BY_ID =
        "DELETE FROM Wishlist WHERE wishlist_id = ?";

    // ✅ SỬA: JOIN với bảng Books để lấy title, author, price, stock
    private static final String SELECT_BY_USER_ID =
        "SELECT w.wishlist_id, w.user_id, w.book_id, w.created_at, " +
        "b.title, b.author, b.price, b.stock " +
        "FROM Wishlist w " +
        "JOIN Books b ON w.book_id = b.book_id " +
        "WHERE w.user_id = ? " +
        "ORDER BY w.created_at DESC";

    private static final String DELETE_BY_USER_AND_BOOK =
        "DELETE FROM Wishlist WHERE user_id = ? AND book_id = ?";
    private static final String CHECK_EXIST =
        "SELECT 1 FROM Wishlist WHERE user_id = ? AND book_id = ?";

    // ==============================================================
    // ICRUD
    // ==============================================================
    @Override
    public boolean create(WishlistDTO obj) {
        if (isExistInWishlist(obj.getUser_id(), obj.getBook_id())) {
            return false; // Tránh trùng lặp
        }
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_WISHLIST)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getBook_id());
            return pstm.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<WishlistDTO> readAll() {
        List<WishlistDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_WISHLIST);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) list.add(mapBasic(rs));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public WishlistDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_WISHLIST_BY_ID)) {

            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) return mapBasic(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(WishlistDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_WISHLIST)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getWishlist_id());
            return pstm.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_WISHLIST_BY_ID)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // CUSTOM METHODS
    // ==============================================================

    // ✅ JOIN Books → trả về đủ title, author, price, stock
    public List<WishlistDTO> getWishlistByUserId(int userId) {
        List<WishlistDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_BY_USER_ID)) {

            pstm.setInt(1, userId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapWithBook(rs)); // ✅ Dùng mapper đầy đủ
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Hàm mà WishlistController gọi khi xóa bằng bookId
    public boolean deleteByUserAndBook(int userId, int bookId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_BY_USER_AND_BOOK)) {

            pstm.setInt(1, userId);
            pstm.setInt(2, bookId);
            return pstm.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Alias để tương thích với code cũ
    public boolean removeBookFromWishlist(int userId, int bookId) {
        return deleteByUserAndBook(userId, bookId);
    }

    public boolean isExistInWishlist(int userId, int bookId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(CHECK_EXIST)) {

            pstm.setInt(1, userId);
            pstm.setInt(2, bookId);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // MAPPERS
    // ==============================================================

    // Mapper cơ bản (chỉ dùng cho readAll, readById)
    private WishlistDTO mapBasic(ResultSet rs) throws SQLException {
        WishlistDTO dto = new WishlistDTO();
        dto.setWishlist_id(rs.getInt("wishlist_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setBook_id(rs.getInt("book_id"));
        dto.setCreated_at(rs.getTimestamp("created_at"));
        return dto;
    }

    // ✅ Mapper đầy đủ JOIN Books (dùng cho getWishlistByUserId)
    private WishlistDTO mapWithBook(ResultSet rs) throws SQLException {
        WishlistDTO dto = new WishlistDTO();
        dto.setWishlist_id(rs.getInt("wishlist_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setBook_id(rs.getInt("book_id"));
        dto.setBookId(rs.getInt("book_id"));   // ✅ Cho JSP dùng ${item.bookId}
        dto.setCreated_at(rs.getTimestamp("created_at"));
        dto.setTitle(rs.getString("title"));
        dto.setAuthor(rs.getString("author"));
        dto.setPrice(rs.getDouble("price"));
        dto.setStock(rs.getInt("stock"));
        return dto;
    }
}