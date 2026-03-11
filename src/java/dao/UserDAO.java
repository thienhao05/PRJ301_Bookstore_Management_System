package dao;

import dto.UserDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements ICRUD<UserDTO, Integer> {

    // ================== CÁC HÀM RIÊNG CỦA USER ==================

    public UserDTO checkLogin(String email, String password) {
        // Đã sửa lại tên cột password_hash và status = 'active'
        String sql = "SELECT * FROM Users WHERE email = ? AND password_hash = ? AND status = 'active'";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToDTO(rs);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Các hàm khác giữ nguyên (checkEmailExist, getAll, getById...)
    // ... (Ông giữ nguyên phần code cũ của các hàm này nhé, tui không viết lại để đỡ rối) ...

    @Override
    public List<UserDTO> getAll() { return new ArrayList<>(); } // Tạm để trống
    @Override
    public UserDTO getById(Integer id) { return null; }         // Tạm để trống
    @Override
    public boolean insert(UserDTO obj) { return false; }        // Tạm để trống
    @Override
    public boolean update(UserDTO obj) { return false; }        // Tạm để trống
    @Override
    public boolean delete(Integer id) { return false; }         // Tạm để trống

    // ================== HÀM HỖ TRỢ ==================

    private UserDTO mapRowToDTO(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        // Đã sửa lại tên cột cho khớp với Database
        user.setId(rs.getInt("user_id")); 
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getInt("role_id"));
        user.setStatus(rs.getString("status"));
        return user;
    }
}