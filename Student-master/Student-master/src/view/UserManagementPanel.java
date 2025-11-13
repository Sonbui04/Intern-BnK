package view;

import controller.UserController;
import model.User;
import model.Role;
import util.SessionManager; // Import SessionManager

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private UserController userController;

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField txtUsername, txtFullName, txtEmail, txtSearch;
    private JPasswordField txtPassword;
    private JComboBox<Role> cboRole;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    public UserManagementPanel() {
        // Khởi tạo UserController
        try {
            userController = new UserController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng Quản lý Người dùng: " + e.getMessage() + "\nVui lòng kiểm tra kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Vô hiệu hóa các nút nếu controller khởi tạo thất bại
            btnAdd = new JButton("Thêm"); btnUpdate = new JButton("Sửa"); btnDelete = new JButton("Xóa"); btnClear = new JButton("Xóa trắng"); btnSearch = new JButton("Tìm kiếm");
            btnAdd.setEnabled(false); btnUpdate.setEnabled(false); btnDelete.setEnabled(false); btnClear.setEnabled(false); btnSearch.setEnabled(false);
            txtUsername = new JTextField(); txtFullName = new JTextField(); txtEmail = new JTextField(); txtPassword = new JPasswordField(); txtSearch = new JTextField();
            cboRole = new JComboBox<>(); cboRole.setEnabled(false);
            return;
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tiêu đề ---
        JLabel titleLabel = new JLabel("QUẢN LÝ NGƯỜI DÙNG", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panel nhập liệu và nút chức năng ---
        JPanel inputAndControlPanel = new JPanel(new GridBagLayout());
        inputAndControlPanel.setBackground(Color.WHITE);
        inputAndControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin người dùng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Username
        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtUsername = new JTextField(20); inputAndControlPanel.add(txtUsername, gbc); row++;

        // Mật khẩu (chỉ nhập khi thêm, hoặc đổi mật khẩu)
        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtPassword = new JPasswordField(20); inputAndControlPanel.add(txtPassword, gbc); row++;

        // Họ tên đầy đủ
        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Họ tên đầy đủ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtFullName = new JTextField(20); inputAndControlPanel.add(txtFullName, gbc); row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtEmail = new JTextField(20); inputAndControlPanel.add(txtEmail, gbc); row++;

        // Vai trò
        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; cboRole = new JComboBox<>(); loadRolesIntoComboBox(); inputAndControlPanel.add(cboRole, gbc); row++;

        // Nút chức năng
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        formButtonPanel.setOpaque(false);
        btnAdd = createStyledButton("Thêm");
        btnUpdate = createStyledButton("Sửa");
        btnDelete = createStyledButton("Xóa");
        btnClear = createStyledButton("Xóa trắng");

        formButtonPanel.add(btnAdd);
        formButtonPanel.add(btnUpdate);
        formButtonPanel.add(btnDelete);
        formButtonPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        inputAndControlPanel.add(formButtonPanel, gbc);
        row++;

        add(inputAndControlPanel, BorderLayout.WEST);

        // --- Panel Bảng và Tìm kiếm ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 242, 245));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(25);
        btnSearch = createStyledButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Bảng danh sách người dùng
        String[] columnNames = {"ID", "Tên đăng nhập", "Họ tên đầy đủ", "Email", "Vai trò"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(0, 102, 204));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setSelectionBackground(new Color(173, 216, 230));
        userTable.setFillsViewportHeight(true);

        // Điều chỉnh chiều rộng cột
        userTable.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Tên đăng nhập
        userTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Họ tên đầy đủ
        userTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
        userTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Vai trò

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        // --- Đăng ký sự kiện ---
        btnAdd.addActionListener(e -> addUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchUsers());

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        // Tải dữ liệu ban đầu
        loadUsers();

        // Cập nhật trạng thái hiển thị của các nút dựa trên vai trò
        updateButtonVisibility();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 80, 160));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
        return button;
    }

    private void loadRolesIntoComboBox() {
        cboRole.removeAllItems();
        cboRole.addItem(null);
        cboRole.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("--- Chọn vai trò ---");
                } else if (value instanceof Role) {
                    setText(((Role) value).getRoleName());
                }
                return this;
            }
        });
        List<Role> roles = userController.getAllRoles();
        for (Role role : roles) {
            // Chỉ thêm ADMIN và SV vào combobox
            if ("ADMIN".equals(role.getRoleName()) || "SV".equals(role.getRoleName())) {
                cboRole.addItem(role);
            }
        }
    }


    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                user.getId(), user.getUsername(), user.getFullName(), user.getEmail(),
                (user.getRole() != null ? user.getRole().getRoleName() : "N/A")
            });
        }
    }

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        Role selectedRole = (Role) cboRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập, mật khẩu và vai trò không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userController.registerUser(username, password, fullName, email, selectedRole.getRoleName())) {
            loadUsers();
            clearForm();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần sửa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        Role selectedRole = (Role) cboRole.getSelectedItem();

        if (username.isEmpty() || selectedRole == null) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập và vai trò không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User userToUpdate = new User();
        userToUpdate.setId(id);
        userToUpdate.setUsername(username);
        userToUpdate.setFullName(fullName);
        userToUpdate.setEmail(email);
        userToUpdate.setRoleId(selectedRole.getId());
        userToUpdate.setRole(selectedRole);

        if (!password.isEmpty()) {
            userToUpdate.setPasswordHash(util.PasswordHasher.hashPassword(password));
        } else {
            // Lấy mật khẩu hash cũ nếu không nhập mật khẩu mới
            User existingUser = userController.getUserById(id); // Cần phương thức getUserById trong UserController
            if (existingUser != null) {
                userToUpdate.setPasswordHash(existingUser.getPasswordHash());
            } else {
                 JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy người dùng hiện tại để lấy mật khẩu cũ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 return;
            }
        }

        if (userController.updateUser(userToUpdate)) {
            loadUsers();
            clearForm();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xóa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        if (userController.deleteUser(id)) {
            loadUsers();
            clearForm();
        }
    }

    private void searchUsers() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();
        for (User user : users) {
            if (user.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                user.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                (user.getRole() != null && user.getRole().getRoleName().toLowerCase().contains(keyword.toLowerCase()))) {
                tableModel.addRow(new Object[]{
                    user.getId(), user.getUsername(), user.getFullName(), user.getEmail(),
                    (user.getRole() != null ? user.getRole().getRoleName() : "N/A")
                });
            }
        }
    }

    private void fillFormFromTable() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            txtUsername.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtPassword.setText("");

            txtFullName.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 3).toString());

            String roleName = tableModel.getValueAt(selectedRow, 4).toString();
            for (int i = 0; i < cboRole.getItemCount(); i++) {
                Role role = cboRole.getItemAt(i);
                if (role != null && role.getRoleName().equals(roleName)) {
                    cboRole.setSelectedItem(role);
                    break;
                }
            }
            txtUsername.setEditable(false);
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        txtEmail.setText("");
        cboRole.setSelectedIndex(0);
        txtUsername.setEditable(true);
        userTable.clearSelection();
        loadUsers();
    }

    // Phương thức mới để cập nhật trạng thái hiển thị của các nút dựa trên vai trò
    private void updateButtonVisibility() {
        boolean canEdit = SessionManager.isAdmin(); // Chỉ admin được thêm/sửa/xóa người dùng

        btnAdd.setVisible(canEdit);
        btnUpdate.setVisible(canEdit);
        btnDelete.setVisible(canEdit);

        // Các trường nhập liệu cũng chỉ cho admin chỉnh sửa
        txtUsername.setEditable(canEdit);
        txtPassword.setEditable(canEdit);
        txtFullName.setEditable(canEdit);
        txtEmail.setEditable(canEdit);
        cboRole.setEnabled(canEdit);
        btnClear.setVisible(canEdit); // Nút xóa trắng cũng chỉ cho admin
    }
}