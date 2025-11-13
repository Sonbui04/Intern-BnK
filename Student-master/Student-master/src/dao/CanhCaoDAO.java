package dao;

import model.CanhCao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CanhCaoDAO {

    // Constructor của DAO đã được làm gọn vì DatabaseConnection đã tải driver
    public CanhCaoDAO() {
        // Không cần Class.forName ở đây
    }

    public boolean addCanhCao(CanhCao canhCao) throws SQLException {
        String sql = "INSERT INTO canhcao (ten_canh_cao, mo_ta) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, canhCao.getTenCanhCao());
            pstmt.setString(2, canhCao.getMoTa());
            int rowsAffected = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    canhCao.setId(generatedKeys.getInt(1)); // Gán lại ID tự tăng
                }
            }
            return rowsAffected > 0;
        }
    }

    public boolean updateCanhCao(CanhCao canhCao) throws SQLException {
        String sql = "UPDATE canhcao SET ten_canh_cao = ?, mo_ta = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, canhCao.getTenCanhCao());
            pstmt.setString(2, canhCao.getMoTa());
            pstmt.setInt(3, canhCao.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCanhCao(int id) throws SQLException {
        String sql = "DELETE FROM canhcao WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<CanhCao> getAllCanhCaos() throws SQLException {
        List<CanhCao> canhCaos = new ArrayList<>();
        String sql = "SELECT id, ten_canh_cao, mo_ta FROM canhcao";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                CanhCao canhCao = new CanhCao();
                canhCao.setId(rs.getInt("id"));
                canhCao.setTenCanhCao(rs.getString("ten_canh_cao"));
                canhCao.setMoTa(rs.getString("mo_ta"));
                canhCaos.add(canhCao);
            }
        }
        return canhCaos;
    }

    public List<CanhCao> searchCanhCaos(String keyword) throws SQLException {
        List<CanhCao> canhCaos = new ArrayList<>();
        String sql = "SELECT id, ten_canh_cao, mo_ta FROM canhcao WHERE ten_canh_cao LIKE ? OR mo_ta LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CanhCao canhCao = new CanhCao();
                    canhCao.setId(rs.getInt("id"));
                    canhCao.setTenCanhCao(rs.getString("ten_canh_cao"));
                    canhCao.setMoTa(rs.getString("mo_ta"));
                    canhCaos.add(canhCao);
                }
            }
        }
        return canhCaos;
    }

    public CanhCao getCanhCaoById(int id) throws SQLException {
        String sql = "SELECT id, ten_canh_cao, mo_ta FROM canhcao WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CanhCao canhCao = new CanhCao();
                    canhCao.setId(rs.getInt("id"));
                    canhCao.setTenCanhCao(rs.getString("ten_canh_cao"));
                    canhCao.setMoTa(rs.getString("mo_ta"));
                    return canhCao;
                }
            }
        }
        return null;
    }
}