package dao;

import dto.NewsDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO implements ICRUD<NewsDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    // Bỏ qua created_at vì database đã set default là GETDATE()
    private static final String INSERT_NEWS = "INSERT INTO News (title, content, staff_id) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_NEWS = "SELECT * FROM News ORDER BY created_at DESC";
    private static final String SELECT_NEWS_BY_ID = "SELECT * FROM News WHERE news_id = ?";
    private static final String UPDATE_NEWS = "UPDATE News SET title = ?, content = ? WHERE news_id = ?";
    private static final String DELETE_NEWS = "DELETE FROM News WHERE news_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    // Dùng TOP (?) trong SQL Server để giới hạn số lượng bài viết mới nhất
    private static final String SELECT_RECENT_NEWS = "SELECT TOP (?) * FROM News ORDER BY created_at DESC";
    private static final String SELECT_NEWS_BY_STAFF = "SELECT * FROM News WHERE staff_id = ? ORDER BY created_at DESC";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(NewsDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_NEWS)) {
             
            pstm.setString(1, obj.getTitle());
            pstm.setString(2, obj.getContent());
            pstm.setInt(3, obj.getStaff_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<NewsDTO> readAll() {
        List<NewsDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_NEWS);
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
    public NewsDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_NEWS_BY_ID)) {
             
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
    public boolean update(NewsDTO obj) {
        // Thông thường khi update bài viết, ta chỉ sửa tiêu đề và nội dung,
        // giữ nguyên staff_id (người viết ban đầu) và created_at.
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_NEWS)) {
             
            pstm.setString(1, obj.getTitle());
            pstm.setString(2, obj.getContent());
            pstm.setInt(3, obj.getNews_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_NEWS)) {
             
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
     * Lấy danh sách N bài tin tức mới nhất (hiển thị trang chủ)
     */
    public List<NewsDTO> getRecentNews(int limit) {
        List<NewsDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_RECENT_NEWS)) {
             
            pstm.setInt(1, limit);
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
     * Lấy danh sách bài viết do một nhân viên cụ thể đăng tải (Trang quản lý của Staff)
     */
    public List<NewsDTO> getNewsByStaffId(int staffId) {
        List<NewsDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_NEWS_BY_STAFF)) {
             
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

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private NewsDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        NewsDTO dto = new NewsDTO();
        dto.setNews_id(rs.getInt("news_id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setCreated_at(rs.getTimestamp("created_at"));
        dto.setStaff_id(rs.getInt("staff_id"));
        return dto;
    }
}