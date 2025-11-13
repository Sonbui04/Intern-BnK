package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException; // Import này cần cho SimpleDateFormat
import java.text.SimpleDateFormat; // Import này cần cho SimpleDateFormat
import java.util.Date; // Import này cần cho Date
import java.util.List;

import model.CanhCao;
import model.Student; // THÊM IMPORT NÀY
import controller.CanhCaoController;
import controller.StudentController; // THÊM IMPORT NÀY

public class DanhSachCanhCaoPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JPanel canhCaoTypeManagementPanel;
    private StudentWarningManagementPanel studentWarningManagementPanel;

    public DanhSachCanhCaoPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        canhCaoTypeManagementPanel = createCanhCaoTypeManagementPanel();
        tabbedPane.addTab("Quản lý Loại Cảnh Cáo", canhCaoTypeManagementPanel);

        studentWarningManagementPanel = new StudentWarningManagementPanel();
        tabbedPane.addTab("Sinh viên Bị Cảnh Cáo", studentWarningManagementPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCanhCaoTypeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtTenCanhCao = new JTextField(20);
        JTextField txtMoTa = new JTextField(20);
        JTextField txtSearch = new JTextField(25);

        JButton btnAdd = createStyledButton("Thêm loại");
        JButton btnUpdate = createStyledButton("Sửa loại");
        JButton btnDelete = createStyledButton("Xóa loại");
        JButton btnClear = createStyledButton("Xóa trắng form");
        JButton btnSearch = createStyledButton("Tìm kiếm");

        CanhCaoController canhCaoControllerLocal;
        try {
            canhCaoControllerLocal = new CanhCaoController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(panel, "Lỗi khởi tạo Controller cho Loại Cảnh Cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            canhCaoControllerLocal = null;
        }
        final CanhCaoController finalCanhCaoController = canhCaoControllerLocal;

        String[] columnNames = {"ID", "Tên loại cảnh cáo", "Mô tả"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable canhCaoTable = new JTable(tableModel);
        canhCaoTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        canhCaoTable.setRowHeight(25);
        canhCaoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        canhCaoTable.getTableHeader().setBackground(new Color(0, 102, 204));
        canhCaoTable.getTableHeader().setForeground(Color.WHITE);
        canhCaoTable.setSelectionBackground(new Color(173, 216, 230));
        canhCaoTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(canhCaoTable);


        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin loại cảnh cáo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Tên loại cảnh cáo:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(txtTenCanhCao, gbc); row++;
        gbc.gridx = 0; gbc.gridy = row; inputPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; inputPanel.add(txtMoTa, gbc); row++;

        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        formButtonPanel.setOpaque(false);
        formButtonPanel.add(btnAdd);
        formButtonPanel.add(btnUpdate);
        formButtonPanel.add(btnDelete);
        formButtonPanel.add(btnClear);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; inputPanel.add(formButtonPanel, gbc); row++;
        panel.add(inputPanel, BorderLayout.WEST);


        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 242, 245));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.CENTER);


        final Runnable refreshTableRunnable = () -> {
            if (finalCanhCaoController != null) {
                tableModel.setRowCount(0);
                List<CanhCao> canhCaos = finalCanhCaoController.getAllCanhCaos(); // Lỗi getAllCanhCaosWithDetails đã sửa
                for (CanhCao cc : canhCaos) {
                    tableModel.addRow(new Object[]{cc.getId(), cc.getTenCanhCao(), cc.getMoTa()});
                }
            }
        };

        refreshTableRunnable.run();


        btnAdd.addActionListener(e -> {
            CanhCaoForm form = new CanhCaoForm(panel, refreshTableRunnable, null); // Truyền panel làm Component
            form.setVisible(true);
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = canhCaoTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn một loại cảnh cáo để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String tenCanhCao = (String) tableModel.getValueAt(selectedRow, 1);
            String moTa = (String) tableModel.getValueAt(selectedRow, 2);

            CanhCao canhCaoToEdit = new CanhCao(id, tenCanhCao, moTa); // SỬA CONSTRUCTOR Ở ĐÂY
            CanhCaoForm form = new CanhCaoForm(panel, refreshTableRunnable, canhCaoToEdit);
            form.setVisible(true);
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = canhCaoTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn một loại cảnh cáo để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (finalCanhCaoController != null && finalCanhCaoController.deleteCanhCao(id)) {
                refreshTableRunnable.run();
            }
        });

        btnClear.addActionListener(e -> {
            txtTenCanhCao.setText("");
            txtMoTa.setText("");
            txtSearch.setText("");
            canhCaoTable.clearSelection();
            refreshTableRunnable.run();
        });

        btnSearch.addActionListener(e -> {
            if (finalCanhCaoController != null) {
                tableModel.setRowCount(0);
                List<CanhCao> searchResult = finalCanhCaoController.searchCanhCaos(txtSearch.getText().trim());
                for (CanhCao cc : searchResult) {
                    tableModel.addRow(new Object[]{cc.getId(), cc.getTenCanhCao(), cc.getMoTa()});
                }
            }
        });

        canhCaoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = canhCaoTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtTenCanhCao.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtMoTa.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });

        return panel;
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
}