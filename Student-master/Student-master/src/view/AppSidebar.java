package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import util.SessionManager;

public class AppSidebar extends JPanel {
    private JButton btnHome;
    private JButton btnStudents;
    private JButton btnClasses;
    private JButton btnAssignments;
    private JButton btnWarnings;
    private JButton btnUserManagement;

    public AppSidebar(ActionListener homeListener, ActionListener studentListener,
                      ActionListener classListener, ActionListener assignmentListener,
                      ActionListener warningsListener, ActionListener userManagementListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 70, 90));
        setPreferredSize(new Dimension(220, getHeight()));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(Box.createVerticalStrut(20));

        JLabel title = new JLabel("MENU", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(30));

        btnHome = createSidebarButton("Trang chủ");
        btnHome.addActionListener(homeListener);
        add(btnHome);

        btnStudents = createSidebarButton("Quản lý Sinh viên");
        btnStudents.addActionListener(studentListener);
        add(btnStudents);

        btnClasses = createSidebarButton("Quản lý Lớp");
        btnClasses.addActionListener(classListener);
        add(btnClasses);

        btnAssignments = createSidebarButton("Xếp lớp");
        btnAssignments.addActionListener(assignmentListener);
        add(btnAssignments);

        btnWarnings = createSidebarButton("Danh sách Cảnh cáo");
        btnWarnings.addActionListener(warningsListener);
        add(btnWarnings);

        btnUserManagement = createSidebarButton("Quản lý Người dùng");
        btnUserManagement.addActionListener(userManagementListener);
        add(btnUserManagement);

        add(Box.createVerticalGlue());

        // Cập nhật quyền truy cập ngay sau khi tạo sidebar
        updateAccess(SessionManager.getCurrentUser().getRole().getRoleName());
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setBackground(new Color(70, 90, 110));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(90, 110, 130));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 90, 110));
            }
        });
        return button;
    }

    // Phương thức mới để cập nhật khả năng truy cập dựa trên vai trò
    public void updateAccess(String roleName) {
        // Mặc định ẩn tất cả các nút (trừ Trang chủ, nếu bạn muốn nó luôn hiện)
        btnStudents.setVisible(false);
        btnClasses.setVisible(false);
        btnAssignments.setVisible(false);
        btnWarnings.setVisible(false);
        btnUserManagement.setVisible(false);

        // Logic hiển thị/kích hoạt dựa trên vai trò
        if (SessionManager.isAdmin()) { // Sử dụng SessionManager.isAdmin()
            btnStudents.setVisible(true);
            btnClasses.setVisible(true);
            btnAssignments.setVisible(true);
            btnWarnings.setVisible(true);
            btnUserManagement.setVisible(true);
        } else if (SessionManager.isSv()) { // Sử dụng SessionManager.isSv()
            // Sinh viên chỉ được xem thông tin sinh viên của mình
            btnStudents.setVisible(true); // Cho phép xem danh sách sinh viên
            // Các nút khác vẫn ẩn
        }
        revalidate();
        repaint();
    }
}