package controller;

import java.awt.Point;
import java.util.Scanner;
import model.*;
import utility.*;

public class WorldActionController {
    private Player player;
    private final WorldMap worldMap;
    private GameState gameState;
    private MessageListener messageListener;
    private UserInputListener inputListener;
    private static int goldExpenditure = 0;
    private static int freqChatting = 0;
    private static int freqGifting = 0;
    private static int visitNPCcount = 0;

    private final FishingController fishingController;

    public WorldActionController(Player player, WorldMap worldMap, GameState gameState, MessageListener messageListener, UserInputListener userInputListener) {
        this.player = player;
        this.worldMap = worldMap;
        this.gameState = gameState;
        this.messageListener = messageListener;
        this.inputListener = userInputListener;

        this.fishingController = new FishingController(messageListener, userInputListener);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public FishingController getFishingController() {
        return fishingController;
    }

    private boolean canPerformAction(int energyCost) {
        if (player.getEnergy() - energyCost < -20) {
            notify("You are too exhausted to perform this action.");
            //System.out.println("You are too exhausted to perform this action.");
            return false;
        }
        return true;
    }

    public void visit() {
    // Check if player is at the edge of farm map (x = 31 or y = 31)
        Point pos = player.getPosition();
        if (pos.x < 31 && pos.y < 31 && pos.x > 0 && pos.y > 0) {
            notify("❌ You must be at the edge of your farm to leave.");
            //System.out.println("❌ You must be at the edge of your farm to leave.");
            return;
        }

        if (!canPerformAction(10)){
            notify("❌ You don't have enough energy to visit the world map. You need at least 10 energy.");
            return;
        }

        notify("📍 Available locations to visit:");
        notify("\nNPC Houses:");
        notify("1. Mayor Tadi's House");
        notify("2. Caroline's House");
        notify("3. Perry's House");
        notify("4. Dasco's House");
        notify("5. Abigail's House");
        notify("6. Emily's Store");

        notify("\nOther Locations:");
        notify("7. Forest River");
        notify("8. Mountain Lake");
        notify("9. Ocean");

        // Get player choice
        notify("🌍 Enter location number (1-9): ");
        inputListener.requestInput("Enter location number (1-9):", input -> {
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                notify("❌ Invalid number.");
                return;
            }

            Location selected = switch (choice) {
                case 1 -> new NPCBuilding("Mayor Tadi");
                case 2 -> new NPCBuilding("Caroline");
                case 3 -> new NPCBuilding("Perry");
                case 4 -> new NPCBuilding("Dasco");
                case 5 -> new NPCBuilding("Abigail");
                case 6 -> new Store("Emily");
                case 7 -> new ForestRiver();
                case 8 -> new MountainLake();
                case 9 -> new Ocean();
                default -> null;
            };

            if (selected == null) {
                notify("❌ Invalid selection.");
            } else {
                gameState.setInWorldMap(true);
                gameState.setCurrentWorldLocation(selected);
                notify("🧭 Visiting " + selected.getName());
            }
        });
    }

    public void back(){
        if (!player.isOutsideFarm()) {
            notify("❌ You are not at the world map.");
            //System.out.println("❌ You are not at the world map.");
            return;
        }
        player.setOutsideFarm(false);
        gameState.setInWorldMap(false);
        gameState.setCurrentWorldLocation(null);
        notify("🏠 You are back at your farm.");
        //System.out.println("🏠 You are back at your farm.");
    }

