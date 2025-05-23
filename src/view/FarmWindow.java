package view;

import controller.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.*;
import utility.*;

public class FarmWindow extends JFrame implements MessageListener, UserInputListener{
    private FarmMap farmMap;
    private WorldMap worldMap;
    private Player player;
    private GameState gameState;
    private FarmActionController farmController;
    private FishingController fishingController;
    private WorldActionController worldController;
    private ControllerFactory controllerFactory;

    private JPanel farmPanel;
    private JPanel actionPanel, buttonPanel;
    private JLabel timeLabel, weatherLabel, seasonLabel, goldLabel, energyLabel;
    private JTextArea messageBox;

    public FarmWindow(Player player, FarmMap farmMap, GameState gameState, WorldMap worldMap) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameState = gameState;
        this.worldMap = worldMap;

        this.controllerFactory = new ControllerFactory(player, gameState, farmMap, this, this);
        this.farmController = controllerFactory.createFarmActionController();

        setTitle("Spakbor Hills");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(new BorderLayout());

        initUI();
        bindMovementKeys();
        refreshAll();
    }

    public void setFarmController(FarmActionController controller) {
        this.farmController = controller;
    }

    public void setWorldController(WorldActionController controller) {
        this.worldController = controller;
    }

    public void setFishingController(FishingController controller) {
        this.fishingController = controller;
    }

    private void initUI() {
        // Status Panel
        JPanel statusPanel = new JPanel(new GridLayout(5, 1));
        timeLabel = new JLabel();
        weatherLabel = new JLabel();
        seasonLabel = new JLabel();
        goldLabel = new JLabel();
        energyLabel = new JLabel();
        statusPanel.add(timeLabel);
        statusPanel.add(weatherLabel);
        statusPanel.add(seasonLabel);
        statusPanel.add(goldLabel);
        statusPanel.add(energyLabel);

        // Action Panel (Kiri)
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(Color.ORANGE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        titlePanel.setOpaque(false);
        JTextPane textPane = new JTextPane();
        textPane.setText("Actions Panel");
        textPane.setEditable(false);
        textPane.setOpaque(false);
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, true);
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
        titlePanel.add(textPane);
        actionPanel.add(titlePanel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Action Buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        actionPanel.add(buttonPanel);

        actionPanel.add(Box.createVerticalGlue());

        addButton("Till", () -> {
            farmController.till();
        });
        addButton("Plant", () -> {
            String seedName = JOptionPane.showInputDialog(this, "Enter seed name:");
            if (seedName != null && !seedName.trim().isEmpty()) {
                farmController.plant(seedName);
            } else {
                showMessage("Invalid seed name.");
            }
        });
        addButton("Water", () -> {
            farmController.water();
        });
        addButton("Harvest", () -> {
            farmController.harvest();
        });
        addButton("Recover Land", () -> {
            farmController.recoverLand();
        });

        addButton("Open Inventory", this::openInventory);

        addButton("Enter House", () -> {
            if (!farmMap.isNearHouse(player.getPosition())) {
                showMessage("🚫 You must be next to the house to enter.");
                return;
            }
        
            player.setInsideHouse(true);
            dispose(); // Tutup GameWindow
        
            SwingUtilities.invokeLater(() -> {
                HouseWindow houseWindow = new HouseWindow(player, gameState, farmController, farmMap, worldMap);
                houseWindow.setVisible(true);
            });
        });

        addButton("Eat", () -> {
            String itemName = JOptionPane.showInputDialog(this, "Enter food name:");
            if (itemName != null && !itemName.trim().isEmpty()) {
                farmController.eat(itemName);
            } else {
                showMessage("Invalid food name.");
            }
        });

        addButton("Sell", () -> {
            farmController.sell();
        });
        
        addButton("Fishing", () -> {
            if (!farmMap.isNearPond(player.getPosition())) {
                showMessage("❌ You must be near the pond to fish here.");
            } else {
                fishingController.fish(player, gameState);
            }
        });

        addButton("Visit", () -> {
            worldController.visit();
        });

        // Map Panel (Tengah)
        final int MAP_HEIGHT = 32;
        final int MAP_WIDTH = 32;
        farmPanel = new JPanel(new GridLayout(MAP_HEIGHT, MAP_WIDTH));

        // 👉 Message Box (Kanan Bawah)
        messageBox = new JTextArea(10, 30);
        messageBox.setEditable(false);
        JScrollPane messageScroll = new JScrollPane(messageBox);

        // Layout Assembly
        add(actionPanel, BorderLayout.WEST);
        add(farmPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.EAST);
        add(messageScroll, BorderLayout.SOUTH);
    }

    private void addButton(String label, Runnable action) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(150, 30));
        btn.addActionListener(e -> action.run());
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonPanel.add(btn);
    }

    public void refreshAll() {
        // Update Status
        timeLabel.setText("Time: " + gameState.getTime());
        weatherLabel.setText("Weather: " + gameState.getWeather());
        seasonLabel.setText("Season: " + gameState.getSeason());
        goldLabel.setText("Gold: " + player.getGold());
        energyLabel.setText("Energy: " + player.getEnergy());

        // Update Map
        final int MAP_HEIGHT = 32;
        final int MAP_WIDTH = 32;
        farmPanel.removeAll();
        for (int y = 0; y < FarmMap.HEIGHT; y++) {
            for (int x = 0; x < FarmMap.WIDTH; x++) {
                Tile tile = farmMap.getTileAt(new Point(x, y));
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
        
                if (player.getPosition().equals(new Point(x, y))) {
                    label.setIcon(new ImageIcon(getClass().getResource("/assets/player/player_male.png"))); 
                } else {
                    label.setIcon(getTileIcon(tile)); 
                }
        
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                farmPanel.add(label);
            }
        }
    }

    private ImageIcon getTileIcon(Tile tile) {
        return switch (tile.getType()) {
            case HOUSE -> new ImageIcon(getClass().getResource("/assets/tiles/house.png"));
            case POND -> new ImageIcon(getClass().getResource("/assets/tiles/pond.png"));
            case SHIPPING_BIN -> new ImageIcon(getClass().getResource("/assets/tiles/shipping_bin.png"));
            case TILLABLE -> new ImageIcon(getClass().getResource("/assets/tiles/tillable.png"));
            case TILLED -> new ImageIcon(getClass().getResource("/assets/tiles/tilled.png"));
            case PLANTED -> new ImageIcon(getClass().getResource("/assets/tiles/planted.png"));
            default -> null; // Replace with an appropriate icon or null if no icon is needed
        };
    }

    public void showMessage(String message) {
        messageBox.append(message + "\n");
    }

    @Override
    public void onMessage(String message) {
        messageBox.append(message + "\n");
    }

    @Override
    public void requestInput(String prompt, Consumer<String> callback) {
        String input = JOptionPane.showInputDialog(this, prompt);
        if (input != null && !input.trim().isEmpty()) {
            callback.accept(input.trim());
        } else {
            System.out.print(prompt + " ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            callback.accept(userInput);
        }
    }

    private void bindMovementKeys() {
        InputMap inputMap = farmPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = farmPanel.getActionMap();
    
        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
    
        actionMap.put("moveUp", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                farmController.move(Direction.UP);
                refreshAll();
            }
        });
    
        actionMap.put("moveDown", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                farmController.move(Direction.DOWN);
                refreshAll();
            }
        });
    
        actionMap.put("moveLeft", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                farmController.move(Direction.LEFT);
                refreshAll();
            }
        });
    
        actionMap.put("moveRight", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                farmController.move(Direction.RIGHT);
                refreshAll();
            }
        });
    }

    private void openInventory() {
        JFrame inventoryFrame = new JFrame("Inventory");
        inventoryFrame.setSize(400, 500);
        inventoryFrame.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 3, 10, 10));

        Map<String, InventoryEntry> items = player.getInventory().getItems();

        for (InventoryEntry entry : items.values()) {
            Item item = entry.getItem();
            int quantity = entry.getQuantity();

            JPanel slot = new JPanel(new BorderLayout());
            JLabel iconLabel = new JLabel(ItemImageLoader.getIcon(item.getItemName()));
            JLabel qtyLabel = new JLabel("x" + quantity, SwingConstants.CENTER);
            JLabel itemNameLabel = new JLabel(item.getItemName(), SwingConstants.CENTER);

            slot.add(iconLabel, BorderLayout.CENTER);
            slot.add(qtyLabel, BorderLayout.SOUTH);
            slot.add(itemNameLabel, BorderLayout.NORTH);
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            slot.setBackground(Color.ORANGE);

            slot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent  e) {
                    showItemDetail(item);
                }
            });
            itemPanel.add(slot);
        }

        JScrollPane scrollPane = new JScrollPane(itemPanel);
        inventoryFrame.add(scrollPane, BorderLayout.CENTER);

        inventoryFrame.setLocationRelativeTo(this);
        inventoryFrame.setVisible(true);
    }

    private void showItemDetail(Item item) {
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setPreferredSize(new Dimension(400, 150));
    
        // KIRI: Icon
        JLabel iconLabel = new JLabel(ItemImageLoader.getIcon(item.getItemName()));
        detailPanel.add(iconLabel, BorderLayout.WEST);
    
        // KANAN: Informasi detail (multi-line label)
        JTextArea detailText = new JTextArea();
        detailText.setEditable(false);
        detailText.setOpaque(false);
        detailText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
        StringBuilder detail = new StringBuilder();
        detail.append("📦 Item: ").append(item.getItemName()).append("\n\n");
    
        if (item instanceof Seeds seed) {
            detail.append("🌱 Type: Seeds\n");
            detail.append("Days to Harvest: ").append(seed.getDaysToHarvest()).append("\n");
            detail.append("Season: ").append(seed.getSeason()).append("\n");
            detail.append("Buy Price: ").append(seed.getBuyPrice()).append("\n");
        } else if (item instanceof Crop crop) {
            detail.append("🌾 Type: Crop\n");
            detail.append("Sell Price: ").append(crop.getSellPrice()).append("\n");
            detail.append("From Seed: ").append(crop.getSourceSeedName()).append("\n");
        } else if (item instanceof Fish fish) {
            detail.append("🐟 Type: Fish (").append(fish.getType()).append(")\n");
            detail.append("Season: ").append(fish.getSeason()).append("\n");
            detail.append("Weather: ").append(fish.getWeather()).append("\n");
            detail.append("Available At: ").append(fish.getLocation()).append("\n");
            detail.append("Sell Price: ").append(fish.getPrice()).append("\n");
        } else if (item instanceof Food food) {
            detail.append("🍽️ Type: Food\n");
            detail.append("Sell Price: ").append(food.getSellPrice()).append("\n");
            detail.append("Buy Price: ").append(food.getBuyPrice()).append("\n");
            detail.append("Energy Gained: ").append(food.getAddEnergy()).append("\n");
        } else if (item instanceof MiscItem misc) {
            detail.append("🗃️ Type: Miscellaneous\n");
            detail.append("Description: ").append(misc.getDescription()).append("\n");
        } else if (item instanceof Equipment equipment) {
            detail.append("⚔️ Type: Equipment\n");
            detail.append("Buy Price: ").append(equipment.getBuyPrice()).append("\n");
            detail.append("Sell Price: ").append(equipment.getSellPrice()).append("\n");
            detail.append("Support for Actions: ").append(equipment.getWhatAction()).append("\n");
        }
    
        detail.append("Edible: ").append(item.isEdible() ? "Yes" : "No");
    
        detailText.setText(detail.toString());
        detailPanel.add(detailText, BorderLayout.CENTER);
    
        // TAMPILKAN POPUP
        JOptionPane.showMessageDialog(this, detailPanel, item.getItemName(), JOptionPane.PLAIN_MESSAGE);
    }
    
    
}
