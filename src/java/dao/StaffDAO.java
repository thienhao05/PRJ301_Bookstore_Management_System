package dao;

import dto.StaffDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO implements ICRUD<StaffDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_STAFF = "INSERT INTO Staff (user_id, hire_date, salary, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_STAFF = "SELECT * FROM Staff";
    private static final String SELECT_STAFF_BY_ID = "SELECT * FROM Staff WHERE staff_id = ?";
    private static final String UPDATE_STAFF = "UPDATE Staff SET user_id = ?, hire_date = ?, salary = ?, status = ? WHERE staff_id = ?";
    private static final String DELETE_STAFF = "DELETE FROM Staff WHERE staff_id = ?";

    // Custom SQL
    private static final String SELECT_STAFF_BY_USER_ID = "SELECT * FROM Staff WHERE user_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(StaffDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_STAFF)) {
             
            pstm.setInt(1, obj.getUser_id());
            pstm.setDate(2, obj.getHire_date());
            pstm.setDouble(3, obj.getSalary());
            pstm.setString(4, obj.getStatus());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<StaffDTO> readAll() {
        List<StaffDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_STAFF);
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
    public StaffDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_STAFF_BY_ID)) {
             
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
    public boolean update(StaffDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_STAFF)) {
             
            pstm.setInt(1, obj.getUser_id());
            pstm.setDate(2, obj.getHire_date());
            pstm.setDouble(3, obj.getSalary());
            pstm.setString(4, obj.getStatus());
            pstm.setInt(5, obj.getStaff_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_STAFF)) {
             
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
     * Lấy thông tin Staff dựa vào user_id (Dùng khi Staff đăng nhập thành công)
     */
    public StaffDTO getStaffByUserId(int userId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_STAFF_BY_USER_ID)) {
             
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
    private StaffDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        StaffDTO dto = new StaffDTO();
        dto.setStaff_id(rs.getInt("staff_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setHire_date(rs.getDate("hire_date"));
        dto.setSalary(rs.getDouble("salary"));
        dto.setStatus(rs.getString("status"));
        return dto;
    }
}