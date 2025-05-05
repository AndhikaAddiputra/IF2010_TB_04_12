package entity;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        // super(gp.tileSize, gp.tileSize, 4, "down"); // Initialize the player with default values
        this.gp = gp; // Reference to the game panel
        this.keyH = keyH; // Reference to the key handler
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100; // Set the player's initial X position
        y = 100; // Set the player's initial Y position
        speed = 4; // Set the player's speed
        direction = "down"; // Set the player's initial direction
    }

    public void getPlayerImage() {
        try {
            stand = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_stand.png")); // Load the player's standing image
            // Load the player's images for different directions
            up1 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_up_1.png")); // Load the player's up image
            up2 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_up_2.png")); // Load the player's up image
            down1 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_down_1.png")); // Load the player's down image
            down2 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_down_2.png")); // Load the player's down image
            left1 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_left_1.png")); // Load the player's left image
            left2 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_left_2.png")); // Load the player's left image  
            right1 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_right_1.png")); // Load the player's right image
            right2 = ImageIO.read(getClass().getResourceAsStream("/image/player_hijab/hijab_right_2.png")); // Load the player's right image

        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                direction = "up"; // Set the direction to up
                y -= speed; // Move up
            }
            else if (keyH.downPressed == true) {
                direction = "down"; // Set the direction to down
                y += speed; // Move down
            }
            if (keyH.leftPressed == true) {
                direction = "left"; // Set the direction to left
                x -= speed; // Move left
            }
            else if (keyH.rightPressed == true) {
                direction = "right"; // Set the direction to right
                x += speed; // Move right
            }
    
            spriteCounter++; // Increment the sprite counter
            if (spriteCounter > 12) { // change every ... frames
                if (spriteNum == 1) {
                    spriteNum = 2; // Switch to the second sprite
                }
                else if (spriteNum == 2) {
                    spriteNum = 1; // Switch to the first sprite
                }
                spriteCounter = 0; // Reset the sprite counter
            }
        }
        else {
            direction = "stand"; // Set the direction to stand
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.WHITE);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize); // Draw a filled rectangle
        BufferedImage image = null; // Initialize the image variable
        switch (direction) {
            case "stand":
                image = stand; // Set the image to the standing image
                break;
            case "up":
                if(spriteNum == 1) {
                    image = up1; // Set the image to the first up image
                }
                if(spriteNum == 2) {
                    image = up2; // Set the image to the second up image
                }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1; // Set the image to the first down image
                }
                if(spriteNum == 2) {
                    image = down2; // Set the image to the second down image
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1; // Set the image to the first left image
                }
                if(spriteNum == 2) {
                    image = left2; // Set the image to the second left image
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1; // Set the image to the first right image
                }
                if(spriteNum == 2) {
                    image = right2; // Set the image to the second right image
                }
                break;
        
            // default:
            //     break;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null); // Draw the image at the player's position
    }
}
