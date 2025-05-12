package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY; // Entity's position
    public int speed; // Entity's speed
    public BufferedImage stand, up1, up2, down1, down2, left1, left2, right1, right2; // Entity's images
    public String direction; // Entity's direction
    public int spriteCounter = 0; // Counter for sprite animation
    public int spriteNum = 1; // Current sprite number
    public Rectangle solidArea; // Rectangle for collision detection
    public boolean collisionOn = false; // Collision flag
}