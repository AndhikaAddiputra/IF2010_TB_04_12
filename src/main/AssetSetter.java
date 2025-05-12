package main;

import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp) {
        this.gp = gp; // Reference to the game panel
    }

    public void setObject() {
        gp.obj[0] = new OBJ_Key(); // Create a new tree object
        gp.obj[0].worldX = 23 * gp.tileSize; // Set the object's world X position
        gp.obj[0].worldY = 7 * gp.tileSize; // Set the object's world Y position
        
        gp.obj[1] = new OBJ_Key(); // Create a new key object
        gp.obj[1].worldX = 23 * gp.tileSize; // Set the object's world X position
        gp.obj[1].worldY = 40 * gp.tileSize; // Set the object's world Y position

        gp.obj[2] = new OBJ_Door(); // Create a new key object
        gp.obj[2].worldX = 10 * gp.tileSize; // Set the object's world X position
        gp.obj[2].worldY = 11 * gp.tileSize; // Set the object's world Y position

        gp.obj[3] = new OBJ_Door(); // Create a new key object
        gp.obj[3].worldX = 0 * gp.tileSize; // Set the object's world X position
        gp.obj[3].worldY = 28 * gp.tileSize; // Set the object's world Y position
    }
    
}
