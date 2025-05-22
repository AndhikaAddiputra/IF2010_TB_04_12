package model;

import java.awt.Point;
import utility.Season;
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

    public void killOutOfSeasonCrops(Season season){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = grid[y][x];
                if (tile.getType() == TileType.PLANTED) {
                    if (!tile.getSeed().getSeason().contains(season)) {
                        tile.setSeed(null);
                        tile.setRemainingHarvestMinutes(-1);
                        tile.setType(TileType.TILLABLE);
                        System.out.println("🌿 Crop at (" + x + "," + y + ") died due to season change.");
                    }
                }
            }
        }
    }

    public void waterAllTillableTiles(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = grid[y][x];
                if (tile.getType() == TileType.TILLED || tile.getType() == TileType.PLANTED) {
                    tile.startGrowth();
                }
            }
        }
    }

    public boolean isNearPond(Point position) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
    
                int x = position.x + dx;
                int y = position.y + dy;
    
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                    Tile tile = grid[y][x];
                    if (tile.getType() == TileType.POND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAtMapEdge(Point pos) {
        return pos.x == 0 || pos.x == WIDTH - 1 || pos.y == 0 || pos.y == HEIGHT - 1;
    }

    public boolean isNearHouse(Point pos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Point neighbor = new Point(pos.x + dx, pos.y + dy);
                if (farmMap.isValidPosition(neighbor)) {
                    Tile tile = farmMap.getTileAt(neighbor);
                    if (tile.getType() == TileType.HOUSE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // handler shopping tambahan
    public boolean isNearShippingBin(Point pos){
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Point neighbor = new Point(pos.x + dx, pos.y + dy);
                if (farmMap.isValidPosition(neighbor)) {
                    Tile tile = farmMap.getTileAt(neighbor);
                    if (tile.getType() == TileType.SHIPPING_BIN) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
}
