import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.MainMenuWindow;

public class App {
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Spakbor Hills");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainMenuWindow menu = new MainMenuWindow();
            menu.setVisible(true);
        });
    }
}
