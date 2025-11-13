package controller;

import model.Student;
import service.StudentService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private StudentService service;

    public StudentController() {
        try {
            service = new StudentService();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra cấu hình hoặc driver JDBC!", "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Lỗi khởi tạo StudentController", e);
        }
    }

    public List<Student> getAllStudents() {
        try {
            return service.getAllStudents();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public boolean addStudent(Student student) {
        try {
            boolean result = service.addStudent(student);
            if (!result) {
                 JOptionPane.showMessageDialog(null, "Thêm sinh viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi thêm sinh viên: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Mã sinh viên '" + student.getId() + "' đã tồn tại. Vui lòng nhập mã khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Phương thức mới: Cập nhật sinh viên
    public boolean updateStudent(Student student) {
        try {
            boolean result = service.updateStudent(student);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Cập nhật sinh viên thất bại! (Không tìm thấy sinh viên để sửa)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi cập nhật sinh viên: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                // SQLSTATE 23000: Integrity Constraint Violation (ví dụ: trùng khóa chính)
                errorMessage = "Mã sinh viên '" + student.getId() + "' đã bị trùng với sinh viên khác.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Phương thức mới: Xóa sinh viên
    public boolean deleteStudent(String id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sinh viên " + id + " này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = service.deleteStudent(id);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Xóa sinh viên thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa sinh viên thất bại! (Không tìm thấy sinh viên hoặc có ràng buộc dữ liệu)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                // SQLSTATE 23000: Integrity Constraint Violation (ví dụ: đang là khóa ngoại trong bảng khác)
                if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                     JOptionPane.showMessageDialog(null, "Không thể xóa sinh viên này vì có dữ liệu liên quan ở bảng khác (ví dụ: bị cảnh cáo, xếp lớp).", "Lỗi ràng buộc", JOptionPane.ERROR_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(null, "Lỗi khi xóa sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        }
        return false;
    }

    public List<Student> searchStudents(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return service.getAllStudents();
            }
            return service.searchStudents(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }
    // Phương thức updateStudentLop đã có, giữ nguyên
    public boolean updateStudentLop(String studentId, Integer lopId) {
        try {
            boolean success = service.updateStudentLop(studentId, lopId);
            if (!success) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy sinh viên để cập nhật hoặc không có thay đổi.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật lớp cho sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}