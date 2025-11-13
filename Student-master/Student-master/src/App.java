import view.LoginFrame; // Import LoginFrame
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true); // Khởi chạy LoginFrame đầu tiên
        });
    }
}