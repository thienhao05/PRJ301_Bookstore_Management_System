package dao;

import dto.ShippingProviderDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShippingProviderDAO {

    public List<ShippingProviderDTO> getAllShippingProviders() {

        List<ShippingProviderDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM ShippingProvider";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ShippingProviderDTO s = new ShippingProviderDTO(
                        rs.getInt("shipping_provider_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getDouble("fee")
                );

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addShippingProvider(ShippingProviderDTO s) {

        String sql = "INSERT INTO ShippingProvider(name, phone, fee) VALUES(?,?,?)";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setDouble(3, s.getFee());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShippingProvider(ShippingProviderDTO s) {

        String sql = "UPDATE ShippingProvider SET name=?, phone=?, fee=? WHERE shipping_provider_id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setDouble(3, s.getFee());
            ps.setInt(4, s.getShipping_provider_id());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteShippingProvider(int id) {

        String sql = "DELETE FROM ShippingProvider WHERE shipping_provider_id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}