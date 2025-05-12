package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public Tile[] tile; // Array of tiles
    public int[][] mapTileNum; // 2D array to store tile numbers for the map

    public TileManager(GamePanel gp) {
        this.gp = gp; // Reference to the game panel
        tile = new Tile[10]; // Initialize the tile array with a size of 10
        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol]; // Initialize the map tile number array
        getTileImage(); // Load the tile images
        loadMap("/res/maps/worldMap01.txt"); // Load the map layout
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile(); // Create a new tile object
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/grass01.png")); // Set the tile image

            tile[1] = new Tile(); // Create a new tile object
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/wall.png")); // Set the tile image
            // tile[1].collision = true; // Set collision for the tile

            tile[2] = new Tile(); // Create a new tile object
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/water00.png")); // Set the tile image
            // tile[2].collision = true; // Set collision for the tile

            tile[3] = new Tile(); // Create a new tile object
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/earth.png")); // Set the tile image

            tile[4] = new Tile(); // Create a new tile object
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/tree.png")); // Set the tile image
            // tile[4].collision = true; // Set collision for the tile

            tile[5] = new Tile(); // Create a new tile object
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/floor01.png")); // Set the tile image

        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath); // Open the map file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Create a buffered reader

            int row = 0; // Row index

            while (row < gp.maxWorldRow) { // Loop through the rows
                String line = br.readLine(); // Read a line from the map file
                String[] numbers = line.split(" "); // Split the line into numbers

                for (int col = 0; col < gp.maxWorldCol; col++) { // Loop through the columns
                    int num = Integer.parseInt(numbers[col]); // Parse the number
                    mapTileNum[row][col] = num; // Store the number in the map tile number array
                }
                row++; // Move to the next row
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        int worldCol = 0; // Column index
        int worldRow = 0; // Row index

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) { // Loop through the columns and rows
            int tileNum = mapTileNum[worldRow][worldCol]; // Get the tile number from the map

            int worldX = worldCol * gp.tileSize; // Calculate the X position
            int worldY = worldRow * gp.tileSize; // Calculate the Y position
            int screenX = worldX - gp.player.worldX + gp.player.screenX; // Calculate the screen X position
            int screenY = worldY - gp.player.worldY + gp.player.screenY; // Calculate the screen Y position

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && // Check if the tile is within the screen bounds
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null); // Draw the tile
            }

            worldCol++; // Move to the next column
            if (worldCol == gp.maxWorldCol) { // If reached the end of the row
                worldCol = 0; // Reset column index
                worldRow++; // Move to the next row
            }
        }
    }
}
