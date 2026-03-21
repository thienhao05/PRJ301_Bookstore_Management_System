package dao;

import dto.CartItemDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO implements ICRUD<CartItemDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_CART_ITEM = "INSERT INTO CartItems (cart_id, book_id, quantity) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CART_ITEMS = "SELECT * FROM CartItems";
    private static final String SELECT_CART_ITEM_BY_ID = "SELECT * FROM CartItems WHERE cart_item_id = ?";
    private static final String UPDATE_CART_ITEM = "UPDATE CartItems SET cart_id = ?, book_id = ?, quantity = ? WHERE cart_item_id = ?";
    private static final String DELETE_CART_ITEM = "DELETE FROM CartItems WHERE cart_item_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_CART_ID = "SELECT * FROM CartItems WHERE cart_id = ?";
    private static final String SELECT_BY_CART_AND_BOOK = "SELECT * FROM CartItems WHERE cart_id = ? AND book_id = ?";
    private static final String DELETE_BY_CART_ID = "DELETE FROM CartItems WHERE cart_id = ?";
    // Thêm 2 dòng này vào danh sách hằng số SQL ở đầu class
    private static final String REMOVE_ALL_DEFAULTS = "UPDATE Addresses SET is_default = 0 WHERE user_id = ?";
    private static final String SET_DEFAULT_ADDRESS = "UPDATE Addresses SET is_default = 1 WHERE address_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(CartItemDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_CART_ITEM)) {

            pstm.setInt(1, obj.getCart_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getQuantity());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CartItemDTO> readAll() {
        List<CartItemDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_CART_ITEMS);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public CartItemDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_CART_ITEM_BY_ID)) {

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
    public boolean update(CartItemDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_CART_ITEM)) {

            pstm.setInt(1, obj.getCart_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getQuantity());
            pstm.setInt(4, obj.getCart_item_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_CART_ITEM)) {

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
     * Lấy toàn bộ danh sách sản phẩm đang có trong giỏ hàng của 1 User (thông
     * qua cart_id)
     */
    public List<CartItemDTO> getItemsByCartId(int cartId) {
        List<CartItemDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_CART_ID)) {

            pstm.setInt(1, cartId);
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
     * Kiểm tra xem 1 cuốn sách đã có trong giỏ hàng cụ thể này chưa. Trả về
     * CartItemDTO nếu có, để bạn lấy ID ra và chạy lệnh Update cộng dồn số
     * lượng.
     */
    public CartItemDTO getItemByCartAndBook(int cartId, int bookId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_CART_AND_BOOK)) {

            pstm.setInt(1, cartId);
            pstm.setInt(2, bookId);
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

    /**
     * Làm sạch giỏ hàng (Xóa toàn bộ item sau khi đã thanh toán thành công)
     */
    public boolean deleteByCartId(int cartId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_BY_CART_ID)) {

            pstm.setInt(1, cartId);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private CartItemDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        CartItemDTO dto = new CartItemDTO();
        dto.setCart_item_id(rs.getInt("cart_item_id"));
        dto.setCart_id(rs.getInt("cart_id"));
        dto.setBook_id(rs.getInt("book_id"));
        dto.setQuantity(rs.getInt("quantity"));
        return dto;
    }

    /**
     * Đổi tên hàm này trong CartItemDAO của bạn để khớp với Controller
     */
    public CartItemDTO getItem(int cartId, int bookId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_CART_AND_BOOK)) {

            pstm.setInt(1, cartId);
            pstm.setInt(2, bookId);
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

    /**
     * Cập nhật địa chỉ mặc định (Đã đổi tên thành setDefault để khớp với
     * Controller) Sử dụng Transaction để đảm bảo tính toàn vẹn dữ liệu.
     */
    public boolean setDefault(int addressId, int userId) {
        Connection conn = null;
        try {
            conn = DbUtils.getConnection();
            // Tắt auto commit để thực hiện giao dịch (Transaction)
            conn.setAutoCommit(false);

            // Bước 1: Gỡ bỏ trạng thái mặc định của TẤT CẢ địa chỉ thuộc về User này
            try ( PreparedStatement pstm1 = conn.prepareStatement(REMOVE_ALL_DEFAULTS)) {
                pstm1.setInt(1, userId);
                pstm1.executeUpdate();
            }

            // Bước 2: Thiết lập địa chỉ được chọn thành mặc định
            try ( PreparedStatement pstm2 = conn.prepareStatement(SET_DEFAULT_ADDRESS)) {
                pstm2.setInt(1, addressId);
                int affectedRows = pstm2.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit(); // Thành công cả 2 bước thì mới lưu vào DB
                    return true;
                }
            }

            conn.rollback(); // Nếu không có hàng nào bị ảnh hưởng ở bước 2, rollback cho chắc

        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Gặp lỗi là "quay xe" ngay lập tức
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
