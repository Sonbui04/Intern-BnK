package view;

import controller.UserController; // Cần import UserController
import model.User; // Cần import User model
import util.SessionManager; // <-- THÊM DÒNG NÀY
import java.awt.event.MouseAdapter; // <-- THÊM DÒNG NÀY
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserController userController;

    public LoginFrame() {
        setTitle("Đăng nhập Hệ thống Quản lý Sinh viên");
        setSize(400, 300);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Không cho phép thay đổi kích thước cửa sổ

        // Khởi tạo UserController
        try {
            userController = new UserController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo hệ thống: " + e.getMessage() + "\nVui lòng kiểm tra kết nối CSDL.", "Lỗi Khởi Tạo", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Vô hiệu hóa nút đăng nhập nếu có lỗi nghiêm trọng
            btnLogin = new JButton("Đăng nhập");
            btnLogin.setEnabled(false);
            return; // Thoát constructor nếu không thể kết nối CSDL
        }

        // --- Giao diện Panel chính ---
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245)); // Màu nền nhẹ
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Kéo giãn theo chiều ngang

        // Tiêu đề
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(41, 128, 185)); // Màu xanh đậm
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Chiếm 2 cột
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; // Đặt lại về 1 cột

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtUsername = new JTextField(15);
        panel.add(txtUsername, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);

        // Nút Đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 0, 10); // Khoảng cách lớn hơn cho nút
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnLogin, gbc);

        // Thêm hiệu ứng hover cho nút
        btnLogin.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(30, 90, 130));
            }

         
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(41, 128, 185));
            }
        });


        // Đăng ký sự kiện cho nút Đăng nhập
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Cho phép đăng nhập bằng phím Enter
        getRootPane().setDefaultButton(btnLogin);

        add(panel, BorderLayout.CENTER);
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()); // Lấy mật khẩu từ JPasswordField

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập và mật khẩu.", "Lỗi Đăng nhập", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gọi phương thức login từ UserController
        User loggedInUser = userController.login(username, password);

        if (loggedInUser != null) {
            // Đăng nhập thành công
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào mừng, " + loggedInUser.getFullName(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
            // Lưu thông tin người dùng vào SessionManager (sẽ tạo ở bước sau)
            SessionManager.setCurrentUser(loggedInUser);

            // Đóng cửa sổ đăng nhập
            dispose();

            // Mở cửa sổ MainDashboard
            SwingUtilities.invokeLater(() -> {
                new MainDashboard().setVisible(true);
            });
        } else {
            // Thông báo lỗi đã được hiển thị trong UserController.login()
            // Hoặc bạn có thể hiển thị một thông báo chung ở đây nếu muốn
        }
    }
}