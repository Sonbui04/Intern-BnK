package dao;

import model.Student;
import model.Lop;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public StudentDAO() {
    }

    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.id, s.hoten, s.gioitinh, s.ngaysinh, s.diachi, s.sdt, s.email, s.lop_id, " +
                     "l.id AS lop_table_id, l.ma_lop, l.ten_lop, l.nam_hoc FROM sinhvien s LEFT JOIN lop l ON s.lop_id = l.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getString("id"));
                s.setName(rs.getString("hoten"));
                s.setGender(rs.getString("gioitinh"));
                s.setDob(rs.getString("ngaysinh"));
                s.setAddress(rs.getString("diachi"));
                s.setPhone(rs.getString("sdt"));
                s.setEmail(rs.getString("email"));

                Object lopIdObj = rs.getObject("lop_id");
                s.setLopId(lopIdObj != null ? (Integer) lopIdObj : null);

                if (rs.getString("ma_lop") != null) {
                    Integer lopTableId = (Integer) rs.getObject("lop_table_id");
                    Integer namHoc = (Integer) rs.getObject("nam_hoc");

                    Lop lop = new Lop(lopTableId, rs.getString("ma_lop"), rs.getString("ten_lop"), namHoc);
                    s.setLop(lop);
                } else {
                    s.setLop(null);
                }
                list.add(s);
            }
        }
        return list;
    }

    public boolean insert(Student s) throws SQLException {
        // Đảm bảo số lượng cột và tham số khớp
        String sql = "INSERT INTO sinhvien (id, hoten, gioitinh, ngaysinh, diachi, sdt, email, lop_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getId());          // Parameter 1: id
            stmt.setString(2, s.getName());         // Parameter 2: hoten
            stmt.setString(3, s.getGender());       // Parameter 3: gioitinh
            stmt.setString(4, s.getDob());          // Parameter 4: ngaysinh
            stmt.setString(5, s.getAddress());      // Parameter 5: diachi
            stmt.setString(6, s.getPhone());        // Parameter 6: sdt  <--- SỬA LỖI Ở ĐÂY
            stmt.setString(7, s.getEmail());        // Parameter 7: email <-- SỬA LỖI Ở ĐÂY
            if (s.getLopId() != null) {             // Parameter 8: lop_id
                stmt.setInt(8, s.getLopId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(Student s) throws SQLException {
        // Đảm bảo số lượng cột và tham số khớp
        String sql = "UPDATE sinhvien SET hoten=?, gioitinh=?, ngaysinh=?, diachi=?, sdt=?, email=?, lop_id=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getName());
            stmt.setString(2, s.getGender());
            stmt.setString(3, s.getDob());
            stmt.setString(4, s.getAddress());
            stmt.setString(5, s.getPhone());        // SỬA LỖI Ở ĐÂY (trước là email)
            stmt.setString(6, s.getEmail());        // SỬA LỖI Ở ĐÂY (trước là lop_id)
            if (s.getLopId() != null) {             // Tham số 7
                stmt.setInt(7, s.getLopId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setString(8, s.getId());           // Tham số 8
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtDeleteStudentWarning = null;
        PreparedStatement stmtDeleteStudent = null;
        boolean success = false;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlDeleteWarnings = "DELETE FROM sinhvien_canhcao WHERE sinhvien_id=?";
            stmtDeleteStudentWarning = conn.prepareStatement(sqlDeleteWarnings);
            stmtDeleteStudentWarning.setString(1, id);
            stmtDeleteStudentWarning.executeUpdate();

            String sqlDeleteStudent = "DELETE FROM sinhvien WHERE id=?";
            stmtDeleteStudent = conn.prepareStatement(sqlDeleteStudent);
            stmtDeleteStudent.setString(1, id);
            success = stmtDeleteStudent.executeUpdate() > 0;

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (stmtDeleteStudentWarning != null) {
                try {
                    stmtDeleteStudentWarning.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtDeleteStudent != null) {
                try {
                    stmtDeleteStudent.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }


    public boolean updateStudentLop(String studentId, Integer lopId) throws SQLException {
        String sql = "UPDATE sinhvien SET lop_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (lopId != null) {
                stmt.setInt(1, lopId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, studentId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Student> search(String keyword) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.id, s.hoten, s.gioitinh, s.ngaysinh, s.diachi, s.sdt, s.email, s.lop_id, " +
                     "l.id AS lop_table_id, l.ma_lop, l.ten_lop, l.nam_hoc FROM sinhvien s LEFT JOIN lop l ON s.lop_id = l.id " +
                     "WHERE s.id LIKE ? OR s.hoten LIKE ? OR l.ma_lop LIKE ? OR l.ten_lop LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getString("id"));
                    s.setName(rs.getString("hoten"));
                    s.setGender(rs.getString("gioitinh"));
                    s.setDob(rs.getString("ngaysinh"));
                    s.setAddress(rs.getString("diachi"));
                    s.setPhone(rs.getString("sdt"));
                    s.setEmail(rs.getString("email"));

                    Object lopIdObj = rs.getObject("lop_id");
                    s.setLopId(lopIdObj != null ? (Integer) lopIdObj : null);

                    if (rs.getString("ma_lop") != null) {
                        Integer lopTableId = (Integer) rs.getObject("lop_table_id");
                        Integer namHoc = (Integer) rs.getObject("nam_hoc");

                        Lop lop = new Lop(lopTableId, rs.getString("ma_lop"), rs.getString("ten_lop"), namHoc);
                        s.setLop(lop);
                    } else {
                        s.setLop(null);
                    }
                    list.add(s);
                }
            }
        }
        return list;
    }
}