package view;

import controller.LopController;
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

public class LopPanel extends JPanel {
    private LopController lopController;

    private JTable lopTable;
    private DefaultTableModel tableModel;
    private JTextField txtMaLop, txtTenLop, txtSearch;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    public LopPanel() {
        try {
            lopController = new LopController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng Quản lý Lớp: " + e.getMessage() + "\nVui lòng kiểm tra kết nối CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnAdd = new JButton("Thêm"); btnUpdate = new JButton("Sửa"); btnDelete = new JButton("Xóa"); btnClear = new JButton("Xóa trắng"); btnSearch = new JButton("Tìm kiếm");
            btnAdd.setEnabled(false); btnUpdate.setEnabled(false); btnDelete.setEnabled(false); btnClear.setEnabled(false); btnSearch.setEnabled(false);
            txtMaLop = new JTextField(); txtTenLop = new JTextField(); txtSearch = new JTextField();
            return;
        }

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("QUẢN LÝ LỚP", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputAndControlPanel = new JPanel(new GridBagLayout());
        inputAndControlPanel.setBackground(Color.WHITE);
        inputAndControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin lớp học"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Mã lớp:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtMaLop = new JTextField(20); inputAndControlPanel.add(txtMaLop, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; inputAndControlPanel.add(new JLabel("Tên lớp:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; txtTenLop = new JTextField(20); inputAndControlPanel.add(txtTenLop, gbc); row++;

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

        String[] columnNames = {"ID", "Mã lớp", "Tên lớp", "Năm học"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lopTable = new JTable(tableModel);
        lopTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lopTable.setRowHeight(25);
        lopTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        lopTable.getTableHeader().setBackground(new Color(0, 102, 204));
        lopTable.getTableHeader().setForeground(Color.WHITE);
        lopTable.setSelectionBackground(new Color(173, 216, 230));
        lopTable.setFillsViewportHeight(true);

        lopTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        lopTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        lopTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        lopTable.getColumnModel().getColumn(3).setPreferredWidth(80);


        JScrollPane scrollPane = new JScrollPane(lopTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addLop());
        btnUpdate.addActionListener(e -> updateLop());
        btnDelete.addActionListener(e -> deleteLop());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchLops());

        lopTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });

        loadLops();

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

    private void loadLops() {
        tableModel.setRowCount(0);
        List<Lop> lops = lopController.getAllLops();
        for (Lop lop : lops) {
            tableModel.addRow(new Object[]{lop.getId(), lop.getMaLop(), lop.getTenLop(), lop.getNamHoc()});
        }
    }

    private void addLop() {
        String maLop = txtMaLop.getText().trim();
        String tenLop = txtTenLop.getText().trim();

        if (maLop.isEmpty() || tenLop.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Lop lop = new Lop(null, maLop, tenLop, null);
        if (lopController.addLop(lop)) {
            loadLops();
            clearForm();
        }
    }

    private void updateLop() {
        int selectedRow = lopTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần sửa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String maLop = txtMaLop.getText().trim();
        String tenLop = txtTenLop.getText().trim();
        Integer namHoc = (Integer) tableModel.getValueAt(selectedRow, 3);

        if (maLop.isEmpty() || tenLop.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã lớp và tên lớp không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Lop lop = new Lop(id, maLop, tenLop, namHoc);
        if (lopController.updateLop(lop)) {
            loadLops();
            clearForm();
        }
    }

    private void deleteLop() {
        int selectedRow = lopTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần xóa từ bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // SỬA LỖI: Lấy mã lớp (String) và truyền vào deleteLop
        String maLop = (String) tableModel.getValueAt(selectedRow, 1); // Lấy mã lớp từ cột 1 (index 1)
        if (lopController.deleteLop(maLop)) { // <--- SỬA LỖI Ở ĐÂY
            loadLops();
            clearForm();
        }
    }

    private void searchLops() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Lop> lops = lopController.searchLops(keyword);
        for (Lop lop : lops) {
            tableModel.addRow(new Object[]{lop.getId(), lop.getMaLop(), lop.getTenLop(), lop.getNamHoc()});
        }
    }

    private void fillFormFromTable() {
        int selectedRow = lopTable.getSelectedRow();
        if (selectedRow != -1) {
            txtMaLop.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtTenLop.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtMaLop.setEditable(false);
        }
    }

    private void clearForm() {
        txtMaLop.setText("");
        txtTenLop.setText("");
        txtMaLop.setEditable(true);
        lopTable.clearSelection();
        loadLops();
    }

    private void updateButtonVisibility() {
        boolean canEdit = SessionManager.isAdmin();

        btnAdd.setVisible(canEdit);
        btnUpdate.setVisible(canEdit);
        btnDelete.setVisible(canEdit);

        txtMaLop.setEditable(canEdit);
        txtTenLop.setEditable(canEdit);
        btnClear.setVisible(canEdit);
    }
}