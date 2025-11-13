package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Thay đổi URL, USER, PASSWORD cho MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/qlsvdb?useSSL=false&serverTimezone=UTC"; // URL của MySQL
    private static final String USER = "root"; // Username của MySQL
    private static final String PASSWORD = ""; // Password của MySQL (thường là rỗng cho root)

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Thay đổi driver Class.forName cho MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối CSDL MySQL thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver JDBC MySQL: " + e.getMessage());
            // Ném RuntimeException để chương trình dừng lại rõ ràng
            throw new RuntimeException("MySQL JDBC Driver not found. Please ensure mysql-connector-j.jar is in your classpath.", e);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối CSDL MySQL: " + e.getMessage());
            // Ném RuntimeException để chương trình dừng lại rõ ràng
            throw new RuntimeException("Failed to connect to MySQL database: " + e.getMessage(), e);
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đóng kết nối CSDL MySQL.");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối CSDL MySQL: " + e.getMessage());
            }
        }
    }
}