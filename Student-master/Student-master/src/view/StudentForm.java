package view;

import model.Student;
import controller.StudentController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StudentForm extends JDialog {
    private StudentPanel parentPanel; // Tham chiếu đến StudentPanel để làm mới bảng
    private JTextField txtId, txtName, txtDob, txtAddress, txtPhone, txtEmail;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
    private JButton btnSave, btnCancel;
    private StudentController studentController;
    private Student studentToEdit; // Đối tượng Student nếu đang ở chế độ sửa

    // Constructor cho cả chế độ thêm và sửa
    // studentToEdit là null nếu thêm mới, là đối tượng Student nếu sửa
    public StudentForm(StudentPanel parent, Student studentToEdit) {
        super((JFrame) SwingUtilities.getWindowAncestor(parent), studentToEdit == null ? "Thêm sinh viên mới" : "Sửa thông tin sinh viên", true);
        this.parentPanel = parent;
        this.studentToEdit = studentToEdit;
        setSize(400, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Khởi tạo controller và xử lý lỗi kết nối
        try {
            studentController = new StudentController();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo chức năng thêm/sửa sinh viên do lỗi CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Header cho form
        JLabel formTitle = new JLabel(studentToEdit == null ? "NHẬP THÔNG TIN SINH VIÊN MỚI" : "SỬA THÔNG TIN SINH VIÊN", JLabel.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);
        row++;
        gbc.gridwidth = 1;

        // ID
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Mã sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtId = new JTextField(20);
        formPanel.add(txtId, gbc);
        row++;

        // Họ tên
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);
        row++;

        // Giới tính
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setOpaque(false);
        rbMale = new JRadioButton("Nam");
        rbFemale = new JRadioButton("Nữ");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);
        rbMale.setSelected(true);
        formPanel.add(genderPanel, gbc);
        row++;

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Ngày sinh (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtDob = new JTextField(20);
        formPanel.add(txtDob, gbc);
        row++;

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtAddress = new JTextField(20);
        formPanel.add(txtAddress, gbc);
        row++;

        // SĐT
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);
        row++;

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // Nếu ở chế độ sửa, điền dữ liệu cũ vào form và khóa trường ID
        if (studentToEdit != null) {
            txtId.setText(studentToEdit.getId());
            txtId.setEditable(false); // Không cho phép sửa ID khi update
            txtName.setText(studentToEdit.getName());
            if ("Nam".equals(studentToEdit.getGender())) {
                rbMale.setSelected(true);
            } else if ("Nữ".equals(studentToEdit.getGender())) {
                rbFemale.setSelected(true);
            }
            txtDob.setText(studentToEdit.getDob());
            txtAddress.setText(studentToEdit.getAddress());
            txtPhone.setText(studentToEdit.getPhone());
            txtEmail.setText(studentToEdit.getEmail());
        }

        // Listeners
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void saveStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String gender = rbMale.isSelected() ? "Nam" : "Nữ";
        String dob = txtDob.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        // Kiểm tra dữ liệu đầu vào cơ bản (có thể mở rộng thêm)
        if (id.isEmpty() || name.isEmpty() || dob.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên, Họ tên và Ngày sinh không được để trống.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student currentStudent = (studentToEdit == null) ? new Student() : studentToEdit;
        currentStudent.setId(id);
        currentStudent.setName(name);
        currentStudent.setGender(gender);
        currentStudent.setDob(dob);
        currentStudent.setAddress(address);
        currentStudent.setPhone(phone);
        currentStudent.setEmail(email);
        currentStudent.setLopId(null); // Tạm thời đặt là null, cần logic để chọn lớp nếu cần

        boolean success = false;
        if (studentToEdit == null) { // Chế độ thêm mới
            success = studentController.addStudent(currentStudent);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            }
        } else { // Chế độ sửa
            success = studentController.updateStudent(currentStudent);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
            }
        }

        if (success) {
            parentPanel.refreshTable(); // Gọi phương thức refreshTable() đã được khai báo public trong StudentPanel
            dispose(); // Đóng form sau khi lưu
        } else {
            // Lỗi đã được xử lý trong controller và hiển thị dialog
        }
    }
}