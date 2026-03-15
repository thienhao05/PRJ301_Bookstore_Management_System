package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    /**
     * Hàm dùng để băm mật khẩu từ chuỗi thường sang chuỗi Hex (SHA-256)
     * @param password Mật khẩu dạng text bình thường
     * @return Chuỗi đã được băm
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Chuyển đổi byte array sang định dạng Hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi thuật toán bảo mật: " + e.getMessage());
        }
    }

    /**
     * Hàm kiểm tra mật khẩu người dùng nhập vào có khớp với mật khẩu trong DB không
     * @param plainPassword Mật khẩu khách vừa gõ vào form
     * @param hashedPassword Mật khẩu đã băm lấy từ Database
     * @return true nếu khớp, false nếu sai
     */
    public static boolean comparePassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput.equals(hashedPassword);
    }
}