    public void handleLocationActions(Location location) {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean inLocation = true;
    
        while (inLocation) {
            if (location instanceof NPCBuilding) {
                incrementVisitCount();
                System.out.println("\n📍 You are at: " + location.getName());
                System.out.println("Available actions:");
                System.out.println("1. Chat");
                System.out.println("2. Gift");
                System.out.println("3. Check NPC Items");
                System.out.println("4. Propose");
                System.out.println("5. Marry");
                System.out.println("6. Watch TV");
                System.out.println("7. Back to World Map");
                System.out.println("8. Return to Farm");
    
                System.out.print("\nSelect action (1-8): ");
                input = scanner.nextLine();
    
                NPCBuilding npcHouse = (NPCBuilding) location;
                String npcName = npcHouse.getNPC().getName();
    
                switch (input) {
                    case "1" -> chat(npcName);
                    case "2" -> {
                        System.out.print("Enter item name to gift: ");
                        String itemName = scanner.nextLine();
                        gift(npcName, itemName);
                    }
                    case "3" -> {
                        NPC npc = NPCRegistry.get(npcName);
                        System.out.println("\n❤️ Loved Items: " + npc.getLovedItems());
                        System.out.println("👍 Liked Items: " + npc.getLikedItems());
                        System.out.println("👎 Hated Items: " + npc.getHateItems());
                    }
                    case "4" -> propose(npcName);
                    case "5" -> marry(npcName);
                    case "6" -> watchTV();
                    case "7" -> inLocation = false;
                    case "8" -> {
                        back();
                        inLocation = false;
                    }
                    default -> System.out.println("❌ Invalid action.");
                }
    
            } else if (location instanceof FishingSpot) {
                System.out.println("\n📍 You are at: " + location.getName());
                System.out.println("Available actions:");
                System.out.println("1. Fish");
                System.out.println("2. Back to World Map");
                System.out.println("3. Return to Farm");
    
                System.out.print("\nSelect action (1-3): ");
                input = scanner.nextLine();
    
                switch (input) {
                    case "1" -> fishingController.fish(player, gameState);
                    case "2" -> inLocation = false;
                    case "3" -> {
                        back();
                        inLocation = false;
                    }
                    default -> System.out.println("❌ Invalid action.");
                }
            }
    
            if (inLocation) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    // Melamar NPC
    public void propose(String npcName) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null) {
            notify("NPC not found.");
            //System.out.println("NPC not found.");
            return;
        }
        if (!canPerformAction(20)){
            notify("❌ You don't have enough energy to propose your love one. You need at least 20 energy.");
            return;
        }
        if (!player.getInventory().hasItem("Proposal Ring")) {
            notify("You need a Proposal Ring to propose.");
            //System.out.println("You need a Proposal Ring to propose.");
            return;
        }
        if (npc.getHeartPoints() < 150) {
            notify(npcName + " is not ready to be proposed (need 150 heart points). Lamaranmu gagal lek");
            player.reduceEnergy(20);
            gameState.advanceTime(60);
            //System.out.println(npcName + " is not ready to be proposed (need 150 heart points).");
            return;
        }
        if (npc.getStatus() == RelationshipStatus.FIANCE || npc.getStatus() == RelationshipStatus.SPOUSE) {
            notify(npcName + " is already your fiance or married.");
            //System.out.println(npcName + " is already your fiance or married.");
            return;
        }
        npc.setStatus(RelationshipStatus.FIANCE);
        player.reduceEnergy(10);
        gameState.advanceTime(60);
        player.setPartner(npc, npc.getStatus().getStatusString());
        notify("Congratulations! " + npcName + " accepted your proposal.");
        //System.out.println("Congratulations! " + npcName + " accepted your proposal.");
    }

