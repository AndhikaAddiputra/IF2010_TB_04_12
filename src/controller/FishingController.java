package controller;

import model.*;
import utility.MessageListener;
import utility.Season;
import utility.TileType;
import utility.UserInputListener;
import utility.Weather;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.Point;

public class FishingController {
    private final Random random = new Random();
    private MessageListener messageListener;
    private UserInputListener userInputListener;

    public FishingController(MessageListener messageListener, UserInputListener userInputListener) {
        this.messageListener = messageListener;
        this.userInputListener = userInputListener;
    }

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
            notify("⚠️ Not enough energy to fish.");
            //System.out.println("⚠️ Not enough energy to fish.");
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
            notify("🐟 No fish appear at this time and place.");
            //System.out.println("🐟 No fish appear at this time and place.");
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

        notify("🎣 You cast your line...");
        notify("💡 A " + selectedFish.getItemName() + " is biting! Guess a number between 1 and " + upperBound + ". It's a common fish!" + selectedFish.getType());
        //System.out.println("🎣 You cast your line...");
        //System.out.println("💡 A " + selectedFish.getItemName() + " is biting! Guess a number between 1 and " + upperBound+ ". It's a common fish!" +selectedFish.getType());

        // 6. Pause waktu, -5 energi, +15 menit setelah selesai
        player.setEnergy(player.getEnergy() - 5);

        startGuessingGame(maxAttempts, target,
            () -> {
                // success: tambahkan ikan ke inventory
                player.getInventory().addItem(selectedFish, 1);
                notify("🎣 " + selectedFish.getItemName() + " added to inventory.");
            },
            () -> {
                // failed
                notify("😭 You failed to catch the fish.");
            }
        );
    }

    private void startGuessingGame(int maxAttempts, int target, Runnable onSuccess, Runnable onFail) {
        attemptGuess(0, maxAttempts, target, onSuccess, onFail);
    }
    
    private void attemptGuess(int attempt, int maxAttempts, int target, Runnable onSuccess, Runnable onFail) {
        if (attempt >= maxAttempts) {
            notify("❌ Out of attempts! The correct number was " + target);
            onFail.run();
            return;
        }
    
        notify("🔢 Attempt " + (attempt + 1) + ":");
    
        userInputListener.requestInput("Enter your guess:", input -> {
            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                notify("⚠️ Invalid input. Please enter a number.");
                // Coba lagi dengan attempt yang sama
                attemptGuess(attempt, maxAttempts, target, onSuccess, onFail);
                return;
            }
    
            if (guess == target) {
                notify("✅ Correct! You caught the fish!");
                onSuccess.run();
            } else if (guess < target) {
                notify("📉 Too low!");
                attemptGuess(attempt + 1, maxAttempts, target, onSuccess, onFail);
            } else {
                notify("📈 Too high!");
                attemptGuess(attempt + 1, maxAttempts, target, onSuccess, onFail);
            }
        });
    }
    

    private void notify(String msg) {
        if (messageListener != null) {
            messageListener.onMessage(msg);
        } else {
            System.out.println(msg); // fallback CLI
        }
    }

}

