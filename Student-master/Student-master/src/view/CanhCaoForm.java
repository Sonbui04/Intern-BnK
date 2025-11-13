package view;

import model.CanhCao;
import controller.CanhCaoController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CanhCaoForm extends JDialog {
    private Runnable refreshCallback;
    private JTextField txtTenCanhCao, txtMoTa;
    private JButton btnSave, btnCancel;
    private CanhCaoController canhCaoController;
    private CanhCao canhCaoToEdit;

    // Constructor cho chế độ thêm mới (nhận một Component cha và Runnable để làm mới bảng)
    // Đây là constructor chính
    public CanhCaoForm(Component parentComponent, Runnable refreshCallback, CanhCao canhCaoToEdit) { // <-- THAY ĐỔI CONSTRUCTOR
        super((JFrame) SwingUtilities.getWindowAncestor(parentComponent), // <-- Dùng parentComponent ở đây
              canhCaoToEdit == null ? "Thêm loại cảnh cáo mới" : "Sửa thông tin loại cảnh cáo", true);
        this.refreshCallback = refreshCallback;
        this.canhCaoToEdit = canhCaoToEdit;
        setSize(380, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            canhCaoController = new CanhCaoController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Không thể khởi tạo chức năng loại cảnh cáo do lỗi CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnSave = new JButton("Lưu");
            btnSave.setEnabled(false);
            btnCancel = new JButton("Hủy");
            add(new JPanel(), BorderLayout.CENTER);
            JPanel tempButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            tempButtonPanel.add(btnSave);
            tempButtonPanel.add(btnCancel);
            add(tempButtonPanel, BorderLayout.SOUTH);
            return;
        }

        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel formTitle = new JLabel(canhCaoToEdit == null ? "NHẬP LOẠI CẢNH CÁO MỚI" : "SỬA LOẠI CẢNH CÁO", JLabel.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);
        row++;
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tên loại cảnh cáo:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtTenCanhCao = new JTextField(20);
        formPanel.add(txtTenCanhCao, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtMoTa = new JTextField(20);
        formPanel.add(txtMoTa, gbc);
        row++;

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        if (canhCaoToEdit != null) {
            txtTenCanhCao.setText(canhCaoToEdit.getTenCanhCao());
            txtMoTa.setText(canhCaoToEdit.getMoTa());
        }

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCanhCao();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void saveCanhCao() {
        String tenCanhCao = txtTenCanhCao.getText().trim();
        String moTa = txtMoTa.getText().trim();

        if (tenCanhCao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên loại cảnh cáo không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CanhCao canhCao = (canhCaoToEdit == null) ? new CanhCao() : canhCaoToEdit;
        canhCao.setTenCanhCao(tenCanhCao);
        canhCao.setMoTa(moTa);

        boolean success = false;
        if (canhCaoToEdit == null) {
            success = canhCaoController.addCanhCao(canhCao);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm loại cảnh cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            success = canhCaoController.updateCanhCao(canhCao);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật loại cảnh cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (success) {
            if (refreshCallback != null) {
                refreshCallback.run(); // Gọi callback để làm mới bảng
            }
            dispose();
        }
    }
}