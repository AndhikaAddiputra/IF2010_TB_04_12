import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Adjusts the window size to fit the game panel

        window.setLocationRelativeTo(null); // Centers the window on the screen
        window.setVisible(true);

        gamePanel.startGameThread(); // Start the game loop
    }
}