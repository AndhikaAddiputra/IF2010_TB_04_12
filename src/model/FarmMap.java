package model;

import java.awt.Point;
import utility.TileType;

public class FarmMap {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private Tile[][] grid;
    private Point playerPosition;
    private Point housePosition;

    public FarmMap() {
        grid = new Tile[HEIGHT][WIDTH];
        generateEmptyLand();
        placeHouseAndPond();
        placeShippingBin();
        placePlayer();
    }

    private void generateEmptyLand() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = new Tile(TileType.TILLABLE, true);
            }
        }
    }

    private void placeHouseAndPond() {
        housePosition = new Point(5, 5); // placeholder
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                grid[housePosition.y + y][housePosition.x + x] = new Tile(TileType.HOUSE, false);
            }
        }
        Point pondPosition = new Point(15, 10);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                grid[pondPosition.y + y][pondPosition.x + x] = new Tile(TileType.POND, false);
            }
        }
    }

    private void placeShippingBin() {
        int x = housePosition.x + 6 + 1;
        int y = housePosition.y;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                grid[y + i][x + j] = new Tile(TileType.SHIPPING_BIN, false);
            }
        }
    }

    private void placePlayer() {
        playerPosition = new Point(housePosition.x + 6, housePosition.y + 3);
    }

    public Tile getTileAt(Point pos) {
        return grid[pos.y][pos.x];
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public boolean isValidPosition(Point pos) {
        return pos.x >= 0 && pos.x < WIDTH && pos.y >= 0 && pos.y < HEIGHT;
    }

    public boolean isAdjacentTo(Point pos, TileType type) {
        int[][] directions = {{0,1},{1,0},{0,-1},{-1,0}};
        for (int[] dir : directions) {
            int nx = pos.x + dir[0];
            int ny = pos.y + dir[1];
            if (isValidPosition(new Point(nx, ny))) {
                if (grid[ny][nx].getType() == type) return true;
            }
        }
        return false;
    }

    public void tick(int minutes) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = grid[y][x];
                tile.reduceHarvestTime(minutes);
            }
        }
    }
    
}
