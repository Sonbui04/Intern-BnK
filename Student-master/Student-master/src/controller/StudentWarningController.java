package controller;

import model.CanhCao;
import model.Student;
import model.StudentWarning;
import service.StudentWarningService;
import service.StudentService; // Cần để lấy danh sách sinh viên
import service.CanhCaoService; // Cần để lấy danh sách loại cảnh cáo
import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class StudentWarningController {
    private StudentWarningService studentWarningService;
    private StudentService studentService; // Để tương tác với sinh viên
    private CanhCaoService canhCaoService; // Để tương tác với loại cảnh cáo

    public StudentWarningController() {
        try {
            studentWarningService = new StudentWarningService();
            studentService = new StudentService(); // Khởi tạo StudentService
            canhCaoService = new CanhCaoService(); // Khởi tạo CanhCaoService
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến CSDL cho chức năng Cảnh cáo Sinh viên: " + e.getMessage(), "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Lỗi khởi tạo StudentWarningController", e);
        }
    }

    public List<StudentWarning> getAllStudentWarnings() {
        try {
            return studentWarningService.getAllStudentWarnings();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách cảnh cáo sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean addStudentWarning(StudentWarning studentWarning) {
        try {
            // Kiểm tra ràng buộc thêm (ví dụ: ngày cảnh cáo không phải tương lai)
            if (studentWarning.getNgayCanhCao().isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "Ngày cảnh cáo không thể là ngày trong tương lai.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra trùng lặp Primary Key (sinhvien_id, canhcao_id, ngay_canh_cao)
            // (Việc này thường do DB tự xử lý với UNIQUE/PK constraint, nhưng có thể check trước)
            // Nếu trùng sẽ ném SQLException với SQLState 23xxx
            boolean result = studentWarningService.addStudentWarning(studentWarning);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Thêm cảnh cáo sinh viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi thêm cảnh cáo sinh viên: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Sinh viên đã bị cảnh cáo loại này vào ngày này rồi.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Ngày cảnh cáo không đúng định dạng YYYY-MM-DD.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteStudentWarning(String sinhVienId, Integer canhCaoId, LocalDate ngayCanhCao) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa cảnh cáo này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = studentWarningService.deleteStudentWarning(sinhVienId, canhCaoId, ngayCanhCao);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Xóa cảnh cáo thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa cảnh cáo thất bại! (Không tìm thấy hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa cảnh cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public List<StudentWarning> searchStudentWarnings(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return studentWarningService.getAllStudentWarnings();
            }
            return studentWarningService.searchStudentWarnings(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm cảnh cáo sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    // Các phương thức để lấy danh sách sinh viên và loại cảnh cáo cho JComboBox
    public List<Student> getAllStudents() {
        try {
            return studentService.getAllStudents();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về rỗng nếu có lỗi
        }
    }

    public List<CanhCao> getAllCanhCaos() {
        try {
            return canhCaoService.getAllCanhCaos();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về rỗng nếu có lỗi
        }
    }
}