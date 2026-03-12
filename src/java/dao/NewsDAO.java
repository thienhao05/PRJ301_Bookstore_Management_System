package dao;

import dto.NewsDTO;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    public List<NewsDTO> getAllNews() {

        List<NewsDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM News";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                NewsDTO n = new NewsDTO(
                        rs.getInt("news_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("staff_id")
                );

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addNews(NewsDTO n) {

        String sql = "INSERT INTO News(title, content, created_at, staff_id) VALUES(?,?,?,?)";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            ps.setTimestamp(3, n.getCreated_at());
            ps.setInt(4, n.getStaff_id());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNews(NewsDTO n) {

        String sql = "UPDATE News SET title=?, content=?, staff_id=? WHERE news_id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            ps.setInt(3, n.getStaff_id());
            ps.setInt(4, n.getNews_id());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNews(int id) {

        String sql = "DELETE FROM News WHERE news_id=?";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}