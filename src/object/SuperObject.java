package object;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import main.GamePanel;

public class SuperObject {
    public BufferedImage image; // Image of the object
    public String name; // Name of the object
    public boolean collision = false; // Collision flag
    public int worldX, worldY; // World coordinates of the object

    public void draw(Graphics2D g2, GamePanel gp) {
        // Draw the object (to be implemented in subclasses)
        int screenX = worldX - gp.player.worldX + gp.player.screenX; // Calculate the screen X position
        int screenY = worldY - gp.player.worldY + gp.player.screenY; // Calculate the screen Y position

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // Check if the tile is within the screen bounds
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); // Draw the tile
        }
    }
}

