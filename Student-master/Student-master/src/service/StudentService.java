package service;

import dao.StudentDAO;
import model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private StudentDAO dao;

    public StudentService() {
        dao = new StudentDAO();
    }

    public List<Student> getAllStudents() throws SQLException {
        return dao.findAll();
    }

    public boolean addStudent(Student student) throws SQLException { // Đổi void thành boolean
        return dao.insert(student); // Trả về kết quả của dao.insert
    }

    // Phương thức mới: Cập nhật sinh viên
    public boolean updateStudent(Student student) throws SQLException {
        return dao.update(student);
    }

    // Phương thức mới: Xóa sinh viên
    public boolean deleteStudent(String id) throws SQLException {
        return dao.delete(id);
    }

    public List<Student> searchStudents(String keyword) throws SQLException {
        return dao.search(keyword);
    }

    public boolean updateStudentLop(String studentId, Integer lopId) throws SQLException {
        return dao.updateStudentLop(studentId, lopId);
    }
}