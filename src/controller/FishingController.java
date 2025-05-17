package controller;

import model.*;
import utility.GameState;
import utility.Season;
import utility.TileType;
import utility.Time;
import utility.Weather;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.Point;

public class FishingController {
    private final Random random = new Random();

    public void fish(Player player, GameState gameState) {
        // 1. Validasi lokasi
        Location currentLocation;

        if (gameState.isInWorldMap()) {
            currentLocation = gameState.getCurrentWorldLocation();
        } else {
            currentLocation = new PondLocation(); // farm pond default
        }
        

        // 2. Validasi energi
        if (player.getEnergy() < -15) {
            System.out.println("⚠️ Not enough energy to fish.");
            return;
        }

        // 3. Ambil informasi waktu & cuaca
        Time currentTime = gameState.getTime();
        Weather currentWeather = gameState.getWeather();
        Season currentSeason = gameState.getSeason();

        // 4. Filter ikan berdasarkan kondisi
        List<Fish> eligibleFish = FishRegistry.getAllFish().stream()
            .filter(fish ->
                fish.getSeason().contains(currentSeason) &&
                fish.getWeather().contains(currentWeather) &&
                fish.getLocation().stream().anyMatch(loc -> loc.getClass().equals(currentLocation.getClass())) &&
                fish.getAvailableTime().stream().anyMatch(range -> range.contains(currentTime))
            ).collect(Collectors.toList());

        if (eligibleFish.isEmpty()) {
            System.out.println("🐟 No fish appear at this time and place.");
            return;
        }

        // 5. Pilih ikan secara acak
        Fish selectedFish = eligibleFish.get(random.nextInt(eligibleFish.size()));
        int upperBound;
        int maxAttempts;

        switch (selectedFish.getType()) {
            case "Common Fish":
                upperBound = 10;
                maxAttempts = 10;
                break;
            case "Regular Fish":
                upperBound = 100;
                maxAttempts = 10;
                break;
            case "Legendary Fish":
                upperBound = 500;
                maxAttempts = 7;
                break;
            default:
                upperBound = 100;
                maxAttempts = 10;
        }

        int target = random.nextInt(upperBound) + 1;

        System.out.println("🎣 You cast your line...");
        System.out.println("💡 A " + selectedFish.getItemName() + " is biting! Guess a number between 1 and " + maxAttempts+ ". It's a common fish!" +selectedFish.getType());

        // 6. Pause waktu, -5 energi, +15 menit setelah selesai
        player.setEnergy(player.getEnergy() - 5);

        Scanner scanner = new Scanner(System.in);
        int attempt = 0;
        boolean success = false;

        while (attempt < maxAttempts) {
            System.out.print("🔢 Attempt " + (attempt + 1) + ": Your guess? ");
            String input = scanner.nextLine();

            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid input. Please enter a number.");
                continue;
            }

            if (guess == target) {
                success = true;
                break;
            } else if (guess < target) {
                System.out.println("📉 Too low!");
            } else {
                System.out.println("📈 Too high!");
            }
            attempt++;
        }

        // 7. Hasil
        gameState.advanceTime(15);
        if (success) {
            player.getInventory().addItem(selectedFish);
            System.out.println("✅ You caught a " + selectedFish.getItemName() + "!");
        } else {
            System.out.println("❌ The fish escaped...");
        }
    }

}

