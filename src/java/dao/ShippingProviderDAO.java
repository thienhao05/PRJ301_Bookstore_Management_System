package dao;

import dto.ShippingProviderDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingProviderDAO implements ICRUD<ShippingProviderDTO> {

    // ==============================================================
    // CÁC CÂU LỆNH SQL
    // ==============================================================
    private static final String INSERT_PROVIDER = "INSERT INTO ShippingProviders (name, phone, fee) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_PROVIDERS = "SELECT * FROM ShippingProviders";
    private static final String SELECT_PROVIDER_BY_ID = "SELECT * FROM ShippingProviders WHERE shipping_provider_id = ?";
    private static final String UPDATE_PROVIDER = "UPDATE ShippingProviders SET name = ?, phone = ?, fee = ? WHERE shipping_provider_id = ?";
    private static final String DELETE_PROVIDER = "DELETE FROM ShippingProviders WHERE shipping_provider_id = ?";

    // ==============================================================
    // TRIỂN KHAI CÁC HÀM TỪ INTERFACE ICRUD
    // ==============================================================
    @Override
    public boolean create(ShippingProviderDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(INSERT_PROVIDER)) {
             
            pstm.setString(1, obj.getName());
            pstm.setString(2, obj.getPhone());
            pstm.setDouble(3, obj.getFee());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ShippingProviderDTO> readAll() {
        List<ShippingProviderDTO> list = new ArrayList<>();
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_ALL_PROVIDERS);
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
    public ShippingProviderDTO readById(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(SELECT_PROVIDER_BY_ID)) {
             
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
    public boolean update(ShippingProviderDTO obj) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(UPDATE_PROVIDER)) {
             
            pstm.setString(1, obj.getName());
            pstm.setString(2, obj.getPhone());
            pstm.setDouble(3, obj.getFee());
            pstm.setInt(4, obj.getShipping_provider_id());
            
            return pstm.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement pstm = conn.prepareStatement(DELETE_PROVIDER)) {
             
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
    private ShippingProviderDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ShippingProviderDTO dto = new ShippingProviderDTO();
        dto.setShipping_provider_id(rs.getInt("shipping_provider_id"));
        dto.setName(rs.getString("name"));
        dto.setPhone(rs.getString("phone"));
        dto.setFee(rs.getDouble("fee"));
        return dto;
    }
}