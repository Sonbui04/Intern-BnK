package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; // Thêm import này
import java.awt.event.ActionListener; // Thêm import này
import util.SessionManager; // Thêm import này

public class AppHeader extends JPanel {
    private JLabel userNameLabel; // Thêm label để hiển thị tên người dùng
    private JButton btnLogout;    // Thêm nút đăng xuất

    public AppHeader() {
        setLayout(new BorderLayout());
        setBackground(new Color(41, 128, 185));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Panel chứa tiêu đề chính và phụ
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel mainTitle = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ GTVT");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("University of Transport Technology");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(Color.WHITE);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);

        add(titlePanel, BorderLayout.CENTER);

        // Panel chứa thông tin người dùng và nút đăng xuất ở phía Đông (phải)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5)); // Căn phải, có khoảng cách
        userPanel.setOpaque(false);

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userNameLabel.setForeground(Color.WHITE);
        userPanel.add(userNameLabel);

        btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBackground(new Color(230, 74, 25)); // Màu cam cho nút logout
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.add(btnLogout);
        add(userPanel, BorderLayout.EAST); // Đặt userPanel vào phía Đông của header

        // Cập nhật tên người dùng khi khởi tạo header
        updateUserInfo();

        // Đăng ký sự kiện cho nút Đăng xuất
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(AppHeader.this, "Bạn có chắc chắn muốn đăng xuất không?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    SessionManager.logout(); // Xóa thông tin phiên làm việc

                    // Đóng cửa sổ hiện tại (MainDashboard)
                    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(AppHeader.this);
                    currentFrame.dispose();

                    // Mở lại màn hình đăng nhập
                    SwingUtilities.invokeLater(() -> {
                        new LoginFrame().setVisible(true);
                    });
                }
            }
        });
    }

    // Phương thức để cập nhật thông tin người dùng (tên và vai trò)
    public void updateUserInfo() {
        if (SessionManager.isLoggedIn()) {
            userNameLabel.setText("Xin chào, " + SessionManager.getCurrentUser().getFullName() + " (" + SessionManager.getCurrentUser().getRole().getRoleName() + ")");
        } else {
            userNameLabel.setText("Chưa đăng nhập");
        }
    }
}