package controller;

import model.*;
import utility.*;

import java.awt.Point;

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
        if (npc.getStatus() == RelationshipStatus.FIANCE || npc.getStatus() == RelationshipStatus.MARRIED) {
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
        npc.setStatus(RelationshipStatus.MARRIED);
        player.setPartner(npc);
        System.out.println("You are now married to " + npcName + "! Time skips to 22:00 and you are sent home.");
    }

    // Menonton TV di rumah (misal rumah di Point(10,10))
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