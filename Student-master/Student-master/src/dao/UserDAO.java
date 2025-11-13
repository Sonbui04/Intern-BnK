package dao;

import model.User;
import model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public UserDAO() {
    }

    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, role_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getRoleId());
            int rowsAffected = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            return rowsAffected > 0;
        }
    }

    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password_hash = ?, full_name = ?, email = ?, role_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getRoleId());
            pstmt.setInt(6, user.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT u.id, u.username, u.password_hash, u.full_name, u.email, u.role_id, r.role_name " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRoleId(rs.getInt("role_id"));

                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    user.setRole(role);
                    return user;
                }
            }
        }
        return null;
    }

    // THÊM PHƯƠNG THỨC NÀY
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT u.id, u.username, u.password_hash, u.full_name, u.email, u.role_id, r.role_name " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRoleId(rs.getInt("role_id"));

                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    user.setRole(role);
                    return user;
                }
            }
        }
        return null;
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.password_hash, u.full_name, u.email, u.role_id, r.role_name " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));

                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                user.setRole(role);
                users.add(user);
            }
        }
        return users;
    }
}