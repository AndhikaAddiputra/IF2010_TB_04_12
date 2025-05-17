import controller.*;
import java.util.Scanner;
import model.*;
import utility.*;

public class MainDebug {
    public static void main(String[] args) {
        // Setup dasar permainan
        FarmMap farmMap = new FarmMap();
        Player player = new Player("Tester", 'M',"MyFarm");
        player.setPosition(new java.awt.Point(11, 10)); // Respawn di sisi rumah

        // Tambahkan beberapa item ke inventory
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Hoe"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Watering Can"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Pickaxe"), 1);
        player.getInventory().addItem(SeedsRegistry.getSeeds("Pumpkin Seeds"), 5);
        player.getInventory().addItem(FoodRegistry.getFood("Baguette"), 5);
        player.getInventory().addItem(FishRegistry.getFish("Salmon"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Firewood"), 3);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Pumpkin Pie Recipe"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Egg"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Coal"), 2);
        player.getInventory().addItem(CropRegistry.getCrop("Wheat"), 1);
        player.getInventory().addItem(CropRegistry.getCrop("Pumpkin"), 1);

        // Buat controller
        FarmActionController farmController = new FarmActionController(player, farmMap, null);
        GameState gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false);
        farmController.setGameState(gameState);
        gameState.setAutoSleepHandler(() -> farmController.sleep()); // handler autosleep nya
        CookingController cookingController = new CookingController(player, gameState, farmMap);
        FishingController fishingController = new FishingController();

        // Interface input CLI
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Game Started. Type help for command list.");
        while (true) {
            System.out.print("\nCommand > ");
            input = scanner.nextLine();

            switch (input.toLowerCase()) {
                case "help" -> {
                    System.out.println("Commands:");
                    System.out.println("move up/down/left/right");
                    System.out.println("till");
                    System.out.println("recover land");
                    System.out.println("plant [seed name]");
                    System.out.println("water");
                    System.out.println("harvest");
                    System.out.println("eat [item name]");
                    System.out.println("sleep");
                    System.out.println("inventory");
                    System.out.println("pos");
                    System.out.println("map");
                    System.out.println("status");
                    System.out.println("tile");
                    System.out.println("time");
                    System.out.println("exit");
                }
                case "cook" -> {
                    System.out.print("Enter recipe name: ");
                    String recipeName = scanner.nextLine();
                    System.out.print("Enter fuel item (Firewood/Coal): ");
                    String fuelName = scanner.nextLine();
                    cookingController.cook(recipeName, fuelName);
                }
                
                case "pos" -> System.out.println("Player Position: " + player.getPosition());
                case "inventory" -> player.getInventory().displayInventory();
                case "till" -> farmController.till();
                case "recover land" -> farmController.recoverLand();
                case "water" -> farmController.water();
                case "harvest" -> farmController.harvest();
                case "sleep" -> farmController.sleep();
                case "status" -> farmController.debugShowPlayerStatus();
                case "tile" -> farmController.debugShowTileInfo();
                case "map" -> printFarmMap(farmMap, player);
                case "enter house" -> farmController.enterHouse();
                case "exit house" -> farmController.exitHouse();
                case "time" -> System.out.println("⏱️ Current Time: " + gameState.getTime());
                case "exit" -> System.exit(0);
                case "watch" -> farmController.watchWeather();
                case "fishing" -> {
                    if (!farmMap.isNearPond(player.getPosition())) {
                        System.out.println("❌ You must be near the pond to fish here.");
                    } else {
                        fishingController.fish(player, gameState);
                    }
                }
                case "set time" -> {
                    System.out.print("Debug command");
                    System.out.print("Enter new time (hour): ");
                    Integer hourInput = scanner.nextInt();
                    System.out.print("Enter new time (minute): ");
                    Integer minuteInput = scanner.nextInt();
                    gameState.setTime(new Time(hourInput, minuteInput));
                }
                default -> {
                    if (input.startsWith("move ")) {
                        String dir = input.substring(5).toUpperCase();
                        try {
                            farmController.move(Direction.valueOf(dir));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid direction.");
                        }
                    } else if (input.startsWith("plant ")) {
                        String seedName = input.substring(6);
                        farmController.plant(seedName);
                    } else if (input.startsWith("eat ")) {
                        String itemName = input.substring(4);
                        farmController.eat(itemName);
                    } else {
                        System.out.println("Unknown command.");
                    }
                }
            }

            // Setelah aksi, tampilkan peta dan status
            if (!player.isInsideHouse()) {
                printFarmMap(farmMap, player);
            } else {
                System.out.println("🏠 You are currently inside the house.");
            }
            farmController.debugShowPlayerStatus();
        }
    }

    private static void printFarmMap(FarmMap map, Player player) {
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                if (player.getPosition().equals(new java.awt.Point(x, y))) {
                    System.out.print("p ");
                    continue;
                }
                Tile tile = map.getTileAt(new java.awt.Point(x, y));
                switch (tile.getType()) {
                    case HOUSE -> System.out.print("h ");
                    case POND -> System.out.print("o ");
                    case SHIPPING_BIN -> System.out.print("s ");
                    case TILLABLE -> System.out.print(". ");
                    case TILLED -> System.out.print("t ");
                    case PLANTED -> System.out.print("l ");
                    default -> System.out.print("? ");
                }
            }
            System.out.println();
        }
    }
}
