package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Thuật toán SHA-256 không tồn tại: " + e.getMessage());
            throw new RuntimeException("Lỗi hashing mật khẩu", e);
        }
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        String hashedAttempt = hashPassword(plainPassword);
        return hashedAttempt.equals(hashedPassword);
    }

    // Hàm main để test hoặc tạo hash cho mật khẩu admin
    public static void main(String[] args) {
    String myPassword = "123"; // THAY ĐỔI THÀNH MẬT KHẨU MONG MUỐN CỦA BẠN
    String hashedMyPassword = hashPassword(myPassword);
    System.out.println("Mật khẩu '" + myPassword + "' được hash là: " + hashedMyPassword);
}
}