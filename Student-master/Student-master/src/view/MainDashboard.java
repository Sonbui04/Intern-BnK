package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.SessionManager;

public class MainDashboard extends JFrame {
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private AppSidebar sidebar;
    private AppHeader header; // Giữ tham chiếu đến AppHeader

    public MainDashboard() {
        setTitle("Hệ thống quản lý sinh viên");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        header = new AppHeader(); // Khởi tạo AppHeader
        add(header, BorderLayout.NORTH);

        // Footer
        AppFooter footer = new AppFooter();
        add(footer, BorderLayout.SOUTH);

        // Main Content Panel (sử dụng CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(240, 242, 245));
        add(mainContentPanel, BorderLayout.CENTER);

        // Khởi tạo các Panel chức năng
        HomePanel homePanel;

        StudentPanel studentPanel = new StudentPanel();
        LopPanel lopPanel = new LopPanel();
        XepLopPanel xepLopPanel = new XepLopPanel();
        DanhSachCanhCaoPanel danhSachCanhCaoPanel = new DanhSachCanhCaoPanel();
        UserManagementPanel userManagementPanel = new UserManagementPanel();


        // Định nghĩa các ActionListener cho Sidebar và HomePanel
        ActionListener homeListener = e -> cardLayout.show(mainContentPanel, "HOME");
        ActionListener studentListener = e -> cardLayout.show(mainContentPanel, "STUDENTS");
        ActionListener classListener = e -> cardLayout.show(mainContentPanel, "CLASSES");
        ActionListener assignmentListener = e -> cardLayout.show(mainContentPanel, "ASSIGNMENTS");
        ActionListener warningsListener = e -> cardLayout.show(mainContentPanel, "WARNINGS");
        ActionListener userManagementListener = e -> cardLayout.show(mainContentPanel, "USER_MANAGEMENT");


        // Khởi tạo HomePanel và truyền các listener vào
        homePanel = new HomePanel(studentListener, classListener, assignmentListener, warningsListener, userManagementListener);

        // Thêm các panel vào CardLayout
        mainContentPanel.add(homePanel, "HOME");
        mainContentPanel.add(studentPanel, "STUDENTS");
        mainContentPanel.add(lopPanel, "CLASSES");
        mainContentPanel.add(xepLopPanel, "ASSIGNMENTS");
        mainContentPanel.add(danhSachCanhCaoPanel, "WARNINGS");
        mainContentPanel.add(userManagementPanel, "USER_MANAGEMENT");


        // Sidebar
        sidebar = new AppSidebar(homeListener, studentListener, classListener, assignmentListener, warningsListener, userManagementListener);
        add(sidebar, BorderLayout.WEST);

        // Mặc định hiển thị HomePanel khi khởi động
        cardLayout.show(mainContentPanel, "HOME");

        // Cập nhật thông tin người dùng trong header sau khi MainDashboard được tạo
        header.updateUserInfo();
    }

    public static void main(String[] args) {
        // Đã chuyển sang LoginFrame là điểm khởi đầu trong App.java
    }
}