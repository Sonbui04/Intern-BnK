package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePanel extends JPanel {

    public HomePanel(ActionListener studentListener, ActionListener classListener,
                     ActionListener assignmentListener, ActionListener warningsListener,
                     ActionListener userManagementListener) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ SINH VIÊN", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(30, 30, 30));
        add(welcomeLabel, BorderLayout.NORTH);

        // GridLayout cho 5 thẻ (3 hàng x 2 cột) để có chỗ cho thẻ Quản lý Người dùng
        JPanel cardsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        cardsPanel.setOpaque(false);
        add(cardsPanel, BorderLayout.CENTER);

        cardsPanel.add(createFeatureCard("Quản lý Sinh viên", "Thêm, sửa, xóa, tìm kiếm thông tin sinh viên.", studentListener));
        cardsPanel.add(createFeatureCard("Quản lý Lớp", "Thêm, sửa, xóa, tìm kiếm thông tin lớp học.", classListener));
        cardsPanel.add(createFeatureCard("Xếp lớp", "Phân công sinh viên vào các lớp học.", assignmentListener));
        cardsPanel.add(createFeatureCard("Danh sách Cảnh cáo", "Quản lý danh sách sinh viên bị cảnh cáo.", warningsListener));
        cardsPanel.add(createFeatureCard("Quản lý Người dùng", "Thêm, sửa, xóa người dùng và phân quyền.", userManagementListener));


        add(Box.createVerticalGlue(), BorderLayout.WEST);
        add(Box.createVerticalGlue(), BorderLayout.EAST);
        add(Box.createVerticalGlue(), BorderLayout.SOUTH);
    }

    private JPanel createFeatureCard(String title, String description, ActionListener listener) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // BỎ CÁC DÒNG card.addActionListener() Ở ĐÂY
        // if (listener != null) {
        //     card.addActionListener(e -> listener.actionPerformed(e));
        // } else {
        //     card.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ch?c n?ng: " + title + " ?ang ???c phát tri?n."));
        // }


        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        card.add(titleLabel, BorderLayout.NORTH);

        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        card.add(descArea, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                // SỬA LỖI: Đảm bảo MouseClicked gọi đúng ActionListener
                if (listener != null) {
                    listener.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, title));
                } else {
                     JOptionPane.showMessageDialog(card, "Chức năng: " + title + " đang được phát triển.");
                }
            }
            public void mouseEntered(MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseExited(MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }
}