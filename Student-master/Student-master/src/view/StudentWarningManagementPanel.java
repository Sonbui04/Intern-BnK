package view;

import model.CanhCao;
import model.Student;
import model.StudentWarning;
import controller.StudentWarningController;
import controller.StudentController;
import controller.CanhCaoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class StudentWarningManagementPanel extends JPanel {
    private StudentWarningController studentWarningController;
    private StudentController studentController;
    private CanhCaoController canhCaoController;

    private JTable warningTable;
    private DefaultTableModel tableModel;
    private JTextField txtNgayCanhCao, txtHocKy, txtLyDo, txtSearch;
    private JComboBox<Student> cboStudent;
    private JComboBox<CanhCao> cboCanhCaoType;
    private JButton btnAdd, btnDelete, btnSearch, btnClearForm;

    public StudentWarningManagementPanel() {
        // Khởi tạo Controllers
        try {
            studentWarningController = new StudentWarningController();
            studentController = new StudentController();
            canhCaoController = new CanhCaoController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng quản lý cảnh cáo sinh viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnAdd = new JButton("Thêm"); btnDelete = new JButton("Xóa"); btnSearch = new JButton("Tìm kiếm"); btnClearForm = new JButton("Xóa trắng");
            cboStudent = new JComboBox<>(); cboCanhCaoType = new JComboBox<>();
            txtNgayCanhCao = new JTextField(); txtHocKy = new JTextField(); txtLyDo = new JTextField(); txtSearch = new JTextField();

            btnAdd.setEnabled(false); btnDelete.setEnabled(false); btnSearch.setEnabled(false); btnClearForm.setEnabled(false);
            cboStudent.setEnabled(false); cboCanhCaoType.setEnabled(false);
            txtNgayCanhCao.setEnabled(false); txtHocKy.setEnabled(false); txtLyDo.setEnabled(false); txtSearch.setEnabled(false);
            return;
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Tiêu đề ---
        JLabel titleLabel = new JLabel("DANH SÁCH SINH VIÊN BỊ CẢNH CÁO", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panel nhập liệu và nút chức năng (Thêm) ---
        JPanel inputAndControlPanel = new JPanel(new GridBagLayout());
        inputAndControlPanel.setBackground(Color.WHITE);
        inputAndControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thêm/Sửa cảnh cáo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Chọn Sinh viên
        gbc.gridx = 0; gbc.gridy = row;
        inputAndControlPanel.add(new JLabel("Sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        cboStudent = new JComboBox<>();
        loadStudentsIntoComboBox();
        inputAndControlPanel.add(cboStudent, gbc);
        row++;

        // Chọn loại Cảnh cáo
        gbc.gridx = 0; gbc.gridy = row;
        inputAndControlPanel.add(new JLabel("Loại cảnh cáo:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        cboCanhCaoType = new JComboBox<>();
        loadCanhCaoTypesIntoComboBox();
        inputAndControlPanel.add(cboCanhCaoType, gbc);
        row++;

        // Ngày cảnh cáo
        gbc.gridx = 0; gbc.gridy = row;
        inputAndControlPanel.add(new JLabel("Ngày cảnh cáo (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtNgayCanhCao = new JTextField(LocalDate.now().toString(), 15);
        inputAndControlPanel.add(txtNgayCanhCao, gbc);
        row++;

        // Học kỳ
        gbc.gridx = 0; gbc.gridy = row;
        inputAndControlPanel.add(new JLabel("Học kỳ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtHocKy = new JTextField(15);
        inputAndControlPanel.add(txtHocKy, gbc);
        row++;

        // Lý do
        gbc.gridx = 0; gbc.gridy = row;
        inputAndControlPanel.add(new JLabel("Lý do:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtLyDo = new JTextField(30);
        inputAndControlPanel.add(txtLyDo, gbc);
        row++;

        // Nút Thêm và Xóa trắng
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        formButtonPanel.setOpaque(false);
        btnAdd = createStyledButton("Thêm cảnh cáo");
        btnClearForm = createStyledButton("Xóa trắng form");
        formButtonPanel.add(btnAdd);
        formButtonPanel.add(btnClearForm);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        inputAndControlPanel.add(formButtonPanel, gbc);
        row++;

        add(inputAndControlPanel, BorderLayout.WEST);

        // --- Panel Bảng và Tìm kiếm ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 242, 245));

        // Panel Tìm kiếm cho bảng
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(25);
        btnSearch = createStyledButton("Tìm kiếm");
        searchPanel.add(new JLabel("Tìm kiếm (SV/Loại/Học kỳ/Lý do):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Bảng danh sách cảnh cáo sinh viên
        String[] columnNames = {"STT", "Mã SV", "Họ Tên SV", "Loại Cảnh cáo", "Ngày Cảnh cáo", "Học kỳ", "Lý do"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        warningTable = new JTable(tableModel);
        warningTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        warningTable.setRowHeight(25);
        warningTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        warningTable.getTableHeader().setBackground(new Color(0, 102, 204));
        warningTable.getTableHeader().setForeground(Color.WHITE);
        warningTable.setSelectionBackground(new Color(173, 216, 230));
        warningTable.setFillsViewportHeight(true);

        TableColumnModel columnModel = warningTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(250);

        warningTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane scrollPane = new JScrollPane(warningTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Nút Xóa (cho bảng)
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        tableButtonPanel.setOpaque(false);
        btnDelete = createStyledButton("Xóa cảnh cáo đã chọn");
        tableButtonPanel.add(btnDelete);
        rightPanel.add(tableButtonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // --- Đăng ký sự kiện ---
        btnAdd.addActionListener(e -> addStudentWarning());
        btnDelete.addActionListener(e -> deleteStudentWarning());
        btnSearch.addActionListener(e -> searchStudentWarnings());
        btnClearForm.addActionListener(e -> clearForm());

        warningTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        // Tải dữ liệu ban đầu
        loadStudentWarnings();
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
                    setText(((Student) value).getId() + " - " + ((Student) value).getName());
                }
                return this;
            }
        });
        List<Student> students = studentController.getAllStudents();
        for (Student s : students) {
            cboStudent.addItem(s);
        }
    }

    private void loadCanhCaoTypesIntoComboBox() {
        cboCanhCaoType.removeAllItems();
        cboCanhCaoType.addItem(null);
        cboCanhCaoType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("--- Chọn loại cảnh cáo ---");
                } else if (value instanceof CanhCao) {
                    setText(((CanhCao) value).getTenCanhCao());
                }
                return this;
            }
        });
        List<CanhCao> canhCaos = canhCaoController.getAllCanhCaos();
        for (CanhCao cc : canhCaos) {
            cboCanhCaoType.addItem(cc);
        }
    }

    private void loadStudentWarnings() {
        tableModel.setRowCount(0);
        List<StudentWarning> warnings = studentWarningController.getAllStudentWarnings();
        int stt = 1;
        for (StudentWarning sw : warnings) {
            String studentName = (sw.getStudent() != null) ? sw.getStudent().getName() : "N/A";
            String studentId = (sw.getStudent() != null) ? sw.getStudent().getId() : "N/A";
            String canhCaoTypeName = (sw.getCanhCao() != null) ? sw.getCanhCao().getTenCanhCao() : "N/A";

            tableModel.addRow(new Object[]{
                stt++, studentId, studentName, canhCaoTypeName,
                sw.getNgayCanhCao(), sw.getHocKy(), sw.getLyDo()
            });
        }
    }

    private void searchStudentWarnings() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<StudentWarning> warnings = studentWarningController.searchStudentWarnings(keyword);
        int stt = 1;
        for (StudentWarning sw : warnings) {
            String studentName = (sw.getStudent() != null) ? sw.getStudent().getName() : "N/A";
            String studentId = (sw.getStudent() != null) ? sw.getStudent().getId() : "N/A";
            String canhCaoTypeName = (sw.getCanhCao() != null) ? sw.getCanhCao().getTenCanhCao() : "N/A";
            tableModel.addRow(new Object[]{
                stt++, studentId, studentName, canhCaoTypeName,
                sw.getNgayCanhCao(), sw.getHocKy(), sw.getLyDo()
            });
        }
    }

    private void addStudentWarning() {
        Student selectedStudent = (Student) cboStudent.getSelectedItem();
        CanhCao selectedCanhCaoType = (CanhCao) cboCanhCaoType.getSelectedItem();
        String ngayCanhCaoStr = txtNgayCanhCao.getText().trim();
        String hocKy = txtHocKy.getText().trim();
        String lyDo = txtLyDo.getText().trim();

        if (selectedStudent == null || selectedCanhCaoType == null || ngayCanhCaoStr.isEmpty() || hocKy.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Sinh viên, Loại cảnh cáo, nhập Ngày cảnh cáo và Học kỳ.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ngayCanhCao;
        try {
            ngayCanhCao = LocalDate.parse(ngayCanhCaoStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày cảnh cáo không đúng định dạng YYYY-MM-DD.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE); // DÒNG LỖI CŨ
            return;
        }

        StudentWarning newWarning = new StudentWarning(
            selectedStudent.getId(),
            selectedCanhCaoType.getId(),
            ngayCanhCao,
            hocKy,
            lyDo
        );

        if (studentWarningController.addStudentWarning(newWarning)) {
            JOptionPane.showMessageDialog(this, "Thêm cảnh cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadStudentWarnings();
            clearForm();
        } else {
            // Lỗi đã được xử lý và hiển thị trong controller
        }
    }

    private void deleteStudentWarning() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cảnh cáo trong bảng để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sinhVienId = (String) tableModel.getValueAt(selectedRow, 1);
        String tenLoaiCanhCao = (String) tableModel.getValueAt(selectedRow, 3);
        String ngayCanhCaoStr = tableModel.getValueAt(selectedRow, 4).toString();

        Integer canhCaoId = null;
        for(int i = 0; i < cboCanhCaoType.getItemCount(); i++) {
            CanhCao cc = cboCanhCaoType.getItemAt(i);
            if(cc != null && cc.getTenCanhCao() != null && cc.getTenCanhCao().equals(tenLoaiCanhCao)) {
                canhCaoId = cc.getId();
                break;
            }
        }

        if (canhCaoId == null) {
            JOptionPane.showMessageDialog(this, "Không thể xác định loại cảnh cáo để xóa. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ngayCanhCao;
        try {
            ngayCanhCao = LocalDate.parse(ngayCanhCaoStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc ngày cảnh cáo. Không thể xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (studentWarningController.deleteStudentWarning(sinhVienId, canhCaoId, ngayCanhCao)) {
            loadStudentWarnings();
            clearForm();
        } else {
            // Lỗi đã được xử lý trong controller.
        }
    }

    private void fillFormFromTable() {
        int selectedRow = warningTable.getSelectedRow();
        if (selectedRow != -1) {
            String studentId = (String) tableModel.getValueAt(selectedRow, 1);
            String canhCaoTypeName = (String) tableModel.getValueAt(selectedRow, 3);
            String ngayCanhCaoStr = tableModel.getValueAt(selectedRow, 4).toString();
            String hocKy = (String) tableModel.getValueAt(selectedRow, 5);
            String lyDo = (String) tableModel.getValueAt(selectedRow, 6);

            for (int i = 0; i < cboStudent.getItemCount(); i++) {
                Student s = cboStudent.getItemAt(i);
                if (s != null && s.getId() != null && s.getId().equals(studentId)) {
                    cboStudent.setSelectedItem(s);
                    break;
                }
            }

            for (int i = 0; i < cboCanhCaoType.getItemCount(); i++) {
                CanhCao cc = cboCanhCaoType.getItemAt(i);
                if (cc != null && cc.getTenCanhCao() != null && cc.getTenCanhCao().equals(canhCaoTypeName)) {
                    cboCanhCaoType.setSelectedItem(cc);
                    break;
                }
            }

            txtNgayCanhCao.setText(ngayCanhCaoStr);
            txtHocKy.setText(hocKy);
            txtLyDo.setText(lyDo);
        }
    }

    private void clearForm() {
        cboStudent.setSelectedIndex(0);
        cboCanhCaoType.setSelectedIndex(0);
        txtNgayCanhCao.setText(LocalDate.now().toString());
        txtHocKy.setText("");
        txtLyDo.setText("");
        txtSearch.setText("");
        warningTable.clearSelection();
        loadStudentWarnings();
    }
}