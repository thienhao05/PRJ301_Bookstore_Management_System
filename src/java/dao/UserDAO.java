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

    private static final String INSERT_USER
            = "INSERT INTO Users (full_name, email, password_hash, phone, role_id, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Users";
    private static final String SELECT_BY_ID = "SELECT * FROM Users WHERE user_id = ?";
    private static final String UPDATE_USER
            = "UPDATE Users SET full_name=?, email=?, password_hash=?, phone=?, role_id=?, status=? WHERE user_id=?";
    private static final String DELETE_USER = "DELETE FROM Users WHERE user_id = ?";
    private static final String CHECK_LOGIN
            = "SELECT * FROM Users WHERE email = ? AND password_hash = ? AND status = 'Active'";
    private static final String CHECK_EMAIL = "SELECT 1 FROM Users WHERE email = ?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";

    @Override
    public boolean create(UserDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_USER)) {

            pstm.setString(1, obj.getUsername());   // full_name
            pstm.setString(2, obj.getEmail());
            pstm.setString(3, obj.getPassword());   // password_hash
            pstm.setString(4, obj.getPhone());      // phone
            pstm.setInt(5, obj.getRoleId());
            pstm.setString(6, obj.isStatus() ? "Active" : "Inactive");

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<UserDTO> readAll() {
        List<UserDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL);  ResultSet rs = pstm.executeQuery()) {

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
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_ID)) {

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
    public boolean update(UserDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_USER)) {

            pstm.setString(1, obj.getUsername());
            pstm.setString(2, obj.getEmail());
            pstm.setString(3, obj.getPassword());
            pstm.setString(4, obj.getPhone());
            pstm.setInt(5, obj.getRoleId());
            pstm.setString(6, obj.isStatus() ? "Active" : "Inactive");
            pstm.setInt(7, obj.getUserId());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_USER)) {

            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------------------------------------------------
    // CUSTOM METHODS
    // ----------------------------------------------------------------
    public UserDTO login(String email, String password) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(CHECK_LOGIN)) {

            pstm.setString(1, email);
            pstm.setString(2, password);
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

    public boolean checkEmailExist(String email) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(CHECK_EMAIL)) {

            pstm.setString(1, email);
            try ( ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserDTO getUserByEmail(String email) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_EMAIL)) {

            pstm.setString(1, email);
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

    // ----------------------------------------------------------------
    // MAPPER
    // ----------------------------------------------------------------
    private UserDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        UserDTO dto = new UserDTO();
        dto.setUserId(rs.getInt("user_id"));
        dto.setUsername(rs.getString("full_name"));     // full_name -> username
        dto.setEmail(rs.getString("email"));
        dto.setPassword(rs.getString("password_hash"));
        dto.setPhone(rs.getString("phone"));            // thêm phone
        dto.setRoleId(rs.getInt("role_id"));
        String status = rs.getString("status");
        dto.setStatus(status != null && status.equalsIgnoreCase("Active"));
        return dto;
    }
}
