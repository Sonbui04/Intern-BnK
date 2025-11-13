package view;

import model.Student;
import controller.StudentController;
import model.Lop;
import controller.LopController;
import util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudentPanel extends JPanel {
    private StudentController studentController;
    private LopController lopController;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtStudentId, txtFullName, txtAddress, txtPhone, txtEmail, txtSearch;
    private JComboBox<String> cboGender;
    private JTextField txtBirthDate; // Đổi lại thành JTextField vì dob là String
    private JComboBox<Lop> cboClass;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    public StudentPanel() {
        try {
            studentController = new StudentController();
            lopController = new LopController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng Quản lý Sinh viên: " + e.getMessage() + "\nVui lòng kiểm tra kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnAdd = new JButton("Thêm"); btnUpdate = new JButton("Sửa"); btnDelete = new JButton("Xóa"); btnClear = new JButton("Xóa trắng"); btnSearch = new JButton("Tìm kiếm");
            btnAdd.setEnabled(false); btnUpdate.setEnabled(false); btnDelete.setEnabled(false); btnClear.setEnabled(false); btnSearch.setEnabled(false);
            txtStudentId = new JTextField(); txtFullName = new JTextField(); txtAddress = new JTextField(); txtPhone = new JTextField(); txtEmail = new JTextField(); txtSearch = new JTextField();
            cboGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"}); cboGender.setEnabled(false);
            txtBirthDate = new JTextField(); txtBirthDate.setEnabled(false); // Sửa ở đây
            cboClass = new JComboBox<>(); cboClass.setEnabled(false);
            return;
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("QUẢN LÝ SINH VIÊN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputAndControlPanel = new JPanel(new GridBagLayout());
        inputAndControlPanel.setBackground(Color.WHITE);
        inputAndControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin sinh viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Mã sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtStudentId = new JTextField(20); inputAndControlPanel.add(txtStudentId, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtFullName = new JTextField(20); inputAndControlPanel.add(txtFullName, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; cboGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"}); inputAndControlPanel.add(cboGender, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Ngày sinh (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtBirthDate = new JTextField(20); inputAndControlPanel.add(txtBirthDate, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtAddress = new JTextField(20); inputAndControlPanel.add(txtAddress, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtPhone = new JTextField(20); inputAndControlPanel.add(txtPhone, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtEmail = new JTextField(20); inputAndControlPanel.add(txtEmail, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Lớp:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; cboClass = new JComboBox<>(); loadClassesIntoComboBox(); inputAndControlPanel.add(cboClass, gbc); row++;


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

        String[] columnNames = {"Mã SV", "Họ tên", "Giới tính", "Ngày sinh", "Địa chỉ", "SĐT", "Email", "Lớp"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(new Color(0, 102, 204));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.setSelectionBackground(new Color(173, 216, 230));
        studentTable.setFillsViewportHeight(true);

        studentTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(80);


        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchStudents());

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        loadStudents();

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

    private void loadClassesIntoComboBox() {
        cboClass.removeAllItems();
        cboClass.addItem(null);
        cboClass.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("--- Chọn lớp ---");
                } else if (value instanceof Lop) {
                    setText(((Lop) value).getMaLop() + " - " + ((Lop) value).getTenLop());
                }
                return this;
            }
        });
        List<Lop> classes = lopController.getAllLops();
        for (Lop lop : classes) {
            cboClass.addItem(lop);
        }
    }

    public void loadStudents() { // Đổi thành public
        tableModel.setRowCount(0);
        List<Student> students = studentController.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getGender(),
                student.getDob(),
                student.getAddress(),
                student.getPhone(),
                student.getEmail(),
                (student.getLop() != null ? student.getLop().getMaLop() : "N/A")
            });
        }
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        updateButtonVisibility();
    }

    private void addStudent() {
        String id = txtStudentId.getText().trim();
        String fullName = txtFullName.getText().trim();
        String gender = (String) cboGender.getSelectedItem();
        String dobString = txtBirthDate.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        Lop selectedLop = (Lop) cboClass.getSelectedItem();
        Integer lopId = (selectedLop != null) ? selectedLop.getId() : null;


        if (id.isEmpty() || fullName.isEmpty() || gender.isEmpty() || dobString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên, họ tên, giới tính và ngày sinh không được để trống.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(id, fullName, gender, dobString, address, phone, email, lopId);
        if (studentController.addStudent(student)) {
            loadStudents();
            clearForm();
        }
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần sửa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String fullName = txtFullName.getText().trim();
        String gender = (String) cboGender.getSelectedItem();
        String dobString = txtBirthDate.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        Lop selectedLop = (Lop) cboClass.getSelectedItem();
        Integer lopId = (selectedLop != null) ? selectedLop.getId() : null;

        if (fullName.isEmpty() || gender.isEmpty() || dobString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên, giới tính và ngày sinh không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(id, fullName, gender, dobString, address, phone, email, lopId);
        if (studentController.updateStudent(student)) {
            loadStudents();
            clearForm();
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên cần xóa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        if (studentController.deleteStudent(id)) {
            loadStudents();
            clearForm();
        }
    }

    private void searchStudents() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Student> students = studentController.searchStudents(keyword);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getGender(),
                student.getDob(),
                student.getAddress(),
                student.getPhone(),
                student.getEmail(),
                (student.getLop() != null ? student.getLop().getMaLop() : "N/A")
            });
        }
        updateButtonVisibility();
    }

    private void fillFormFromTable() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            txtStudentId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtFullName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cboGender.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            txtBirthDate.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtAddress.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtPhone.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 6).toString());

            String lopMa = tableModel.getValueAt(selectedRow, 7).toString();
            for (int i = 0; i < cboClass.getItemCount(); i++) {
                Lop lop = cboClass.getItemAt(i);
                if (lop != null && lop.getMaLop().equals(lopMa)) {
                    cboClass.setSelectedItem(lop);
                    break;
                }
            }
            txtStudentId.setEditable(false);
        }
        updateButtonVisibility();
    }

    private void clearForm() {
        txtStudentId.setText("");
        txtFullName.setText("");
        cboGender.setSelectedIndex(0);
        txtBirthDate.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        cboClass.setSelectedIndex(0);
        txtStudentId.setEditable(true);
        studentTable.clearSelection();
        loadStudents();
        updateButtonVisibility();
    }

    public void refreshTable() { // Đổi thành public
        loadStudents();
    }

    private void updateButtonVisibility() {
        boolean canEdit = SessionManager.isAdmin();

        btnAdd.setVisible(canEdit);
        btnUpdate.setVisible(canEdit);
        btnDelete.setVisible(canEdit);

        txtStudentId.setEditable(canEdit);
        txtFullName.setEditable(canEdit);
        cboGender.setEnabled(canEdit);
        txtBirthDate.setEditable(canEdit);
        txtAddress.setEditable(canEdit);
        txtPhone.setEditable(canEdit);
        txtEmail.setEditable(canEdit);
        cboClass.setEnabled(canEdit);
        btnClear.setVisible(canEdit);
    }
}