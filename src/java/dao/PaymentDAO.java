package dao;

import dto.PaymentDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements ICRUD<PaymentDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_PAYMENT = "INSERT INTO Payments (order_id, method, amount, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PAYMENTS = "SELECT * FROM Payments ORDER BY payment_date DESC";
    private static final String SELECT_PAYMENT_BY_ID = "SELECT * FROM Payments WHERE payment_id = ?";
    private static final String UPDATE_PAYMENT = "UPDATE Payments SET order_id = ?, method = ?, amount = ?, status = ? WHERE payment_id = ?";
    private static final String DELETE_PAYMENT = "DELETE FROM Payments WHERE payment_id = ?";

    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    private static final String SELECT_PAYMENT_BY_ORDER_ID = "SELECT * FROM Payments WHERE order_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(PaymentDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_PAYMENT)) {
             
            pstm.setInt(1, obj.getOrder_id());
            pstm.setString(2, obj.getMethod());
            pstm.setDouble(3, obj.getAmount());
            pstm.setString(4, obj.getStatus()); // Ví dụ: 'Pending', 'Completed', 'Refunded'
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<PaymentDTO> readAll() {
        List<PaymentDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_PAYMENTS);
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
    public PaymentDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_PAYMENT_BY_ID)) {
             
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
    public boolean update(PaymentDTO obj) {
        // Hàm này thường dùng khi admin cập nhật trạng thái từ 'Pending' sang 'Completed'
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_PAYMENT)) {
             
            pstm.setInt(1, obj.getOrder_id());
            pstm.setString(2, obj.getMethod());
            pstm.setDouble(3, obj.getAmount());
            pstm.setString(4, obj.getStatus());
            pstm.setInt(5, obj.getPayment_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_PAYMENT)) {
             
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
     * Lấy thông tin thanh toán dựa trên mã đơn hàng (order_id)
     */
    public PaymentDTO getPaymentByOrderId(int orderId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_PAYMENT_BY_ORDER_ID)) {
             
            pstm.setInt(1, orderId);
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
    private PaymentDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        PaymentDTO dto = new PaymentDTO();
        dto.setPayment_id(rs.getInt("payment_id"));
        dto.setOrder_id(rs.getInt("order_id"));
        dto.setMethod(rs.getString("method"));
        dto.setAmount(rs.getDouble("amount"));
        dto.setPayment_date(rs.getTimestamp("payment_date"));
        dto.setStatus(rs.getString("status"));
        return dto;
    }
}