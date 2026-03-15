package dao;

import dto.PublisherDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO implements ICRUD<PublisherDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    // Tạm bỏ qua cột 'phone' vì DTO không chứa trường này
    private static final String INSERT_PUBLISHER = "INSERT INTO Publishers (name, address) VALUES (?, ?)";
    private static final String SELECT_ALL_PUBLISHERS = "SELECT * FROM Publishers";
    private static final String SELECT_PUBLISHER_BY_ID = "SELECT * FROM Publishers WHERE publisher_id = ?";
    private static final String UPDATE_PUBLISHER = "UPDATE Publishers SET name = ?, address = ? WHERE publisher_id = ?";
    private static final String DELETE_PUBLISHER = "DELETE FROM Publishers WHERE publisher_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(PublisherDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_PUBLISHER)) {
             
            pstm.setString(1, obj.getName());
            // Map description trong DTO xuống cột address trong SQL
            pstm.setString(2, obj.getDescription()); 
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<PublisherDTO> readAll() {
        List<PublisherDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_PUBLISHERS);
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
    public PublisherDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_PUBLISHER_BY_ID)) {
             
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
    public boolean update(PublisherDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_PUBLISHER)) {
             
            pstm.setString(1, obj.getName());
            pstm.setString(2, obj.getDescription());
            pstm.setInt(3, obj.getId());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_PUBLISHER)) {
             
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private PublisherDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        PublisherDTO dto = new PublisherDTO();
        
        // Map linh hoạt giữa cột DB và tên biến DTO
        dto.setId(rs.getInt("publisher_id"));
        dto.setName(rs.getString("name"));
        dto.setDescription(rs.getString("address")); 
        
        return dto;
    }
}