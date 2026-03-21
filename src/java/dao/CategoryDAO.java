package dao;

import dto.CategoryDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICRUD<CategoryDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_CATEGORY = "INSERT INTO Categories (name, description) VALUES (?, ?)";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM Categories";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM Categories WHERE category_id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE Categories SET name = ?, description = ? WHERE category_id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM Categories WHERE category_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    // Tìm kiếm danh mục theo tên (Hỗ trợ thanh search cho Admin)
    private static final String SEARCH_CATEGORY_BY_NAME = "SELECT * FROM Categories WHERE name LIKE ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(CategoryDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_CATEGORY)) {
             
            pstm.setString(1, obj.getName());
            pstm.setString(2, obj.getDescription());
            
            // NẾU BẠN ĐÃ THÊM CỘT STATUS VÀO SQL, sửa chuỗi INSERT thành: 
            // "INSERT INTO Categories (name, description, status) VALUES (?, ?, ?)"
            // Và mở comment dòng dưới:
            // pstm.setInt(3, obj.getStatus());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CategoryDTO> readAll() {
        List<CategoryDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_CATEGORIES);
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
    public CategoryDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_CATEGORY_BY_ID)) {
             
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
    public boolean update(CategoryDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_CATEGORY)) {
             
            pstm.setString(1, obj.getName());
            pstm.setString(2, obj.getDescription());
            pstm.setInt(3, obj.getId());
            
            // Tương tự, nếu có cột status trong DB thì thêm vào chuỗi UPDATE và set:
            // pstm.setInt(3, obj.getStatus());
            // pstm.setInt(4, obj.getId());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Lưu ý: Cần cẩn thận khi xóa Category vì nó liên kết với bảng Books.
        // Thực tế nên update status = 0 (ẩn) thay vì xóa vật lý (DELETE).
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_CATEGORY)) {
             
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
     * Tìm kiếm danh mục bằng từ khóa (Tương đối)
     */
    public List<CategoryDTO> searchCategoryByName(String keyword) {
        List<CategoryDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SEARCH_CATEGORY_BY_NAME)) {
             
            pstm.setString(1, "%" + keyword + "%");
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
    private CategoryDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        CategoryDTO dto = new CategoryDTO();
        
        // Khớp biến "id" trong DTO với cột "category_id" trong SQL
        dto.setId(rs.getInt("category_id"));
        dto.setName(rs.getString("name"));
        dto.setDescription(rs.getString("description"));
        
        // Vì SQL hiện tại chưa có cột status, mình set mặc định là 1 (Active)
        // Nếu DB có cột status, hãy đổi thành: dto.setStatus(rs.getInt("status"));
        dto.setStatus(1); 
        
        return dto;
    }
}