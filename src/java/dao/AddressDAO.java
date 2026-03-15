package dao;

import dto.AddressDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO implements ICRUD<AddressDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL CƠ BẢN
    // ==============================================================
    private static final String INSERT_ADDRESS = "INSERT INTO Addresses (user_id, full_address, city, district, is_default) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_ADDRESSES = "SELECT * FROM Addresses";
    private static final String SELECT_ADDRESS_BY_ID = "SELECT * FROM Addresses WHERE address_id = ?";
    private static final String UPDATE_ADDRESS = "UPDATE Addresses SET user_id = ?, full_address = ?, city = ?, district = ?, is_default = ? WHERE address_id = ?";
    private static final String DELETE_ADDRESS = "DELETE FROM Addresses WHERE address_id = ?";

    // ==============================================================
    // CÂU LỆNH SQL NGHIỆP VỤ (CUSTOM SQL)
    // ==============================================================
    private static final String SELECT_BY_USER_ID = "SELECT * FROM Addresses WHERE user_id = ? ORDER BY is_default DESC";
    private static final String SELECT_DEFAULT_ADDRESS = "SELECT * FROM Addresses WHERE user_id = ? AND is_default = 1";

    // Hai câu lệnh để xử lý logic set địa chỉ mặc định
    private static final String REMOVE_ALL_DEFAULTS = "UPDATE Addresses SET is_default = 0 WHERE user_id = ?";
    private static final String SET_DEFAULT_ADDRESS = "UPDATE Addresses SET is_default = 1 WHERE address_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(AddressDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(INSERT_ADDRESS)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setString(2, obj.getFull_address());
            pstm.setString(3, obj.getCity());
            pstm.setString(4, obj.getDistrict());
            pstm.setBoolean(5, obj.isIs_default()); // Ép kiểu từ boolean sang BIT của SQL

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<AddressDTO> readAll() {
        List<AddressDTO> list = new ArrayList<>();
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_ADDRESSES);  ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public AddressDTO readById(int id) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_ADDRESS_BY_ID)) {

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
    public boolean update(AddressDTO obj) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(UPDATE_ADDRESS)) {

            pstm.setInt(1, obj.getUser_id());
            pstm.setString(2, obj.getFull_address());
            pstm.setString(3, obj.getCity());
            pstm.setString(4, obj.getDistrict());
            pstm.setBoolean(5, obj.isIs_default());
            pstm.setInt(6, obj.getAddress_id());

            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        // Cần lưu ý không xóa địa chỉ nếu nó đang được liên kết bởi một Order cũ
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(DELETE_ADDRESS)) {

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
     * Lấy toàn bộ sổ địa chỉ của User (sắp xếp địa chỉ mặc định lên đầu tiên)
     */
    public List<AddressDTO> getAddressesByUserId(int userId) {
        List<AddressDTO> list = new ArrayList<>();
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
     * Lấy ra địa chỉ mặc định để tự động điền vào form thanh toán (Checkout)
     */
    public AddressDTO getDefaultAddress(int userId) {
        try ( Connection conn = DbUtils.getConnection();  PreparedStatement pstm = conn.prepareStatement(SELECT_DEFAULT_ADDRESS)) {

            pstm.setInt(1, userId);
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

    /**
     * Cập nhật địa chỉ mặc định (Reset toàn bộ về 0, rồi set địa chỉ được chọn
     * thành 1)
     */
    public boolean setDefaultAddress(int addressId, int userId) {
        Connection conn = null;
        try {
            conn = DbUtils.getConnection();
            // Tắt auto commit để thực hiện 2 câu lệnh SQL như một Transaction (giao dịch)
            conn.setAutoCommit(false);

            // Bước 1: Gỡ bỏ mặc định của tất cả địa chỉ cũ
            try ( PreparedStatement pstm1 = conn.prepareStatement(REMOVE_ALL_DEFAULTS)) {
                pstm1.setInt(1, userId);
                pstm1.executeUpdate();
            }

            // Bước 2: Set địa chỉ mới thành mặc định
            try ( PreparedStatement pstm2 = conn.prepareStatement(SET_DEFAULT_ADDRESS)) {
                pstm2.setInt(1, addressId);
                pstm2.executeUpdate();
            }

            // Hoàn tất giao dịch
            conn.commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Trả lại hiện trạng ban đầu nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // ==============================================================
    // HÀM MAPPER (SQL -> DTO)
    // ==============================================================
    private AddressDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        AddressDTO dto = new AddressDTO();
        dto.setAddress_id(rs.getInt("address_id"));
        dto.setUser_id(rs.getInt("user_id"));
        dto.setFull_address(rs.getString("full_address"));
        dto.setCity(rs.getString("city"));
        dto.setDistrict(rs.getString("district"));

        // ResultSet lấy giá trị BIT thành boolean cho Java
        dto.setIs_default(rs.getBoolean("is_default"));

        return dto;
    }

    /**
     * Lấy toàn bộ sổ địa chỉ của User (đã đổi tên để khớp với Controller)
     */
    public List<AddressDTO> readAllByUserId(int userId) {
        List<AddressDTO> list = new ArrayList<>();
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
    
    
}
