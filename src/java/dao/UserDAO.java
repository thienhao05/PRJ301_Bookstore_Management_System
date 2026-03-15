package dao;

import dto.UserDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements ICRUD<UserDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_USER = "INSERT INTO Users (full_name, email, password_hash, role_id, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM Users WHERE user_id = ?";
    private static final String UPDATE_USER = "UPDATE Users SET full_name = ?, email = ?, password_hash = ?, role_id = ?, status = ? WHERE user_id = ?";
    private static final String DELETE_USER = "DELETE FROM Users WHERE user_id = ?";
    
    // ==============================================================
    // CUSTOM SQL 
    // ==============================================================
    private static final String CHECK_LOGIN = "SELECT * FROM Users WHERE email = ? AND password_hash = ? AND status = 'Active'";
    private static final String CHECK_EMAIL = "SELECT 1 FROM Users WHERE email = ?";
    // Câu lệnh SQL mới để tìm User bằng Email
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";

    @Override
    public boolean create(UserDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_USER)) {
            
            pstm.setString(1, obj.getUsername()); 
            pstm.setString(2, obj.getEmail());
            pstm.setString(3, obj.getPassword()); 
            pstm.setInt(4, obj.getRoleId());
            pstm.setString(5, obj.isStatus() ? "Active" : "Inactive"); 
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<UserDTO> readAll() {
        List<UserDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_USERS);
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
    public UserDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_USER_BY_ID)) {
             
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
    public boolean update(UserDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_USER)) {
             
            pstm.setString(1, obj.getUsername());
            pstm.setString(2, obj.getEmail());
            pstm.setString(3, obj.getPassword());
            pstm.setInt(4, obj.getRoleId());
            pstm.setString(5, obj.isStatus() ? "Active" : "Inactive");
            pstm.setInt(6, obj.getUserId());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_USER)) {
             
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

    public UserDTO login(String email, String password) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(CHECK_LOGIN)) {
             
            pstm.setString(1, email);
            pstm.setString(2, password);
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

    public boolean checkEmailExist(String email) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(CHECK_EMAIL)) {
             
            pstm.setString(1, email);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy thông tin User đầy đủ thông qua Email
     */
    public UserDTO getUserByEmail(String email) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_USER_BY_EMAIL)) {
            
            pstm.setString(1, email);
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
    private UserDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        UserDTO dto = new UserDTO();
        dto.setUserId(rs.getInt("user_id"));
        
        dto.setUsername(rs.getString("full_name"));
        dto.setEmail(rs.getString("email"));
        dto.setPassword(rs.getString("password_hash"));
        dto.setRoleId(rs.getInt("role_id"));
        
        String statusStr = rs.getString("status");
        dto.setStatus(statusStr != null && statusStr.equalsIgnoreCase("Active"));
        
        return dto;
    }
}