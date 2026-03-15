package dao;

import dto.CartDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDAO implements ICRUD<CartDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    // Chỉ cần insert user_id, created_at sẽ tự động lấy GETDATE() dưới SQL
    private static final String INSERT_CART = "INSERT INTO Cart (user_id) VALUES (?)";
    private static final String SELECT_ALL_CARTS = "SELECT * FROM Cart";
    private static final String SELECT_CART_BY_ID = "SELECT * FROM Cart WHERE cart_id = ?";
    private static final String UPDATE_CART = "UPDATE Cart SET user_id = ? WHERE cart_id = ?";
    private static final String DELETE_CART = "DELETE FROM Cart WHERE cart_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_USER_ID = "SELECT * FROM Cart WHERE user_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(CartDTO obj) {
        // Hàm này nên được gọi tự động ngay sau khi User đăng ký tài khoản thành công
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_CART)) {
             
            pstm.setInt(1, obj.getUser_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CartDTO> readAll() {
        List<CartDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_CARTS);
             ResultSet rs = pstm.executeQuery()) {
             
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public CartDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_CART_BY_ID)) {
             
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
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
    public boolean update(CartDTO obj) {
        // Trong thực tế, giỏ hàng hiếm khi bị update (vì nó gắn chết với 1 user_id)
        // Nhưng mình vẫn viết để tuân thủ interface ICRUD
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_CART)) {
             
            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getCart_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Khi xóa Cart, cần đảm bảo bảng CartItems đã được xóa sạch trước (để tránh lỗi khóa ngoại)
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_CART)) {
             
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
     * Lấy ID Giỏ hàng của một User (Dùng để hiển thị giỏ hàng hoặc chuẩn bị thêm sách vào giỏ)
     */
    public CartDTO getCartByUserId(int userId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_BY_USER_ID)) {
             
            pstm.setInt(1, userId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private CartDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        CartDTO dto = new CartDTO();
        dto.setCart_id(rs.getInt("cart_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setCreated_at(rs.getTimestamp("created_at"));
        return dto;
    }
}