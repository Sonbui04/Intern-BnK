package dao;

import model.CanhCao;
import model.Student;
import model.StudentWarning;
import model.Lop; // <-- THÊM IMPORT NÀY
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentWarningDAO {

    public StudentWarningDAO() {
        // Constructor của DAO đã được làm gọn
    }

    public boolean addStudentWarning(StudentWarning studentWarning) throws SQLException {
        String sql = "INSERT INTO sinhvien_canhcao (sinhvien_id, canhcao_id, ngay_canh_cao, hoc_ky, ly_do) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentWarning.getSinhVienId());
            pstmt.setInt(2, studentWarning.getCanhCaoId());
            pstmt.setDate(3, Date.valueOf(studentWarning.getNgayCanhCao()));
            pstmt.setString(4, studentWarning.getHocKy());
            pstmt.setString(5, studentWarning.getLyDo());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteStudentWarning(String sinhVienId, Integer canhCaoId, LocalDate ngayCanhCao) throws SQLException {
        String sql = "DELETE FROM sinhvien_canhcao WHERE sinhvien_id = ? AND canhcao_id = ? AND ngay_canh_cao = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sinhVienId);
            pstmt.setInt(2, canhCaoId);
            pstmt.setDate(3, Date.valueOf(ngayCanhCao));
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<StudentWarning> getAllStudentWarnings() throws SQLException {
        List<StudentWarning> warnings = new ArrayList<>();
        String sql = "SELECT sw.sinhvien_id, sw.canhcao_id, sw.ngay_canh_cao, sw.hoc_ky, sw.ly_do, " +
                     "s.hoten AS sinhvien_hoten, s.lop_id, " + // <-- THAY s.ma_lop thành s.lop_id
                     "l.ma_lop AS lop_ma_lop, l.ten_lop AS lop_ten_lop, " + // <-- THÊM CÁC CỘT TỪ BẢNG LỚP
                     "cc.ten_canh_cao, cc.mo_ta FROM sinhvien_canhcao sw " +
                     "JOIN sinhvien s ON sw.sinhvien_id = s.id " +
                     "LEFT JOIN lop l ON s.lop_id = l.id " + // <-- THÊM JOIN VỚI BẢNG LỚP
                     "JOIN canhcao cc ON sw.canhcao_id = cc.id " +
                     "ORDER BY sw.ngay_canh_cao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                StudentWarning sw = new StudentWarning();
                sw.setSinhVienId(rs.getString("sinhvien_id"));
                sw.setCanhCaoId(rs.getInt("canhcao_id"));
                sw.setNgayCanhCao(rs.getDate("ngay_canh_cao").toLocalDate());
                sw.setHocKy(rs.getString("hoc_ky"));
                sw.setLyDo(rs.getString("ly_do"));

                // Tạo đối tượng Student
                Student student = new Student();
                student.setId(rs.getString("sinhvien_id"));
                student.setName(rs.getString("sinhvien_hoten"));
                Object lopIdObj = rs.getObject("lop_id"); // Lấy lop_id từ sinhvien
                student.setLopId(lopIdObj != null ? (Integer) lopIdObj : null);
                // Tạo đối tượng Lop nếu có thông tin lop
                if (rs.getString("lop_ma_lop") != null) { // Kiểm tra nếu có mã lớp
                    Lop lop = new Lop(null, rs.getString("lop_ma_lop"), rs.getString("lop_ten_lop"), null); // ID và NamHoc của Lop có thể là null
                    student.setLop(lop); // Gán đối tượng Lop vào Student
                }

                // Tạo đối tượng CanhCao
                CanhCao canhCao = new CanhCao();
                canhCao.setId(rs.getInt("canhcao_id"));
                canhCao.setTenCanhCao(rs.getString("ten_canh_cao"));
                canhCao.setMoTa(rs.getString("mo_ta"));

                sw.setStudent(student);
                sw.setCanhCao(canhCao);
                warnings.add(sw);
            }
        }
        return warnings;
    }

    public List<StudentWarning> searchStudentWarnings(String keyword) throws SQLException {
        List<StudentWarning> warnings = new ArrayList<>();
        String sql = "SELECT sw.sinhvien_id, sw.canhcao_id, sw.ngay_canh_cao, sw.hoc_ky, sw.ly_do, " +
                     "s.hoten AS sinhvien_hoten, s.lop_id, " + // <-- THAY s.ma_lop thành s.lop_id
                     "l.ma_lop AS lop_ma_lop, l.ten_lop AS lop_ten_lop, " + // <-- THÊM CÁC CỘT TỪ BẢNG LỚP
                     "cc.ten_canh_cao, cc.mo_ta FROM sinhvien_canhcao sw " +
                     "JOIN sinhvien s ON sw.sinhvien_id = s.id " +
                     "LEFT JOIN lop l ON s.lop_id = l.id " + // <-- THÊM JOIN VỚI BẢNG LỚP
                     "JOIN canhcao cc ON sw.canhcao_id = cc.id " +
                     "WHERE s.hoten LIKE ? OR s.id LIKE ? OR cc.ten_canh_cao LIKE ? OR sw.hoc_ky LIKE ? OR sw.ly_do LIKE ? OR l.ma_lop LIKE ? OR l.ten_lop LIKE ? " + // <-- THÊM TÌM KIẾM THEO LỚP
                     "ORDER BY sw.ngay_canh_cao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);
            pstmt.setString(6, searchPattern); // Cho lop_ma_lop
            pstmt.setString(7, searchPattern); // Cho lop_ten_lop
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentWarning sw = new StudentWarning();
                    sw.setSinhVienId(rs.getString("sinhvien_id"));
                    sw.setCanhCaoId(rs.getInt("canhcao_id"));
                    sw.setNgayCanhCao(rs.getDate("ngay_canh_cao").toLocalDate());
                    sw.setHocKy(rs.getString("hoc_ky"));
                    sw.setLyDo(rs.getString("ly_do"));

                    Student student = new Student();
                    student.setId(rs.getString("sinhvien_id"));
                    student.setName(rs.getString("sinhvien_hoten"));
                    Object lopIdObj = rs.getObject("lop_id");
                    student.setLopId(lopIdObj != null ? (Integer) lopIdObj : null);
                    if (rs.getString("lop_ma_lop") != null) {
                        Lop lop = new Lop(null, rs.getString("lop_ma_lop"), rs.getString("lop_ten_lop"), null);
                        student.setLop(lop);
                    }

                    CanhCao canhCao = new CanhCao();
                    canhCao.setId(rs.getInt("canhcao_id"));
                    canhCao.setTenCanhCao(rs.getString("ten_canh_cao"));
                    canhCao.setMoTa(rs.getString("mo_ta"));

                    sw.setStudent(student);
                    sw.setCanhCao(canhCao);
                    warnings.add(sw);
                }
            }
        }
        return warnings;
    }
}