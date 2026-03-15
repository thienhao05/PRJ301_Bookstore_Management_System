package dao;

import dto.DiscountDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO implements ICRUD<DiscountDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_DISCOUNT = "INSERT INTO Discounts (code, discount_percent, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_DISCOUNTS = "SELECT * FROM Discounts ORDER BY start_date DESC";
    private static final String SELECT_DISCOUNT_BY_ID = "SELECT * FROM Discounts WHERE discount_id = ?";
    private static final String UPDATE_DISCOUNT = "UPDATE Discounts SET code = ?, discount_percent = ?, start_date = ?, end_date = ?, status = ? WHERE discount_id = ?";
    private static final String DELETE_DISCOUNT = "DELETE FROM Discounts WHERE discount_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    // Kiểm tra mã: Trạng thái Active VÀ Ngày hiện tại (GETDATE) nằm giữa start_date và end_date
    private static final String SELECT_VALID_DISCOUNT_BY_CODE = "SELECT * FROM Discounts WHERE code = ? AND status = 'Active' AND CAST(GETDATE() AS DATE) BETWEEN start_date AND end_date";
    private static final String SELECT_DISCOUNT_BY_CODE = "SELECT * FROM Discounts WHERE code = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(DiscountDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_DISCOUNT)) {

            pstm.setString(1, obj.getCode());
            pstm.setInt(2, obj.getDiscount_percent());
            pstm.setDate(3, obj.getStart_date());
            pstm.setDate(4, obj.getEnd_date());
            pstm.setString(5, obj.getStatus() != null ? obj.getStatus() : "Active");

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DiscountDTO> readAll() {
        List<DiscountDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_DISCOUNTS);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public DiscountDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_DISCOUNT_BY_ID)) {

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
    public boolean update(DiscountDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_DISCOUNT)) {

            pstm.setString(1, obj.getCode());
            pstm.setInt(2, obj.getDiscount_percent());
            pstm.setDate(3, obj.getStart_date());
            pstm.setDate(4, obj.getEnd_date());
            pstm.setString(5, obj.getStatus());
            pstm.setInt(6, obj.getDiscount_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_DISCOUNT)) {

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
     * Dùng ở trang Checkout: Lấy mã giảm giá nếu mã đó ĐANG HỢP LỆ (còn hạn,
     * đang Active)
     */
    public DiscountDTO getValidDiscountByCode(String code) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_VALID_DISCOUNT_BY_CODE)) {

            pstm.setString(1, code);
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
     * Dùng cho Admin: Tiếm kiếm chi tiết mã giảm giá bằng mã (Bất kể hết hạn
     * hay chưa)
     */
    public DiscountDTO getDiscountByCode(String code) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_DISCOUNT_BY_CODE)) {

            pstm.setString(1, code);
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

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private DiscountDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        DiscountDTO dto = new DiscountDTO();
        dto.setDiscount_id(rs.getInt("discount_id"));
        dto.setCode(rs.getString("code"));
        dto.setDiscount_percent(rs.getInt("discount_percent"));
        dto.setStart_date(rs.getDate("start_date"));
        dto.setEnd_date(rs.getDate("end_date"));
        dto.setStatus(rs.getString("status"));
        return dto;
    }
}
