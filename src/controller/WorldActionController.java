package controller;

import java.awt.Point;
import java.util.Scanner;
import model.*;
import utility.*;

public class WorldActionController {
    private Player player;
    private final WorldMap worldMap;
    private GameState gameState;

    public WorldActionController(Player player, WorldMap worldMap, GameState gameState) {
        this.player = player;
        this.worldMap = worldMap;
        this.gameState = gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void visit() {
    // Check if player is at the edge of farm map (x = 31 or y = 31)
        Point pos = player.getPosition();
        if (pos.x < 31 && pos.y < 31 && pos.x > 0 && pos.y > 0) {
            System.out.println("❌ You must be at the edge of your farm to leave.");
            return;
        }

        System.out.println("📍 Available locations to visit:");
        System.out.println("\nNPC Houses:");
        System.out.println("1. Mayor Tadi's House");
        System.out.println("2. Caroline's House");
        System.out.println("3. Perry's House");
        System.out.println("4. Dasco's House");
        System.out.println("5. Abigail's House");
        System.out.println("6. Emily's Store");
        
        System.out.println("\nOther Locations:");
        System.out.println("7. Forest River");
        System.out.println("8. Mountain Lake");
        System.out.println("9. Ocean");

        // Get player choice
        System.out.print("\nEnter location number (1-9): ");
        Scanner scanner = new Scanner(System.in);
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
            return;
        }

        Location destination = null;
        switch (choice) {
            case 1 -> destination = worldMap.getLocation("Mayor Tadi's House");
            case 2 -> destination = worldMap.getLocation("Caroline's House");
            case 3 -> destination = worldMap.getLocation("Perry's House");
            case 4 -> destination = worldMap.getLocation("Dasco's House");
            case 5 -> destination = worldMap.getLocation("Abigail's House");
            case 6 -> destination = worldMap.getLocation("Store");
            case 7 -> destination = worldMap.getLocation("ForestRiver");
            case 8 -> destination = worldMap.getLocation("MountainLake");
            case 9 -> destination = worldMap.getLocation("Ocean");
            default -> {
                System.out.println("❌ Invalid location number.");
                return;
            }
        }

        if (destination != null) {
            player.setOutsideFarm(true);
            player.reduceEnergy(10);
            gameState.setInWorldMap(true);
            gameState.setCurrentWorldLocation(destination);
            destination.visit(player);
            gameState.advanceTime(15);
            System.out.println("🚶 Traveled to " + destination.getName() + ".");
            handleLocationActions(destination);
        }
    }

    public void back(){
        if (!player.isOutsideFarm()) {
            System.out.println("❌ You are not at the world map.");
            return;
        }
        player.setOutsideFarm(false);
        gameState.setInWorldMap(false);
        gameState.setCurrentWorldLocation(null);
        System.out.println("🏠 You are back at your farm.");
    }

    public void handleLocationActions(Location location) {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean inLocation = true;
    
        while (inLocation) {
            if (location instanceof NPCBuilding) {
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
                    case "1" -> chat(npcName, player.getPosition());
                    case "2" -> {
                        System.out.print("Enter item name to gift: ");
                        String itemName = scanner.nextLine();
                        gift(npcName, itemName, player.getPosition());
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
                    case "1" -> new FishingController().fish(player, gameState);
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
            System.out.println("NPC not found.");
            return;
        }
        if (!player.getInventory().hasItem("Proposal Ring")) {
            System.out.println("You need a Proposal Ring to propose.");
            return;
        }
        if (npc.getHeartPoints() < 150) {
            System.out.println(npcName + " is not ready to be proposed (need 150 heart points).");
            return;
        }
        if (npc.getStatus() == RelationshipStatus.FIANCE || npc.getStatus() == RelationshipStatus.SPOUSE) {
            System.out.println(npcName + " is already your fiance or married.");
            return;
        }
        npc.setStatus(RelationshipStatus.FIANCE);
        player.reduceEnergy(10);
        gameState.advanceTime(60);
        System.out.println("Congratulations! " + npcName + " accepted your proposal.");
    }

    // Menikah dengan NPC
    public void marry(String npcName) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null || npc.getStatus() != RelationshipStatus.FIANCE) {
            System.out.println("You can only marry your fiance.");
            return;
        }
        if (!player.getInventory().hasItem("Proposal Ring")) {
            System.out.println("You need a Proposal Ring to marry.");
            return;
        }
        player.reduceEnergy(80);
        gameState.setTime(new Time(22, 0));
        npc.setStatus(RelationshipStatus.SPOUSE);
        player.setPartner(npc, npc.getStatus().getStatusString());
        System.out.println("You are now married to " + npcName + "! Time skips to 22:00 and you are sent home.");
    }

    // Menonton TV di rumah NPC
    public void watchTV() {
        if (!player.getPosition().equals(new Point(10, 10))) {
            System.out.println("You can only watch TV at home.");
            return;
        }
        player.reduceEnergy(5);
        gameState.advanceTime(15);
        System.out.println("You watched TV for 15 minutes. -5 energy.");
    }

    // Chatting dengan NPC di rumah NPC
    public void chat(String npcName, Point npcHomePosition) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null) {
            System.out.println("NPC not found.");
            return;
        }
        if (npcHomePosition == null || !player.getPosition().equals(npcHomePosition)) {
            System.out.println("You must be at " + npcName + "'s house to chat.");
            return;
        }
        player.reduceEnergy(10);
        gameState.advanceTime(10);
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        System.out.println("You chatted with " + npcName + ". +10 heart points, -10 energy.");
    }

    // Memberi hadiah ke NPC di rumah NPC
    public void gift(String npcName, String itemName, Point npcHomePosition) {
        NPC npc = NPCRegistry.get(npcName);
        if (npc == null) {
            System.out.println("NPC not found.");
            return;
        }
        if (npcHomePosition == null || !player.getPosition().equals(npcHomePosition)) {
            System.out.println("You must be at " + npcName + "'s house to give a gift.");
            return;
        }
        Item item = player.getInventory().getItem(itemName);
        if (item == null) {
            System.out.println("You don't have " + itemName + " to give.");
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
        System.out.println("You gave " + itemName + " to " + npcName + ". Heart points: " + (heartDelta >= 0 ? "+" : "") + heartDelta + ", -5 energy.");
    }
}