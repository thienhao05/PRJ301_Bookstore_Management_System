package dao;

import dto.BookDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements ICRUD<BookDTO, Integer> {

    @Override
    public List<BookDTO> getAll() {
        List<BookDTO> list = new ArrayList<>();
        // Truy vấn lấy tất cả sách, ưu tiên sách mới nhất
        String sql = "SELECT * FROM Books ORDER BY created_at DESC";
        
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRowToDTO(rs));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public BookDTO getById(Integer id) {
        String sql = "SELECT * FROM Books WHERE book_id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
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

    // Các hàm Insert, Update, Delete tạm để trống, sau này làm trang Admin mình code sau
    @Override
    public boolean insert(BookDTO obj) { return false; }
    @Override
    public boolean update(BookDTO obj) { return false; }
    @Override
    public boolean delete(Integer id) { return false; }

    // ================== HÀM HỖ TRỢ ==================
    private BookDTO mapRowToDTO(ResultSet rs) throws SQLException {
        BookDTO book = new BookDTO();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPrice(rs.getDouble("price"));
        book.setStock(rs.getInt("stock"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setPublisherId(rs.getInt("publisher_id"));
        book.setCreatedAt(rs.getTimestamp("created_at"));
        return book;
    }

    // ================== TEST ==================
    public static void main(String[] args) {
        BookDAO dao = new BookDAO();
        List<BookDTO> books = dao.getAll();
        System.out.println("Tổng số sách lấy được: " + books.size());
        if (!books.isEmpty()) {
            System.out.println("Cuốn sách đầu tiên: " + books.get(0).getTitle() + " - Giá: " + books.get(0).getPrice());
        }
    }
}