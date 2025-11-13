package controller;

import model.User;
import model.Role;
import service.UserService;
import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private UserService userService;

    public UserController() {
        try {
            userService = new UserService();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến CSDL cho chức năng Người dùng: " + e.getMessage(), "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Lỗi khởi tạo UserController", e);
        }
    }

    public User login(String username, String password) {
        try {
            User user = userService.authenticate(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi Đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi CSDL khi đăng nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean registerUser(String username, String plainPassword, String fullName, String email, String roleName) {
        try {
            boolean success = userService.registerUser(username, plainPassword, fullName, email, roleName);
            if (success) {
                JOptionPane.showMessageDialog(null, "Đăng ký người dùng thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Đăng ký người dùng thất bại! (Có thể tên đăng nhập đã tồn tại)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi CSDL khi đăng ký người dùng: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Tên đăng nhập '" + username + "' đã tồn tại.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            boolean result = userService.updateUser(user);
            if (!result) {
                JOptionPane.showMessageDialog(null, "Cập nhật người dùng thất bại! (Không tìm thấy hoặc lỗi khác)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMessage = "Lỗi CSDL khi cập nhật người dùng: " + e.getMessage();
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                errorMessage = "Tên đăng nhập '" + user.getUsername() + "' đã tồn tại.";
            }
            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteUser(int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa người dùng này không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = userService.deleteUser(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Xóa người dùng thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa người dùng thất bại! (Không tìm thấy hoặc có ràng buộc dữ liệu)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                return success;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi CSDL khi xóa người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public List<Role> getAllRoles() {
        try {
            return userService.getAllRoles();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách vai trò: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    // THÊM PHƯƠNG THỨC NÀY
    public User getUserById(int id) {
        try {
            return userService.getUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy thông tin người dùng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}