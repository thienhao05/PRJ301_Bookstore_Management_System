package dao;

import dto.OrderDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements ICRUD<OrderDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_ORDER = "INSERT INTO Orders (user_id, address_id, discount_id, shipping_provider_id, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDERS = "SELECT * FROM Orders ORDER BY order_date DESC";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM Orders WHERE order_id = ?";
    private static final String UPDATE_ORDER = "UPDATE Orders SET user_id = ?, address_id = ?, discount_id = ?, shipping_provider_id = ?, total_amount = ?, status = ? WHERE order_id = ?";
    private static final String DELETE_ORDER = "DELETE FROM Orders WHERE order_id = ?";

    // ==============================================================
    // CÁC CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_USER_ID = "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC";
    private static final String UPDATE_STATUS = "UPDATE Orders SET status = ? WHERE order_id = ?";
    private static final String SELECT_USER_ID_BY_ORDER_ID = "SELECT user_id FROM Orders WHERE order_id = ?";
    private static final String UPDATE_PAYMENT_INFO = "UPDATE Orders SET payment_method = ?, status = ? WHERE order_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(OrderDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_ORDER)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getAddress_id());

            // Xử lý NULL cho discount_id
            if (obj.getDiscount_id() > 0) {
                pstm.setInt(3, obj.getDiscount_id());
            } else {
                pstm.setNull(3, java.sql.Types.INTEGER);
            }

            // Xử lý NULL cho shipping_provider_id
            if (obj.getShipping_provider_id() > 0) {
                pstm.setInt(4, obj.getShipping_provider_id());
            } else {
                pstm.setNull(4, java.sql.Types.INTEGER);
            }

            pstm.setInt(5, obj.getTotal_amount());
            pstm.setString(6, obj.getStatus() != null ? obj.getStatus() : "Pending");

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OrderDTO> readAll() {
        List<OrderDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_ORDERS);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public OrderDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ORDER_BY_ID)) {

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
    public boolean update(OrderDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_ORDER)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getAddress_id());

            if (obj.getDiscount_id() > 0) {
                pstm.setInt(3, obj.getDiscount_id());
            } else {
                pstm.setNull(3, java.sql.Types.INTEGER);
            }

            if (obj.getShipping_provider_id() > 0) {
                pstm.setInt(4, obj.getShipping_provider_id());
            } else {
                pstm.setNull(4, java.sql.Types.INTEGER);
            }

            pstm.setInt(5, obj.getTotal_amount());
            pstm.setString(6, obj.getStatus());
            pstm.setInt(7, obj.getOrder_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Cần lưu ý: Nếu xóa Order, phải xóa OrderDetails và Payments trước (khóa ngoại)
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_ORDER)) {

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
     * Lấy danh sách lịch sử mua hàng của một khách hàng
     */
    public List<OrderDTO> getOrdersByUserId(int userId) {
        List<OrderDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_USER_ID)) {

            pstm.setInt(1, userId);
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
     * Admin/Staff cập nhật trạng thái đơn hàng nhanh chóng
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_STATUS)) {

            pstm.setString(1, newStatus);
            pstm.setInt(2, orderId);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private OrderDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        OrderDTO dto = new OrderDTO();
        dto.setOrder_id(rs.getInt("order_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setAddress_id(rs.getInt("address_id"));

        // rs.getInt() trả về 0 nếu cột đó mang giá trị NULL
        dto.setDiscount_id(rs.getInt("discount_id"));
        dto.setShipping_provider_id(rs.getInt("shipping_provider_id"));

        dto.setTotal_amount(rs.getInt("total_amount"));
        dto.setOrder_date(rs.getTimestamp("order_date"));
        dto.setStatus(rs.getString("status"));

        return dto;
    }

    /**
     * Lấy UserID của một đơn hàng để kiểm tra tính chính chủ (Security Check)
     */
    public int getUserId(int orderId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_USER_ID_BY_ORDER_ID)) {

            pstm.setInt(1, orderId);
            try ( ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    /**
     * Cập nhật phương thức thanh toán và trạng thái đơn hàng sau khi thanh toán
     * thành công
     */
    public boolean updatePaymentStatus(int orderId, String paymentMethod, String paymentStatus) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_PAYMENT_INFO)) {

            pstm.setString(1, paymentMethod);
            pstm.setString(2, paymentStatus); // Ví dụ: "Paid" hoặc "Pending"
            pstm.setInt(3, orderId);

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
