package main;

import javax.swing.*;
import java.awt.*;

import entity.Player;
import object.SuperObject;
// import tile.Tile;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    //screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // Scale the tile size
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16; // 16 tiles across
    public final int maxScreenRow = 12; // 12 tiles down
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels high

    //world settings
    public final int maxWorldCol = 50; // 50 tiles across
    public final int maxWorldRow = 50; // 50 tiles down
    public final int worldWidth = tileSize * maxWorldCol; // 768 pixels wide
    public final int worldHeight = tileSize * maxWorldRow; // 576 pixels high

    int FPS = 60; // Frames per second

    TileManager tileM = new TileManager(this); // Tile manager for handling tiles
    KeyHandler keyH = new KeyHandler(); // Key handler for input
    Thread gameThread; // Thread for the game loop
    public CollisionChecker cChecker = new CollisionChecker(this); // Collision checker for handling collisions
    public AssetSetter aSetter = new AssetSetter(this); // Asset setter for initializing game objects
    public Player player = new Player(this, keyH); // Player object
    public SuperObject obj[] = new SuperObject[10]; // Array of game objects

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // Enable double buffering for smoother graphics}
        this.addKeyListener(keyH); // Add key listener for input
        this.setFocusable(true); // Make the panel focusable to receive key events
    }

    public void setupGame() {
        aSetter.setObject(); // Set up game objects
        // aSetter.setNPC(); // Set up NPCs (if any)
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // Start the game loop
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / FPS; // Draw every 1/60th of a second
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
        tileM.draw(g2); // Draw the tiles

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this); // Draw the objects
            }
        }

        player.draw(g2);
        
        g2.dispose(); // Dispose of the graphics context
    }
}
