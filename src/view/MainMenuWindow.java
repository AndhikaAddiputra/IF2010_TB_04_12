package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Character;

import model.*;
import utility.MusicPlayer;
import utility.Season;
import utility.Weather;
import controller.*;

public class MainMenuWindow extends JFrame {
    private JTextField playerNameField;
    private JComboBox<String> genderCombo;
    private JTextField farmNameField;
    private MusicPlayer musicPlayer;

    public MainMenuWindow() {
        setTitle("Spakbor Hills - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon(getClass().getResource("/assets/main_menu_background.jpg")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // Change to BoxLayout
        setContentPane(mainPanel);

        // Center panel to hold logo and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);  // Make panel transparent

        // Add spacing at top
        centerPanel.add(Box.createVerticalGlue());

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/logo_spakbor_hills.png")));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JButton startButton = new JButton("Start");
        JButton exitButton = new JButton("Exit");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(e -> openInputDialog());
        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(exitButton);

        // Add spacing at bottom
        centerPanel.add(Box.createVerticalGlue());

        // Add center panel to main panel
        mainPanel.add(centerPanel);

        // Music
        musicPlayer = new MusicPlayer();
        musicPlayer.play("/assets/music/background_music.wav");

        // Add volume control
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setOpaque(false);
        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100f;
            musicPlayer.setVolume(volume);
        });
        controlPanel.add(new JLabel("Volume:"));
        controlPanel.add(volumeSlider);

        // Add to layout
        centerPanel.add(controlPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Stop music when window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (musicPlayer != null) {
                    musicPlayer.stop();
                }
            }
        });
    }

    private void openInputDialog() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Player Name:"));
        playerNameField = new JTextField();
        inputPanel.add(playerNameField);

        inputPanel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[]{"M", "F"});
        inputPanel.add(genderCombo);

        inputPanel.add(new JLabel("Farm Name:"));
        farmNameField = new JTextField();
        inputPanel.add(farmNameField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "New Game Setup", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String playerName = playerNameField.getText().trim();
            Character gender = ((String)genderCombo.getSelectedItem()).charAt(0);
            String farmName = farmNameField.getText().trim();

            if (playerName.isEmpty() || farmName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }

            startGame(playerName, gender, farmName);
        }
    }

    private void startGame(String playerName, Character gender, String farmName) {
        // Create initial game objects using player input
        FarmMap farmMap = new FarmMap();
        WorldMap worldMap = new WorldMap();
        Player player = new Player(playerName, gender, farmName);
        player.setPosition(new java.awt.Point(11, 10));
    
        // Add starter items to inventory
        player.getInventory().addItem(SeedsRegistry.getSeeds("Parnsnip Seeds"), 5);
        player.getInventory().addItem(CropRegistry.getCrop("Wheat"), 5);
        player.getInventory().addItem(FishRegistry.getFish("Salmon"), 3);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Baguette Recipe"), 1);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Pumpkin Pie Recipe"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Coal"), 5);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Hoe"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Watering Can"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Pickaxe"), 1);
    
        // Create game state and window
        GameState gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false);
        FarmWindow window = new FarmWindow(player, farmMap, gameState, worldMap);
    
        // Create and set controllers
        FarmActionController farmController = new FarmActionController(player, farmMap, gameState, window, window);
        FishingController fishingController = new FishingController(window, window);
        WorldActionController worldController = new WorldActionController(player, worldMap, gameState, window, window);
    
        window.setFarmController(farmController);
        window.setFishingController(fishingController);
        window.setWorldController(worldController);
    
        // Show game window
        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
        });
    
        dispose(); // Close main menu
    }
}

