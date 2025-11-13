package view;

// import controller.XepLopController; // Không cần import XepLopController nếu nó không tồn tại hoặc không dùng trực tiếp
import controller.StudentController;
import controller.LopController;
import model.Student;
import model.Lop;
import util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class XepLopPanel extends JPanel {
    // private XepLopController xepLopController; // Bỏ dòng này nếu không có XepLopController riêng
    private StudentController studentController;
    private LopController lopController;

    private JTable xepLopTable; // Tên bảng sẽ là studentTable hoặc tương tự, vì nó hiển thị sinh viên
    private DefaultTableModel tableModel;
    private JComboBox<Student> cboStudent;
    private JComboBox<Lop> cboLop;

    private JButton btnAssignClass, btnRemoveClass; // Đổi tên nút để phù hợp với chức năng xếp lớp

    public XepLopPanel() {
        try {
            // xepLopController = new XepLopController(); // Không khởi tạo XepLopController nếu không có
            studentController = new StudentController();
            lopController = new LopController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng Xếp Lớp: " + e.getMessage() + "\nVui lòng kiểm tra kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnAssignClass = new JButton("Xếp lớp"); btnRemoveClass = new JButton("Hủy xếp"); // Đổi tên nút
            btnAssignClass.setEnabled(false); btnRemoveClass.setEnabled(false);
            cboStudent = new JComboBox<>(); cboStudent.setEnabled(false);
            cboLop = new JComboBox<>(); cboLop.setEnabled(false);
            return;
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("XẾP LỚP SINH VIÊN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputAndControlPanel = new JPanel(new GridBagLayout());
        inputAndControlPanel.setBackground(Color.WHITE);
        inputAndControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Phân công lớp"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Chọn sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; cboStudent = new JComboBox<>(); loadStudentsIntoComboBox(); inputAndControlPanel.add(cboStudent, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Chọn lớp:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; cboLop = new JComboBox<>(); loadClassesIntoComboBox(); inputAndControlPanel.add(cboLop, gbc); row++;

        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        formButtonPanel.setOpaque(false);
        btnAssignClass = createStyledButton("Xếp lớp");
        btnRemoveClass = createStyledButton("Hủy xếp");

        formButtonPanel.add(btnAssignClass);
        formButtonPanel.add(btnRemoveClass);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        inputAndControlPanel.add(formButtonPanel, gbc);
        row++;

        add(inputAndControlPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 242, 245));

        String[] columnNames = {"Mã SV", "Họ tên SV", "Lớp Hiện Tại", "Mã Lớp"}; // Bỏ cột "ID Xếp Lớp" nếu không có bảng riêng
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        xepLopTable = new JTable(tableModel); // Đổi tên biến cho rõ ràng (trước là xepLopTable)
        xepLopTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        xepLopTable.setRowHeight(25);
        xepLopTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        xepLopTable.getTableHeader().setBackground(new Color(0, 102, 204));
        xepLopTable.getTableHeader().setForeground(Color.WHITE);
        xepLopTable.setSelectionBackground(new Color(173, 216, 230));
        xepLopTable.setFillsViewportHeight(true);

        xepLopTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã SV
        xepLopTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Họ tên SV
        xepLopTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Lớp Hiện Tại
        xepLopTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Mã Lớp


        JScrollPane scrollPane = new JScrollPane(xepLopTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        btnAssignClass.addActionListener(e -> assignClassToStudent());
        btnRemoveClass.addActionListener(e -> removeClassFromStudent());

        xepLopTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        loadStudentsForXepLop(); // Tải danh sách sinh viên vào bảng này

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

    private void loadStudentsIntoComboBox() {
        cboStudent.removeAllItems();
        cboStudent.addItem(null);
        cboStudent.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("--- Chọn sinh viên ---");
                } else if (value instanceof Student) {
                    setText(((Student) value).getName() + " (" + ((Student) value).getId() + ")"); // Dùng getName()
                }
                return this;
            }
        });
        List<Student> students = studentController.getAllStudents();
        for (Student student : students) {
            cboStudent.addItem(student);
        }
    }

    private void loadClassesIntoComboBox() {
        cboLop.removeAllItems();
        cboLop.addItem(null);
        cboLop.setRenderer(new DefaultListCellRenderer() {
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
        List<Lop> lops = lopController.getAllLops();
        for (Lop lop : lops) {
            cboLop.addItem(lop);
        }
    }

    private void loadStudentsForXepLop() { // Đổi tên phương thức để phản ánh nội dung bảng
        tableModel.setRowCount(0);
        List<Student> students = studentController.getAllStudents(); // Lấy tất cả sinh viên
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(), // Họ tên SV
                (student.getLop() != null ? student.getLop().getTenLop() : "Chưa có lớp"), // Lớp Hiện Tại (tên lớp)
                (student.getLop() != null ? student.getLop().getMaLop() : "N/A") // Mã Lớp
            });
        }
    }

    private void assignClassToStudent() {
        Student selectedStudent = (Student) cboStudent.getSelectedItem();
        Lop selectedLop = (Lop) cboLop.getSelectedItem();

        if (selectedStudent == null || selectedLop == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên và lớp!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Logic xếp lớp thực chất là cập nhật lop_id của sinh viên
        if (studentController.updateStudentLop(selectedStudent.getId(), selectedLop.getId())) {
            JOptionPane.showMessageDialog(this, "Xếp lớp thành công cho sinh viên " + selectedStudent.getId() + "!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadStudentsForXepLop(); // Tải lại bảng để thấy sự thay đổi
            clearForm(); // Xóa lựa chọn và form
        } else {
            // Lỗi đã được xử lý và hiển thị trong controller.
        }
    }

    private void removeClassFromStudent() {
        Student selectedStudent = (Student) cboStudent.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên từ bảng hoặc ComboBox để hủy xếp lớp.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy xếp lớp cho sinh viên " + selectedStudent.getId() + " không?", "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (studentController.updateStudentLop(selectedStudent.getId(), null)) { // Gán lop_id là NULL
                JOptionPane.showMessageDialog(this, "Hủy xếp lớp thành công cho sinh viên " + selectedStudent.getId() + "!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadStudentsForXepLop();
                clearForm();
            } else {
                // Lỗi đã được xử lý trong controller.
            }
        }
    }

    private void clearForm() {
        cboStudent.setSelectedIndex(0);
        cboLop.setSelectedIndex(0);
        xepLopTable.clearSelection();
        loadStudentsForXepLop(); // Tải lại toàn bộ danh sách
    }

    private void fillFormFromTable() {
        int selectedRow = xepLopTable.getSelectedRow();
        if (selectedRow != -1) {
            String studentId = tableModel.getValueAt(selectedRow, 0).toString(); // Mã SV là cột 0
            String lopMa = tableModel.getValueAt(selectedRow, 3).toString(); // Mã Lớp là cột 3

            for (int i = 0; i < cboStudent.getItemCount(); i++) {
                Student student = cboStudent.getItemAt(i);
                if (student != null && student.getId().equals(studentId)) {
                    cboStudent.setSelectedItem(student);
                    break;
                }
            }

            for (int i = 0; i < cboLop.getItemCount(); i++) {
                Lop lop = cboLop.getItemAt(i);
                if (lop != null && lop.getMaLop().equals(lopMa)) {
                    cboLop.setSelectedItem(lop);
                    break;
                }
            }
        }
    }

    private void updateButtonVisibility() {
        boolean canEdit = SessionManager.isAdmin();

        btnAssignClass.setVisible(canEdit);
        btnRemoveClass.setVisible(canEdit);

        cboStudent.setEnabled(canEdit);
        cboLop.setEnabled(canEdit);
        // Nút Xóa trắng cũng chỉ cho admin, nhưng XepLopPanel không có nút clear riêng
        // btnClear.setVisible(canEdit); // Nếu có nút Clear form

    }
}