    // Menikah dengan NPC
    public void marry(String npcName) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null || npc.getStatus() != RelationshipStatus.FIANCE) {
            notify("You can only marry your fiance.");
            //System.out.println("You can only marry your fiance.");
            return;
        }
        if (!canPerformAction(80)){
            notify("❌ You don't have enough energy to marry. You need at least 80 energy.");
            return;
        }
        if (!player.getInventory().hasItem("Proposal Ring")) {
            notify("You need a Proposal Ring to marry.");
            //System.out.println("You need a Proposal Ring to marry.");
            return;
        }
        player.reduceEnergy(80);
        gameState.setTime(new Time(22, 0));
        npc.setStatus(RelationshipStatus.SPOUSE);
        player.setPartner(npc, npc.getStatus().getStatusString());
        notify("You are now married to " + npcName + "! Time skips to 22:00 and you are sent home.");
        //System.out.println("You are now married to " + npcName + "! Time skips to 22:00 and you are sent home.");
    }

    // Menonton TV di rumah NPC
    public void watchTV() {
        if (!player.getPosition().equals(new Point(10, 10))) {
            notify("You can only watch TV at home.");
            //System.out.println("You can only watch TV at home.");
            return;
        }
        player.reduceEnergy(5);
        gameState.advanceTime(15);
        notify("You watched TV for 15 minutes. -5 energy.");
        //System.out.println("You watched TV for 15 minutes. -5 energy.");
    }

    // Chatting dengan NPC di rumah NPC
    public void chat(String npcName) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null) {
            notify("NPC not found.");
            //System.out.println("NPC not found.");
            return;
        }
        if (!canPerformAction(10)){
            notify("❌ You don't have enough energy to chat. You need at least 10 energy.");
            return;
        }
        
        player.reduceEnergy(10);
        gameState.advanceTime(10);
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        notify("You chatted with " + npcName + ". +10 heart points, -10 energy.");
        freqChatting++;
        //System.out.println("You chatted with " + npcName + ". +10 heart points, -10 energy.");
    }

    // Memberi hadiah ke NPC di rumah NPC
    public void gift(String npcName, String itemName) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null) {
            notify("NPC not found.");
            //System.out.println("NPC not found.");
            return;
        }
        if (!canPerformAction(5)){
            notify("❌ You don't have enough energy to give a gift. You need at least 5 energy.");
            return;
        }
        
        Item item = player.getInventory().getItem(itemName);
        if (item == null) {
            notify("You don't have " + itemName + " to give.");
            //System.out.println("You don't have " + itemName + " to give.");
            return;
        }
        int heartDelta = 0;
        if (npc.getLovedItems() != null && npc.getLovedItems().contains(itemName)) {
            heartDelta = 25;
        } else if (npc.getLikedItems() != null && npc.getLikedItems().contains(itemName)) {
            heartDelta = 20;
        } else if (npc.getHateItems() != null && npc.getHateItems().contains(itemName)) {
            heartDelta = -25;
        }
        npc.setHeartPoints(npc.getHeartPoints() + heartDelta);
        player.getInventory().removeItem(itemName, 1);
        player.reduceEnergy(5);
        gameState.advanceTime(10);
        freqGifting++;
        notify("You gave " + itemName + " to " + npcName + ". Heart points: " + (heartDelta >= 0 ? "+" : "") + heartDelta + ", -5 energy.");
        //System.out.println("You gave " + itemName + " to " + npcName + ". Heart points: " + (heartDelta >= 0 ? "+" : "") + heartDelta + ", -5 energy.");
    }

    // Belanja di toko neng emily
    public void buy(String itemName, int quantity) {
        if (!(gameState.getCurrentWorldLocation() instanceof Store store)) {
            notify("❌ You must be in a store to buy items.");
            return;
        }
    
        Item itemToBuy = store.getInventory().getItem(itemName);
        if (itemToBuy == null) {
            notify("❌ Item not found in store: " + itemName);
            return;
        }
    
        int pricePerItem = Store.getBuyPrice(itemName);
        if (pricePerItem == -1) {
            notify("❌ No price found for item: " + itemName);
            return;
        }
    
        int totalPrice = pricePerItem * quantity;
        if (player.getGold() < totalPrice) {
            notify("❌ Not enough gold. You need " + totalPrice + " gold.");
            return;
        }
    
        // Process transaction
        player.getInventory().addItem(itemToBuy, quantity);
        player.setGold(player.getGold() - totalPrice);
        goldExpenditure += totalPrice;
        notify("✅ Bought " + quantity + " x " + itemName + " for " + totalPrice + " gold.");
    }

    private void notify(String msg) {
        if (messageListener != null) {
            messageListener.onMessage(msg);
        } else {
            System.out.println(msg); // fallback CLI
        }
    }

    public int getGoldExpenditure() {
        return goldExpenditure;
    }

    public int getFreqChatting() {
        return freqChatting;
    }

    public int getFreqGifting() {
        return freqGifting;
    } 
    
    public void incrementVisitCount() {
        visitNPCcount++;
    }
    
    public int getVisitNPCcount() {
        return visitNPCcount;
    }
}