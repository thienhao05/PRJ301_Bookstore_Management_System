package dao;

import dto.ReviewDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements ICRUD<ReviewDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_REVIEW = "INSERT INTO Reviews (user_id, book_id, rating, comment) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_REVIEWS = "SELECT * FROM Reviews ORDER BY review_date DESC";
    private static final String SELECT_REVIEW_BY_ID = "SELECT * FROM Reviews WHERE review_id = ?";
    private static final String UPDATE_REVIEW = "UPDATE Reviews SET rating = ?, comment = ? WHERE review_id = ?";
    private static final String DELETE_REVIEW = "DELETE FROM Reviews WHERE review_id = ?";

    // CUSTOM SQL
    private static final String SELECT_REVIEWS_BY_BOOK_ID = "SELECT * FROM Reviews WHERE book_id = ? ORDER BY review_date DESC";
    private static final String SELECT_AVERAGE_RATING = "SELECT AVG(CAST(rating AS FLOAT)) as avg_rating FROM Reviews WHERE book_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(ReviewDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_REVIEW)) {
             
            pstm.setInt(1, obj.getUser_id());
            pstm.setInt(2, obj.getBook_id());
            pstm.setInt(3, obj.getRating());
            pstm.setString(4, obj.getComment());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ReviewDTO> readAll() {
        List<ReviewDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_REVIEWS);
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
    public ReviewDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_REVIEW_BY_ID)) {
             
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
    public boolean update(ReviewDTO obj) {
        // Thông thường người dùng chỉ được sửa rating và comment của chính họ
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_REVIEW)) {
             
            pstm.setInt(1, obj.getRating());
            pstm.setString(2, obj.getComment());
            pstm.setInt(3, obj.getReview_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_REVIEW)) {
             
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
     * Lấy danh sách đánh giá của một cuốn sách cụ thể
     */
    public List<ReviewDTO> getReviewsByBookId(int bookId) {
        List<ReviewDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_REVIEWS_BY_BOOK_ID)) {
             
            pstm.setInt(1, bookId);
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
     * Tính điểm đánh giá trung bình của một cuốn sách (Trả về kiểu double)
     */
    public double getAverageRatingByBookId(int bookId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_AVERAGE_RATING)) {
             
            pstm.setInt(1, bookId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    // Làm tròn 1 chữ số thập phân, ví dụ: 4.5
                    return Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private ReviewDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ReviewDTO dto = new ReviewDTO();
        dto.setReview_id(rs.getInt("review_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setBook_id(rs.getInt("book_id"));
        dto.setRating(rs.getInt("rating"));
        dto.setComment(rs.getString("comment"));
        dto.setReview_date(rs.getTimestamp("review_date"));
        return dto;
    }
}