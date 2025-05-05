package main;

import javax.swing.*;
import java.awt.*;

import entity.Player;

public class GamePanel extends JPanel implements Runnable {
    //screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // Scale the tile size
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16; // 16 tiles across
    final int maxScreenRow = 12; // 12 tiles down
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels high

    KeyHandler keyH = new KeyHandler(); // Key handler for input
    Thread gameThread; // Thread for the game loop

    Player player = new Player(this, keyH); // Player object
    // set player
    int playerX = 100; // Player's X position
    int playerY = 100; // Player's Y position
    int playerSpeed = 4; // Player's speed

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // Enable double buffering for smoother graphics}
        this.addKeyListener(keyH); // Add key listener for input
        this.setFocusable(true); // Make the panel focusable to receive key events
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // Start the game loop
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / 60; // Draw every 1/60th of a second
        double delta = 0; // Time difference
        long lastTime = System.nanoTime(); // Get the current time
        long currentTime; // Variable to store the current time
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime(); // Get the current time

            delta += (currentTime - lastTime) / drawInterval; // Calculate the time difference
            timer += (currentTime - lastTime); // Update the timer
            lastTime = currentTime; // Update the last time

            if (delta >= 1) { // If enough time has passed
                // 1 UPDATE
                update(); // Update game logic

                // 2 DRAW
                repaint(); // Repaint the screen

                delta--; // Decrease delta
                drawCount++; // Increment the draw count
            }

            if (timer >= 1000000000) { // If 1 second has passed
                System.out.println("FPS: " + drawCount); // Print the FPS
                drawCount = 0; // Reset the draw count
                timer = 0; // Reset the timer
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the parent class's paintComponent method
        Graphics2D g2 = (Graphics2D) g; // Cast to Graphics2D for better rendering

        // Draw the game elements here
        // Example: g2.drawRect(0, 0, tileSize, tileSize); // Draw a rectangle
        player.draw(g2);
        
        g2.dispose(); // Dispose of the graphics context
    }
}
