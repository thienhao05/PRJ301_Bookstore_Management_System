package dao;

import dto.NotificationDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO implements ICRUD<NotificationDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    // Bỏ qua created_at và is_read khi insert vì SQL Server đã có DEFAULT
    private static final String INSERT_NOTIFICATION = "INSERT INTO Notifications (user_id, content) VALUES (?, ?)";
    private static final String SELECT_ALL_NOTIFICATIONS = "SELECT * FROM Notifications ORDER BY created_at DESC";
    private static final String SELECT_NOTIFICATION_BY_ID = "SELECT * FROM Notifications WHERE notification_id = ?";
    private static final String UPDATE_NOTIFICATION = "UPDATE Notifications SET user_id = ?, content = ?, is_read = ? WHERE notification_id = ?";
    private static final String DELETE_NOTIFICATION = "DELETE FROM Notifications WHERE notification_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_USER_ID = "SELECT * FROM Notifications WHERE user_id = ? ORDER BY created_at DESC";
    private static final String COUNT_UNREAD_BY_USER = "SELECT COUNT(*) as unread_count FROM Notifications WHERE user_id = ? AND is_read = 0";
    private static final String MARK_AS_READ = "UPDATE Notifications SET is_read = 1 WHERE notification_id = ?";
    // Thêm dòng này vào danh sách hằng số SQL ở đầu class
    private static final String MARK_ALL_AS_READ = "UPDATE Notifications SET is_read = 1 WHERE user_id = ? AND is_read = 0";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(NotificationDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_NOTIFICATION)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setString(2, obj.getContent());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<NotificationDTO> readAll() {
        List<NotificationDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_NOTIFICATIONS);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public NotificationDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_NOTIFICATION_BY_ID)) {

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
    public boolean update(NotificationDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_NOTIFICATION)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setString(2, obj.getContent());
            pstm.setBoolean(3, obj.isIs_read());
            pstm.setInt(4, obj.getNotification_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_NOTIFICATION)) {

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
     * Lấy danh sách thông báo của riêng một user
     */
    public List<NotificationDTO> getNotificationsByUserId(int userId) {
        List<NotificationDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_BY_USER_ID)) {

            pstm.setInt(1, userId);
            try ( ResultSet rs = pstm.executeQuery()) {
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
     * Đếm số lượng thông báo chưa đọc của user
     */
    public int countUnreadNotifications(int userId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(COUNT_UNREAD_BY_USER)) {

            pstm.setInt(1, userId);
            try ( ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("unread_count");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Đánh dấu một thông báo là đã đọc
     */
    public boolean markAsRead(int notificationId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(MARK_AS_READ)) {

            pstm.setInt(1, notificationId);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private NotificationDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotification_id(rs.getInt("notification_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setContent(rs.getString("content"));
        dto.setCreated_at(rs.getTimestamp("created_at"));

        // Sử dụng hàm isIs_read theo chuẩn DTO của bạn
        dto.setIs_read(rs.getBoolean("is_read"));

        return dto;
    }

    public boolean markAllAsRead(int userId) {
        try (
                 Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(MARK_ALL_AS_READ)) {

            pstm.setInt(1, userId);
            return pstm.executeUpdate() >= 0;
            // Lưu ý: Dùng >= 0 vì nếu không có thông báo nào chưa đọc, 
            // lệnh SQL vẫn chạy đúng (trả về 0) và không coi là lỗi.
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
