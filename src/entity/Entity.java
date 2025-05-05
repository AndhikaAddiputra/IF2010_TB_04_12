package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y; // Entity's position
    public int speed; // Entity's speed
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // Entity's images
    public String direction; // Entity's direction
    public int spriteCounter = 0; // Counter for sprite animation
    public int spriteNum = 1; // Current sprite number
}