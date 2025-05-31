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
    private JLabel timeLabel, weatherLabel, seasonLabel, goldLabel, energyLabel, nameLabel;
    private JTextArea messageBox;

    public FarmWindow(Player player, FarmMap farmMap, GameState gameState, WorldMap worldMap) {
        this.player = player;
        this.farmMap = farmMap;
        this.gameState = gameState;
        this.worldMap = worldMap;

        this.controllerFactory = new ControllerFactory(player, gameState, farmMap, this, this);

        this.farmController = controllerFactory.createFarmActionController();
        this.worldController = controllerFactory.createWorldActionController();
        this.fishingController = worldController.getFishingController();

        setTitle("Spakbor Hills");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(875, 725);
        setLayout(new BorderLayout());

        initUI();
        bindMovementKeys();
        refreshAll();
    }

    /*
    public void setFarmController(FarmActionController controller) {
        this.farmController = controller;
    }

    public void setWorldController(WorldActionController controller) {
        this.worldController = controller;
    }

    public void setFishingController(FishingController controller) {
        this.fishingController = controller;
    }
    */

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        refreshAll(); // Refresh UI with new GameState
    }

    private void initUI() {
        // Status and Player Panel
        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Farm Status Section (Top)
        JPanel farmStatusPanel = new JPanel();
        farmStatusPanel.setLayout(new BoxLayout(farmStatusPanel, BoxLayout.Y_AXIS));
        farmStatusPanel.setBorder(BorderFactory.createTitledBorder("Farm Status"));
        
        timeLabel = new JLabel();
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        weatherLabel = new JLabel();
        weatherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        seasonLabel = new JLabel();
        seasonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        farmStatusPanel.add(timeLabel);
        farmStatusPanel.add(Box.createVerticalStrut(5));
        farmStatusPanel.add(weatherLabel);
        farmStatusPanel.add(Box.createVerticalStrut(5));
        farmStatusPanel.add(seasonLabel);

        // Player Info Section (Bottom)
        JPanel playerInfoPanel = new JPanel(new BorderLayout(5, 5));
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Player Info"));
        
        // Add player image on left side
        String playerImagePath = "/assets/player/player_" + 
                            (player.getGender() == 'M' ? "male" : "female") + ".png";
        ImageIcon playerIcon = new ImageIcon(getClass().getResource(playerImagePath));
        Image scaled = playerIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        JLabel playerImage = new JLabel(new ImageIcon(scaled));
        playerImage.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        playerInfoPanel.add(playerImage, BorderLayout.WEST);
        
        // Player stats on right side
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        
        nameLabel = new JLabel("Name: " + player.getName());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        goldLabel = new JLabel();
        goldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        energyLabel = new JLabel();
        energyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statsPanel.add(nameLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(goldLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(energyLabel);
        statsPanel.add(Box.createVerticalStrut(10));

        // More Info Button
        JButton moreInfoButton = new JButton("More Info");
        moreInfoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        moreInfoButton.addActionListener(e -> showPlayerDetails());
        statsPanel.add(moreInfoButton);
        
        playerInfoPanel.add(statsPanel, BorderLayout.CENTER);

        // Playing Control Section (Center)
        JPanel playingControlPanel = new JPanel();
        playingControlPanel.setLayout(new BoxLayout(playingControlPanel, BoxLayout.Y_AXIS));
        playingControlPanel.setBorder(BorderFactory.createTitledBorder("Playing Control"));

        // Control Buttons Panel
        JPanel controlButtonsPanel = new JPanel();
        controlButtonsPanel.setLayout(new BoxLayout(controlButtonsPanel, BoxLayout.Y_AXIS));
        controlButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Help Button
        JButton helpButton = new JButton("Help");
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setMaximumSize(new Dimension(150, 30));
        helpButton.addActionListener(e -> showHelp());
        controlButtonsPanel.add(helpButton);
        controlButtonsPanel.add(Box.createVerticalStrut(5));

        // Statistics Button
        JButton statsButton = new JButton("Statistics");
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.setMaximumSize(new Dimension(150, 30));
        statsButton.addActionListener(e -> showStatistics());
        controlButtonsPanel.add(statsButton);
        controlButtonsPanel.add(Box.createVerticalStrut(5));

        // Fish Available Button 
        JButton fishButton = new JButton("Fish Available");
        fishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fishButton.setMaximumSize(new Dimension(150, 30));
        fishButton.addActionListener(e -> showAvailableFish());
        controlButtonsPanel.add(fishButton);
        controlButtonsPanel.add(Box.createVerticalStrut(5));

        // Seeds Available Button
        JButton seedsButton = new JButton("Seeds Available");
        seedsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        seedsButton.setMaximumSize(new Dimension(150, 30));
        seedsButton.addActionListener(e -> showAvailableSeeds());
        controlButtonsPanel.add(seedsButton);

        playingControlPanel.add(controlButtonsPanel);
        statusPanel.add(playingControlPanel, BorderLayout.CENTER);

        // Add sections to status panel
        statusPanel.add(farmStatusPanel, BorderLayout.NORTH);
        statusPanel.add(playerInfoPanel, BorderLayout.SOUTH);

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
            if (gameState.getTime().getHour() >= 22) {
                showMessage("🚫 It's too late to visit anyone.");
                return;
            }
            openVisitPanel();
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
                    // Use appropriate player sprite based on gender
                    String playerImagePath = "/assets/player/player_" + 
                        (player.getGender() == 'M' ? "male" : "female") + ".png";
                    label.setIcon(new ImageIcon(getClass().getResource(playerImagePath))); 
                } else {
                    label.setIcon(getTileIcon(tile)); 
                }
        
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
        detailPanel.setPreferredSize(new Dimension(400, 200)); // Made taller for button
        
        // Left: Icon
        JLabel iconLabel = new JLabel(ItemImageLoader.getIcon(item.getItemName()));
        detailPanel.add(iconLabel, BorderLayout.WEST);
        
        // Center: Item details
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTextArea detailText = new JTextArea();
        detailText.setEditable(false);
        detailText.setOpaque(false);
        detailText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        StringBuilder detail = new StringBuilder();
        detail.append("Item: ").append(item.getItemName()).append("\n\n");
    
        if (item instanceof Seeds seed) {
            detail.append("Type: Seeds\n");
            detail.append("Days to Harvest: ").append(seed.getDaysToHarvest()).append("\n");
            detail.append("Season: ").append(seed.getSeason()).append("\n");
            detail.append("Buy Price: ").append(seed.getBuyPrice()).append("\n");
        } else if (item instanceof Crop crop) {
            detail.append("Type: Crop\n");
            detail.append("Sell Price: ").append(crop.getSellPrice()).append("\n");
            detail.append("From Seed: ").append(crop.getSourceSeedName()).append("\n");
        } else if (item instanceof Fish fish) {
            detail.append("Type: Fish (").append(fish.getType()).append(")\n");
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
            detail.append("Type: Miscellaneous\n");
            detail.append("Description: ").append(misc.getDescription()).append("\n");
        } else if (item instanceof Equipment equipment) {
            detail.append("Type: Equipment\n");
            detail.append("Buy Price: ").append(equipment.getBuyPrice()).append("\n");
            detail.append("Sell Price: ").append(equipment.getSellPrice()).append("\n");
            detail.append("Support for Actions: ").append(equipment.getWhatAction()).append("\n");
        }
    
        detail.append("Edible: ").append(item.isEdible() ? "Yes" : "No");
    
        detailText.setText(detail.toString());
        centerPanel.add(detailText, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton favoriteButton = new JButton("Add to Favorites ⭐");
        favoriteButton.addActionListener(e -> {
            // Check if item is already in favorites
            boolean isAlreadyFavorite = false;
            for (Item favItem : player.getFavoriteItems()) {
                if (favItem != null && favItem.getItemName().equals(item.getItemName())) {
                    isAlreadyFavorite = true;
                    break;
                }
            }
            
            if (isAlreadyFavorite) {
                JOptionPane.showMessageDialog(
                    this,
                    "This item is already in your favorites!",
                    "Already Favorited",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                player.addFavoriteItem(item);
                JOptionPane.showMessageDialog(
                    this,
                    "Added " + item.getItemName() + " to favorites!",
                    "Added to Favorites",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        buttonPanel.add(favoriteButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        detailPanel.add(centerPanel, BorderLayout.CENTER);
    
        // TAMPILKAN POPUP
        JOptionPane.showMessageDialog(this, detailPanel, item.getItemName(), JOptionPane.PLAIN_MESSAGE);
    }

    private void openVisitPanel() {
        Point pos = player.getPosition();
        if (pos.x < 31 && pos.y < 31 && pos.x > 0 && pos.y > 0) {
            showMessage("❌ You must be at the edge of your farm to leave.");
            return;
        }

        VisitPanel visitPanel = new VisitPanel(player, gameState, farmMap);
        visitPanel.setVisible(true);
    }

    private void showPlayerDetails() {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(player.getName()).append("\n");
        details.append("Farm Name: ").append(player.getFarmName()).append("\n");
        details.append("Energy: ").append(player.getEnergy()).append("\n");
        details.append("Gold: ").append(player.getGold()).append("\n");
        details.append("Gender: ").append(player.getGender()).append("\n");
        
        details.append("Partner: ");
        if (player.getPartner().isEmpty()) {
            details.append("-\n");
        } else {
            player.getPartner().forEach((npc, status) -> 
                details.append(npc.getName())
                      .append(" (").append(status).append(")\n"));
        }
    
        details.append("\nFavorite Items:\n");
        Item[] favoriteItems = player.getFavoriteItems();
        boolean hasItems = false;
        for (Item item : favoriteItems) {
            if (item != null) {
                hasItems = true;
                String itemType = "";
                
                details.append(itemType).append(" ")
                    .append(item.getItemName()).append("\n");
            }
        }
        if (!hasItems) {
            details.append("None\n");
        }
        
    
        JTextArea detailText = new JTextArea(details.toString());
        detailText.setEditable(false);
        detailText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailText.setBackground(new Color(245, 245, 220));
        
        detailPanel.add(new JScrollPane(detailText));
    
        JOptionPane.showMessageDialog(
            this,
            detailPanel,
            "Player Details",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showHelp() {
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        StringBuilder helpText = new StringBuilder();
        helpText.append("🎮 Welcome to Spakbor Hills!\n\n");
        helpText.append("Game Controls:\n");
        helpText.append("• Movement: W,A,S,D keys\n");
        helpText.append("• Till Land: Stand on tillable land and click 'Till'\n");
        helpText.append("• Plant: Stand on tilled land and click 'Plant'\n");
        helpText.append("• Water: Stand on planted land and click 'Water'\n");
        helpText.append("• Harvest: Stand on ready crops and click 'Harvest'\n");
        helpText.append("\nSocial Activities:\n");
        helpText.append("• Visit NPCs between 6:00 and 22:00\n");
        helpText.append("• Chat with NPCs to build relationship\n");
        helpText.append("• Give gifts to increase heart points\n");
        helpText.append("• Propose when relationship is strong enough\n");
        helpText.append("\nOther Activities:\n");
        helpText.append("• Fish at ponds or water bodies\n");
        helpText.append("• Cook food in your house\n");
        helpText.append("• Sell items at the shipping bin\n");
        helpText.append("• Sleep to restore energy\n");
        
        JTextArea textArea = new JTextArea(helpText.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 220));
        
        helpPanel.add(new JScrollPane(textArea));
    
        JOptionPane.showMessageDialog(
            this,
            helpPanel,
            "Game Help",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showStatistics() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        StringBuilder stats = new StringBuilder();
        
        // Financial Statistics
        int totalIncome = farmController.getGoldIncome();
        int totalExpenditure = worldController.getGoldExpenditure();
        int totalSeasons = Math.max(1, gameState.getTotalSeason());
        float totalIncomePerSeason = (float) totalIncome / totalSeasons;
        float totalExpenditurePerSeason = (float) totalExpenditure / totalSeasons;
        
        stats.append("💰 Financial Statistics\n");
        stats.append("Total Income: ").append(totalIncome).append(" gold\n");
        stats.append("Total Expenditure: ").append(totalExpenditure).append(" gold\n");
        stats.append("Average Season Income: ").append(totalIncomePerSeason).append(" gold\n");
        stats.append("Average Season Expenditure: ").append(totalExpenditurePerSeason).append(" gold\n\n");
    
        // Game Progress
        stats.append("⏳ Game Progress\n");
        stats.append("Total Days Played: ").append(gameState.getTotalDay()).append("\n\n");
    
        // NPC Relationships
        stats.append("👥 NPC Relationships\n");
        for (NPC npc : NPCRegistry.getAll()) {
            stats.append("\n").append(npc.getName()).append(":\n");
            String status = player.getPartner().containsKey(npc) ? 
                        player.getPartner().get(npc) : "-";
            stats.append("• Status: ").append(status).append("\n");
        }
        stats.append("Chatting frequency: ").append(worldController.getFreqChatting()).append("\n");
        stats.append("Gifting frequency: ").append(worldController.getFreqGifting()).append("\n");
        stats.append("Visit NPC frequency: ").append(worldController.getVisitNPCcount()).append("\n\n");
        
        // Farming Statistics
        stats.append("\n🌾 Farming Statistics\n");
        stats.append("Total Crops Harvested: ").append(farmController.getCropsHarvested()).append("\n\n");
    
        // Fishing Statistics
        stats.append("🎣 Fishing Statistics\n");
        stats.append("Total Fish Caught: ").append(fishingController.getFishCaught()).append("\n");
        stats.append("• Common Fish: ").append(fishingController.getFishCaughtCommon()).append("\n");
        stats.append("• Regular Fish: ").append(fishingController.getFishCaughtRegular()).append("\n");
        stats.append("• Legendary Fish: ").append(fishingController.getFishCaughtLegendary()).append("\n");
    
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 220));
        
        statsPanel.add(new JScrollPane(textArea));
    
        JOptionPane.showMessageDialog(
            this,
            statsPanel,
            "Game Statistics",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showAvailableFish() {
        JPanel fishPanel = new JPanel();
        fishPanel.setLayout(new BoxLayout(fishPanel, BoxLayout.Y_AXIS));
        fishPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        StringBuilder fishInfo = new StringBuilder();
        fishInfo.append("🎣 Fish Available This Season (" + gameState.getSeason() + "):\n\n");
        
        for (Fish fish : FishRegistry.getAllFish()) {
            if (fish.getSeason().contains(gameState.getSeason())) {
                Range<Time> timeRange = fish.getAvailableTime().iterator().next(); 
                Time startTime = timeRange.getStart();
                Time endTime = timeRange.getEnd();  
                fishInfo.append("• ").append(fish.getItemName())
                       .append(" (").append(fish.getType()).append(")\n");
                fishInfo.append("  Time: ").append(startTime.formatTimeRange(endTime)).append("\n");
                fishInfo.append("  Weather: ").append(fish.getWeather()).append("\n");
                fishInfo.append("  Location: ").append(fish.getLocation()).append("\n");
                fishInfo.append("  Price: ").append(fish.getPrice()).append(" gold\n\n");
            }
        }
        
        JTextArea textArea = new JTextArea(fishInfo.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 220));
        
        fishPanel.add(new JScrollPane(textArea));
        
        JOptionPane.showMessageDialog(
            this,
            fishPanel,
            "Available Fish",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showAvailableSeeds() {
        JPanel seedsPanel = new JPanel();
        seedsPanel.setLayout(new BoxLayout(seedsPanel, BoxLayout.Y_AXIS));
        seedsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        StringBuilder seedsInfo = new StringBuilder();
        seedsInfo.append("🌱 Seeds Available This Season (" + gameState.getSeason() + "):\n\n");
        
        for (Map.Entry<String, Seeds> entry : SeedsRegistry.getSeedsMap().entrySet()) {
            Seeds seed = entry.getValue();
            if (seed.getSeason().contains(gameState.getSeason())) {
                Crop crop = CropRegistry.getCropFromSeedName(seed.getItemName());
                seedsInfo.append("• ").append(seed.getItemName()).append("\n");
                seedsInfo.append("  Days to Harvest: ").append(seed.getDaysToHarvest()).append("\n");
                seedsInfo.append("  Buy Price: ").append(seed.getBuyPrice()).append(" gold\n");
                if (crop != null) {
                    seedsInfo.append("  Sell Price (crop): ").append(crop.getSellPrice()).append(" gold\n");
                    seedsInfo.append("  Units per Harvest: ").append(crop.getUnitPerHarvest()).append("\n");
                }
                seedsInfo.append("\n");
            }
        }
        
        JTextArea textArea = new JTextArea(seedsInfo.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 220));
        
        seedsPanel.add(new JScrollPane(textArea));
        
        JOptionPane.showMessageDialog(
            this,
            seedsPanel,
            "Available Seeds",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
}
