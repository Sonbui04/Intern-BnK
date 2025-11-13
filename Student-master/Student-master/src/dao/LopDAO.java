package dao;

import model.Lop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LopDAO {

    public boolean addLop(Lop lop) throws SQLException { // Thêm throws SQLException
        String sql = "INSERT INTO lop (ma_lop, ten_lop) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, lop.getMaLop());
            pstmt.setString(2, lop.getTenLop());

            int rowsAffected = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lop.setId(generatedKeys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } // Không có catch ở đây
    }

    public boolean updateLop(Lop lop) throws SQLException { // Thêm throws SQLException
        String sql = "UPDATE lop SET ten_lop = ? WHERE ma_lop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lop.getTenLop());
            pstmt.setString(2, lop.getMaLop());
            return pstmt.executeUpdate() > 0;
        } // Không có catch ở đây
    }

    public boolean deleteLop(String maLop) throws SQLException { // Thêm throws SQLException
        String sql = "DELETE FROM lop WHERE ma_lop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLop);
            return pstmt.executeUpdate() > 0;
        } // Không có catch ở đây
    }

    public List<Lop> getAllLops() throws SQLException { // Thêm throws SQLException
        List<Lop> lops = new ArrayList<>();
        String sql = "SELECT id, ma_lop, ten_lop, nam_hoc FROM lop";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Lop lop = new Lop();
                lop.setId(rs.getInt("id"));
                lop.setMaLop(rs.getString("ma_lop"));
                lop.setTenLop(rs.getString("ten_lop"));
                lop.setNamHoc(rs.getInt("nam_hoc"));
                lops.add(lop);
            }
        } // Không có catch ở đây
        return lops;
    }

    public List<Lop> searchLops(String keyword) throws SQLException { // Thêm throws SQLException
        List<Lop> lops = new ArrayList<>();
        String sql = "SELECT id, ma_lop, ten_lop, nam_hoc FROM lop WHERE ma_lop LIKE ? OR ten_lop LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Lop lop = new Lop();
                    lop.setId(rs.getInt("id"));
                    lop.setMaLop(rs.getString("ma_lop"));
                    lop.setTenLop(rs.getString("ten_lop"));
                    lop.setNamHoc(rs.getInt("nam_hoc"));
                    lops.add(lop);
                }
            }
        } // Không có catch ở đây
        return lops;
    }

    public Lop getLopById(String maLop) throws SQLException { // Thêm throws SQLException
        String sql = "SELECT id, ma_lop, ten_lop, nam_hoc FROM lop WHERE ma_lop = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLop);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Lop lop = new Lop();
                    lop.setId(rs.getInt("id"));
                    lop.setMaLop(rs.getString("ma_lop"));
                    lop.setTenLop(rs.getString("ten_lop"));
                    lop.setNamHoc(rs.getInt("nam_hoc"));
                    return lop;
                }
            }
        } // Không có catch ở đây
        return null;
    }
}