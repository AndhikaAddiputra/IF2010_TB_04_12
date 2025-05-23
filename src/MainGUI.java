import controller.FarmActionController;
import controller.FishingController;
import controller.WorldActionController;
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

        GameState gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false);
        FarmWindow window = new FarmWindow(player, farmMap, gameState, worldMap);
        FarmActionController farmController = new FarmActionController(player, farmMap, gameState, window, window);
        FishingController fishingController = new FishingController(window, window);
        WorldActionController worldController = new WorldActionController(player, worldMap, gameState, window, window);

        window.setFarmController(farmController);
        window.setFishingController(fishingController);
        window.setWorldController(worldController);

        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
        });
    }
}
