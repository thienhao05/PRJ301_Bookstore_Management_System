package dao;

import dto.AddressDTO;
import utils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    // ==============================================================
    // 1. TẠO ĐỊA CHỈ MỚI (dùng trong AddressController)
    // ==============================================================
    public boolean create(AddressDTO dto) {
        String sql = "INSERT INTO Addresses (user_id, full_address, city, district, is_default) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, dto.getUser_id());
            pstm.setString(2, dto.getFull_address());
            pstm.setString(3, dto.getCity());
            pstm.setString(4, dto.getDistrict());
            pstm.setBoolean(5, dto.isIs_default());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // 2. TẠO ĐỊA CHỈ MỚI VÀ TRẢ VỀ ID (dùng trong OrderController khi placeOrder)
    // ==============================================================
    public int createAndGetId(int userId, String fullAddress, String city, String district) {
        String sql = "INSERT INTO Addresses (user_id, full_address, city, district, is_default) " +
                     "VALUES (?, ?, ?, ?, 0)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, userId);
            pstm.setString(2, fullAddress);
            pstm.setString(3, city != null ? city : "");
            pstm.setString(4, district != null ? district : "");

            int rows = pstm.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // ✅ Trả về address_id vừa tạo
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Lỗi
    }

    // ==============================================================
    // 3. LẤY DANH SÁCH ĐỊA CHỈ THEO USER (dùng trong AddressController)
    // ==============================================================
    public List<AddressDTO> readAllByUserId(int userId) {
        List<AddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Addresses WHERE user_id = ? ORDER BY is_default DESC";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, userId);
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
    // 4. LẤY 1 ĐỊA CHỈ THEO ID (dùng trong AddressController - edit)
    // ==============================================================
    public AddressDTO readById(int addressId) {
        String sql = "SELECT * FROM Addresses WHERE address_id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, addressId);
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
    // 5. CẬP NHẬT ĐỊA CHỈ (dùng trong AddressController - update)
    // ==============================================================
    public boolean update(AddressDTO dto) {
        String sql = "UPDATE Addresses SET full_address = ?, city = ?, district = ?, is_default = ? " +
                     "WHERE address_id = ? AND user_id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, dto.getFull_address());
            pstm.setString(2, dto.getCity());
            pstm.setString(3, dto.getDistrict());
            pstm.setBoolean(4, dto.isIs_default());
            pstm.setInt(5, dto.getAddress_id());
            pstm.setInt(6, dto.getUser_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // 6. XÓA ĐỊA CHỈ (dùng trong AddressController - delete)
    // ==============================================================
    public boolean delete(int addressId) {
        String sql = "DELETE FROM Addresses WHERE address_id = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, addressId);
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // 7. ĐẶT LÀM ĐỊA CHỈ MẶC ĐỊNH (dùng trong AddressController - setDefault)
    // ==============================================================
    public boolean setDefaultAddress(int addressId, int userId) {
        // Bước 1: Bỏ mặc định tất cả địa chỉ của user
        String resetSql = "UPDATE Addresses SET is_default = 0 WHERE user_id = ?";
        // Bước 2: Đặt địa chỉ được chọn làm mặc định
        String setSql   = "UPDATE Addresses SET is_default = 1 WHERE address_id = ? AND user_id = ?";

        try (Connection conn = DbUtils.getConnection()) {
            // Tắt auto-commit để thực hiện 2 câu lệnh trong 1 transaction
            conn.setAutoCommit(false);

            try (PreparedStatement reset = conn.prepareStatement(resetSql);
                 PreparedStatement set   = conn.prepareStatement(setSql)) {

                reset.setInt(1, userId);
                reset.executeUpdate();

                set.setInt(1, addressId);
                set.setInt(2, userId);
                set.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==============================================================
    // MAPPER: ResultSet → AddressDTO
    // ==============================================================
    private AddressDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        AddressDTO dto = new AddressDTO();
        dto.setAddress_id(rs.getInt("address_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setFull_address(rs.getString("full_address"));
        dto.setCity(rs.getString("city"));
        dto.setDistrict(rs.getString("district"));
        dto.setIs_default(rs.getBoolean("is_default"));
        return dto;
    }
}