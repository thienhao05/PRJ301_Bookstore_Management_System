package dao;

import dto.PublisherDTO;
import utils.DbUtils;
import java.sql.*;
import java.util.*;

public class PublisherDAO {

    public List<PublisherDTO> getAllPublishers() {

        List<PublisherDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Publishers";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PublisherDTO p = new PublisherDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addPublisher(PublisherDTO p) {

        String sql = "INSERT INTO Publishers(name,description) VALUES(?,?)";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePublisher(PublisherDTO p) {

        String sql = "UPDATE Publishers SET name=?, description=? WHERE id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePublisher(int id) {

        String sql = "DELETE FROM Publishers WHERE id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}