package dao;

import dto.OrderDetailDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO implements ICRUD<OrderDetailDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO OrderDetails (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDER_DETAILS = "SELECT * FROM OrderDetails";
    private static final String SELECT_ORDER_DETAIL_BY_ID = "SELECT * FROM OrderDetails WHERE order_detail_id = ?";
    private static final String UPDATE_ORDER_DETAIL = "UPDATE OrderDetails SET order_id = ?, book_id = ?, quantity = ?, price = ? WHERE order_detail_id = ?";
    private static final String DELETE_ORDER_DETAIL = "DELETE FROM OrderDetails WHERE order_detail_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_ORDER_ID = "SELECT * FROM OrderDetails WHERE order_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(OrderDetailDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_ORDER_DETAIL)) {
             
            pstm.setInt(1, obj.getOrder_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getQuantity());
            pstm.setInt(4, obj.getPrice());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderDetailDTO> readAll() {
        List<OrderDetailDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_ORDER_DETAILS);
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
    public OrderDetailDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ORDER_DETAIL_BY_ID)) {
             
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
    public boolean update(OrderDetailDTO obj) {
        // Lưu ý: Thông thường chi tiết đơn hàng ít khi được update sau khi đã thanh toán/chốt đơn.
        // Tuy nhiên, vẫn hỗ trợ hàm này cho Admin nếu cần điều chỉnh thủ công.
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_ORDER_DETAIL)) {
             
            pstm.setInt(1, obj.getOrder_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getQuantity());
            pstm.setInt(4, obj.getPrice());
            pstm.setInt(5, obj.getOrder_detail_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_ORDER_DETAIL)) {
             
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
     * Lấy toàn bộ chi tiết sách của một Đơn hàng cụ thể
     */
    public List<OrderDetailDTO> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetailDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_BY_ORDER_ID)) {
             
            pstm.setInt(1, orderId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private OrderDetailDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrder_detail_id(rs.getInt("order_detail_id"));
        dto.setOrder_id(rs.getInt("order_id"));
        dto.setBook_id(rs.getInt("book_id"));
        dto.setQuantity(rs.getInt("quantity"));
        
        // Mặc dù trong SQL price là DECIMAL, nhưng DTO bạn đang dùng int
        // Hàm getInt() của ResultSet sẽ tự động ép kiểu phần nguyên cho bạn
        dto.setPrice(rs.getInt("price"));
        
        return dto;
    }
}