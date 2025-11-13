package view;

import javax.swing.*;
import java.awt.*;

public class AppFooter extends JPanel {
    public AppFooter() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(new Color(41, 128, 185)); // Màu xanh đậm cho footer
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel footerText = new JLabel("Trường Đại học Công nghệ GTVT - 54 Triều Khúc, Hà Nội");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(Color.WHITE);
        add(footerText);
    }
}