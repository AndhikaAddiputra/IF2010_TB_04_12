import javax.swing.SwingUtilities;
import model.*;
import utility.Season;
import utility.Weather;
import view.FarmWindow;

public class MainGUI {
    public static void main(String[] args){
        FarmMap farmMap = new FarmMap();
        WorldMap worldMap = new WorldMap();
        Player player = new Player("Tester", 'M',"MyFarm");
        player.setPosition(new java.awt.Point(11, 10));

        // Add items to inventory
        player.getInventory().addItem(SeedsRegistry.getSeeds("Parnsnip Seeds"), 5);
        player.getInventory().addItem(CropRegistry.getCrop("Wheat"), 5);
        player.getInventory().addItem(FishRegistry.getFish("Salmon"), 3);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Baguette Recipe"), 1);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Pumpkin Pie Recipe"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Coal"), 5);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Hoe"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Watering Can"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Pickaxe"), 1);

        GameState gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false, null);
        FarmWindow window = new FarmWindow(player, farmMap, gameState, worldMap);

        gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false, window);
        window.setGameState(gameState);

        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
        });
    }
}
