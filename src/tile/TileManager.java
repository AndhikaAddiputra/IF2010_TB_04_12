package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile; // Array of tiles
    int[][] mapTileNum; // 2D array to store tile numbers for the map

    public TileManager(GamePanel gp) {
        this.gp = gp; // Reference to the game panel
        tile = new Tile[10]; // Initialize the tile array with a size of 10
        mapTileNum = new int[gp.maxScreenRow][gp.maxScreenCol]; // Initialize the map tile number array
        getTileImage(); // Load the tile images
        loadMap(); // Load the map layout
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile(); // Create a new tile object
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/grass01.png")); // Set the tile image

            tile[1] = new Tile(); // Create a new tile object
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/wall.png")); // Set the tile image

            tile[2] = new Tile(); // Create a new tile object
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/image/tile/water00.png")); // Set the tile image
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/res/maps/map01.txt"); // Open the map file
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Create a buffered reader

            int row = 0; // Row index

            while (row < gp.maxScreenRow) { // Loop through the rows
                String line = br.readLine(); // Read a line from the map file
                String[] numbers = line.split(" "); // Split the line into numbers

                for (int col = 0; col < gp.maxScreenCol; col++) { // Loop through the columns
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
        // Loop through the tiles and draw them on the screen
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int tileNum = mapTileNum[row][col]; // Get the tile number from the map
                int x = col * gp.tileSize; // Calculate the x position
                int y = row * gp.tileSize; // Calculate the y position

                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null); // Draw the tile
            }
        }
    }
}
