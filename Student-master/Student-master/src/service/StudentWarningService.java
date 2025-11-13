package service;

import dao.StudentWarningDAO;
import model.StudentWarning;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class StudentWarningService {
    private StudentWarningDAO dao;

    public StudentWarningService() {
        this.dao = new StudentWarningDAO();
    }

    public boolean addStudentWarning(StudentWarning studentWarning) throws SQLException {
        // Có thể thêm logic kiểm tra trùng lặp (nếu muốn một sinh viên chỉ bị 1 loại cảnh cáo 1 ngày)
        // Tuy nhiên, Primary Key đã xử lý trùng lặp tự động trong DB.
        return dao.addStudentWarning(studentWarning);
    }

    public boolean deleteStudentWarning(String sinhVienId, Integer canhCaoId, LocalDate ngayCanhCao) throws SQLException {
        return dao.deleteStudentWarning(sinhVienId, canhCaoId, ngayCanhCao);
    }

    public List<StudentWarning> getAllStudentWarnings() throws SQLException {
        return dao.getAllStudentWarnings();
    }

    public List<StudentWarning> searchStudentWarnings(String keyword) throws SQLException {
        return dao.searchStudentWarnings(keyword);
    }
}