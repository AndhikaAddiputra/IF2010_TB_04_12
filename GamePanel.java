import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // Scale the tile size
    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16; // 16 tiles across
    final int maxScreenRow = 12; // 12 tiles down
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels high

    Thread gameThread; // Thread for the game loop

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // Enable double buffering for smoother graphics}
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // Start the game loop
    }

    @Override
    public void run() {
        // Game loop

        while (gameThread != null) {
            // Update game logic here
            System.out.println("the game loop is running");

            // 1 UPDATE
            update();

            // 2 DRAW
            repaint();
        }
    }

    public void update() {
        // Update game logic here
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the parent class's paintComponent method
        Graphics2D g2 = (Graphics2D) g; // Cast to Graphics2D for better rendering

        // Draw the game elements here
        // Example: g2.drawRect(0, 0, tileSize, tileSize); // Draw a rectangle

        g2.setColor(Color.WHITE);
        g2.fillRect(100, 100, tileSize, tileSize); // Draw a filled rectangle
        g2.dispose(); // Dispose of the graphics context
    }
}
