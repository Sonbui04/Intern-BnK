package dao;

import model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    public RoleDAO() {
    }

    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, role_name FROM roles";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
            }
        }
        return roles;
    }

    public Role getRoleById(int id) throws SQLException {
        String sql = "SELECT id, role_name FROM roles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setRoleName(rs.getString("role_name"));
                    return role;
                }
            }
        }
        return null;
    }

    public Role getRoleByName(String roleName) throws SQLException {
        String sql = "SELECT id, role_name FROM roles WHERE role_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setRoleName(rs.getString("role_name"));
                    return role;
                }
            }
        }
        return null;
    }
}