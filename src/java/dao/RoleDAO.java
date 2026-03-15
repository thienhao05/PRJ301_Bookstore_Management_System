package dao;

import dto.RoleDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO implements ICRUD<RoleDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_ROLE = "INSERT INTO Roles (role_name) VALUES (?)";
    private static final String SELECT_ALL_ROLES = "SELECT * FROM Roles";
    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM Roles WHERE role_id = ?";
    private static final String UPDATE_ROLE = "UPDATE Roles SET role_name = ? WHERE role_id = ?";
    private static final String DELETE_ROLE = "DELETE FROM Roles WHERE role_id = ?";

    // CÂU LỆNH SQL MỞ RỘNG
    private static final String SELECT_ROLE_BY_NAME = "SELECT * FROM Roles WHERE role_name = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(RoleDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_ROLE)) {
             
            pstm.setString(1, obj.getRoleName());
            return pstm.executeUpdate() > 0;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<RoleDTO> readAll() {
        List<RoleDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_ROLES);
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
    public RoleDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ROLE_BY_ID)) {
             
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
    public boolean update(RoleDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_ROLE)) {
             
            pstm.setString(1, obj.getRoleName());
            pstm.setInt(2, obj.getRoleId());
            return pstm.executeUpdate() > 0;
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Lưu ý: Trong thực tế, ít khi xóa Role vì nó dính khóa ngoại tới Users.
        // Cần đảm bảo không có user nào đang giữ role_id này trước khi xóa.
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_ROLE)) {
             
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
     * Lấy thông tin Role dựa trên tên (Ví dụ: truyền vào "Customer" để lấy roleId)
     */
    public RoleDTO getRoleByName(String roleName) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ROLE_BY_NAME)) {
             
            pstm.setString(1, roleName);
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
    private RoleDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(rs.getInt("role_id"));
        dto.setRoleName(rs.getString("role_name"));
        return dto;
    }
}