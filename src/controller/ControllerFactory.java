package controller;

import model.*;
import utility.*;
import view.*;

public class ControllerFactory {
    private final Player player;
    private final GameState gameState;
    private final FarmMap farmMap;
    private final WorldMap worldMap = new WorldMap(); // Assuming WorldMap is needed for WorldActionController
    private final MessageListener messageListener;
    private final UserInputListener inputListener;

    public ControllerFactory(Player player, GameState gameState, FarmMap farmMap,
                             MessageListener messageListener, UserInputListener inputListener) {
        this.player = player;
        this.gameState = gameState;
        this.farmMap = farmMap;
        this.messageListener = messageListener;
        this.inputListener = inputListener;
    }

    public FarmActionController createFarmActionController() {
        return new FarmActionController(player, farmMap, gameState, messageListener, inputListener);
    }

    public CookingController createCookingController() {
        return new CookingController(player, gameState, farmMap, messageListener, inputListener);
    }

    public FishingController createFishingController() {
        return new FishingController(messageListener, inputListener);
    }
    
    public WorldActionController createWorldActionController() {
        return new WorldActionController(player, worldMap, gameState, messageListener, inputListener);
    }

    // Tambahkan controller lainnya sesuai kebutuhan
}

