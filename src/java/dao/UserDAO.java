package dao;

import dto.UserDTO;
import utils.DbUtils;
import java.sql.*;
import java.util.*;

public class UserDAO implements ICRUD<UserDTO> {

    // LOGIN
    public UserDTO login(String username, String password) {
        // Map username vào cột email, password vào password_hash và status so sánh với chữ 'Active'
        String sql = "SELECT * FROM Users WHERE email=? AND password_hash=? AND status='Active'";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserDTO(
                        rs.getInt("user_id"), // Tên cột trong DB
                        rs.getString("full_name"), // Tên cột trong DB (đẩy vào biến username của DTO)
                        rs.getString("password_hash"),// Tên cột trong DB
                        rs.getString("email"),
                        rs.getInt("role_id"), // Tên cột trong DB
                        "Active".equalsIgnoreCase(rs.getString("status")) // Chuyển chữ 'Active' thành boolean true
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // CREATE
    @Override
    public boolean create(UserDTO obj) {
        String sql = "INSERT INTO Users(full_name, password_hash, email, role_id, status) VALUES(?,?,?,?,?)";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, obj.getUsername()); // Lấy tên DTO gán vào full_name
            ps.setString(2, obj.getPassword()); // Lấy pass DTO gán vào password_hash
            ps.setString(3, obj.getEmail());
            ps.setInt(4, obj.getRoleId());
            ps.setString(5, obj.isStatus() ? "Active" : "Inactive"); // Chuyển true/false thành chữ

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // READ BY ID
    @Override
    public UserDTO read(int id) {
        String sql = "SELECT * FROM Users WHERE user_id=?";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserDTO(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("password_hash"),
                        rs.getString("email"),
                        rs.getInt("role_id"),
                        "Active".equalsIgnoreCase(rs.getString("status"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ ALL
    @Override
    public List<UserDTO> readAll() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserDTO user = new UserDTO(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("password_hash"),
                        rs.getString("email"),
                        rs.getInt("role_id"),
                        "Active".equalsIgnoreCase(rs.getString("status"))
                );
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    @Override
    public boolean update(UserDTO obj) {
        String sql = "UPDATE Users SET full_name=?, password_hash=?, email=?, role_id=?, status=? WHERE user_id=?";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, obj.getUsername());
            ps.setString(2, obj.getPassword());
            ps.setString(3, obj.getEmail());
            ps.setInt(4, obj.getRoleId());
            ps.setString(5, obj.isStatus() ? "Active" : "Inactive");
            ps.setInt(6, obj.getUserId()); // Khóa chính để update

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Users WHERE user_id=?";

        try ( Connection con = DbUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
