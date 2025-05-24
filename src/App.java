import javax.swing.SwingUtilities;
import view.MainMenuWindow;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuWindow menu = new MainMenuWindow();
            menu.setVisible(true);
        });
    }
}
