package view;

import controller.*;
import java.awt.*;
import javax.swing.*;
import model.*;

public class VisitPanel extends JFrame {
    private final Player player;
    private final GameState gameState;
    private final FarmMap farmMap;
    private final ControllerFactory controllerFactory;

    public VisitPanel(Player player, GameState gameState, FarmMap farmMap) {
        this.player = player;
        this.gameState = gameState;
        this.farmMap = farmMap;

        this.controllerFactory = new ControllerFactory(player, gameState, farmMap, this::showMessage, this::requestInput);

        setTitle("Visit Locations");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Choose a location to visit:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));

        addVisitButton(panel, "Mayor Tadi's House", new NPCBuilding("Mayor Tadi"));
        addVisitButton(panel, "Caroline's House", new NPCBuilding("Caroline"));
        addVisitButton(panel, "Perry's House", new NPCBuilding("Perry"));
        addVisitButton(panel, "Dasco's House", new NPCBuilding("Dasco"));
        addVisitButton(panel, "Abigail's House", new NPCBuilding("Abigail"));
        addVisitButton(panel, "Emily's Store", new Store("Emily"));
        addVisitButton(panel, "Forest River", new ForestRiver());
        addVisitButton(panel, "Mountain Lake", new MountainLake());
        addVisitButton(panel, "Ocean", new Ocean());

        add(panel);
    }

    private void addVisitButton(JPanel panel, String label, Location location) {
        JButton button = new JButton(label);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            gameState.setInWorldMap(true);
            gameState.setCurrentWorldLocation(location);
            showMessage("🧭 Visiting " + location.getName());
            player.reduceEnergy(10);

            // Open the corresponding window
            openLocationWindow(location);

            dispose(); // Close this VisitPanel
        });
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }

    private void openLocationWindow(Location location) {
        if (location instanceof NPCBuilding npc) {
            NPC npcLocation = npc.getNPC();
            if (npc != null) {
                NPCWindow npcWindow = new NPCWindow(player, npcLocation, gameState);
                npcWindow.setVisible(true);
            } else {
                showMessage("❌ Could not find NPC.");
            }
        } else if (location instanceof Store store) {
            StoreWindow storeWindow = new StoreWindow(player, store, gameState);
            storeWindow.setVisible(true);
        } else if (location instanceof ForestRiver || location instanceof MountainLake || location instanceof Ocean) {
            FishingSpotWindow fishingWindow = new FishingSpotWindow(player, location, gameState);
            fishingWindow.setVisible(true);
        } else {
            showMessage("🚫 No window implemented for this location.");
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private void requestInput(String prompt, java.util.function.Consumer<String> callback) {
        String input = JOptionPane.showInputDialog(this, prompt);
        if (input != null) {
            callback.accept(input);
        }
    }
}

