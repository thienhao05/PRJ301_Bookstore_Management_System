package dao;

import dto.ShiftDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO implements ICRUD<ShiftDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_SHIFT = "INSERT INTO Shifts (staff_id, shift_date, start_time, end_time) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SHIFTS = "SELECT * FROM Shifts ORDER BY shift_date DESC, start_time ASC";
    private static final String SELECT_SHIFT_BY_ID = "SELECT * FROM Shifts WHERE shift_id = ?";
    private static final String UPDATE_SHIFT = "UPDATE Shifts SET staff_id = ?, shift_date = ?, start_time = ?, end_time = ? WHERE shift_id = ?";
    private static final String DELETE_SHIFT = "DELETE FROM Shifts WHERE shift_id = ?";

    // ==============================================================
    // CÁC CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_STAFF_ID = "SELECT * FROM Shifts WHERE staff_id = ? ORDER BY shift_date DESC";
    private static final String SELECT_BY_DATE = "SELECT * FROM Shifts WHERE shift_date = ? ORDER BY start_time ASC";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(ShiftDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_SHIFT)) {
             
            pstm.setInt(1, obj.getStaffId());      // Đổi thành getStaffId()
        pstm.setDate(2, obj.getShiftDate());   // Đổi thành getShiftDate()
        pstm.setTime(3, obj.getStartTime());   // Đổi thành getStartTime()
        pstm.setTime(4, obj.getEndTime());     // Đổi thành getEndTime()
        pstm.setInt(5, obj.getId());           // Đổi thành getId()
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ShiftDTO> readAll() {
        List<ShiftDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_SHIFTS);
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
    public ShiftDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_SHIFT_BY_ID)) {
             
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
    public boolean update(ShiftDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_SHIFT)) {
             
            pstm.setInt(1, obj.getStaffId());      // Đổi thành getStaffId()
        pstm.setDate(2, obj.getShiftDate());   // Đổi thành getShiftDate()
        pstm.setTime(3, obj.getStartTime());   // Đổi thành getStartTime()
        pstm.setTime(4, obj.getEndTime());     // Đổi thành getEndTime()
        pstm.setInt(5, obj.getId());           // Đổi thành getId();
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_SHIFT)) {
             
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
     * Lấy danh sách ca làm việc của một nhân viên cụ thể
     */
    public List<ShiftDTO> getShiftsByStaffId(int staffId) {
        List<ShiftDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_BY_STAFF_ID)) {
             
            pstm.setInt(1, staffId);
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

    /**
     * Lấy danh sách toàn bộ ca làm việc trong một ngày cụ thể
     */
    public List<ShiftDTO> getShiftsByDate(Date shiftDate) {
        List<ShiftDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_BY_DATE)) {
             
            pstm.setDate(1, shiftDate);
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
    private ShiftDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
    ShiftDTO dto = new ShiftDTO();
    dto.setId(rs.getInt("shift_id"));
    dto.setStaffId(rs.getInt("staff_id"));
    dto.setShiftDate(rs.getDate("shift_date"));
    dto.setStartTime(rs.getTime("start_time"));
    dto.setEndTime(rs.getTime("end_time"));
    
    // Tạm thời gán giá trị để không bị lỗi 500 khi JSP gọi ${s.name}
    dto.setName("Nhân viên #" + rs.getInt("staff_id"));
    dto.setDescription("Lịch trực cố định");
    return dto;
}
}