package view;

import java.awt.*;
import java.awt.event.*;
import java.lang.Character;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.*;
import utility.MusicPlayer;
import utility.Season;
import utility.Weather;

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
        JButton creditButton = new JButton("Credits");
        startButton.setOpaque(true);
        exitButton.setOpaque(true);
        creditButton.setOpaque(true);
        startButton.setFocusPainted(false);
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(34, 139, 34)); // forest green
        startButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(10, 20, 10, 20)));

        exitButton.setFocusPainted(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(34, 139, 34)); // forest green
        exitButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(10, 20, 10, 20)));

        creditButton.setFocusPainted(false);
        creditButton.setForeground(Color.WHITE);
        creditButton.setBackground(new Color(34, 139, 34)); // forest green
        creditButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), new EmptyBorder(10, 20, 10, 20)));

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font customFont = loadCustomFont(20f);
        startButton.setFont(customFont);
        exitButton.setFont(customFont);
        creditButton.setFont(customFont);

        startButton.addActionListener(e -> openInputDialog());
        exitButton.addActionListener(e -> System.exit(0));

        startButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            startButton.setBackground(new Color(50, 205, 50)); // lighter green
            }
        @Override
        public void mouseExited(MouseEvent evt) {
            startButton.setBackground(new Color(34, 139, 34)); // normal
            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent evt) {
            exitButton.setBackground(new Color(50, 205, 50)); // lighter green
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            exitButton.setBackground(new Color(34, 139, 34)); // normal
            }
        });

        creditButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(MainMenuWindow.this, 
                    "Game developed by: Kelompok 12\n" +
                    "- Andhika M. Addiputra (Backend and Frontend)\n" +
                    "- Kevin Azra (Frontend)\n" +
                    "- Zheanetta Apple (Frontend)\n" +
                    "- Jason Samuel (Frontend)", 
                    "Credits", 
                    JOptionPane.INFORMATION_MESSAGE);    
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                creditButton.setBackground(new Color(50, 205, 50)); // lighter green
            }
            @Override
            public void mouseExited(MouseEvent e) {
                creditButton.setBackground(new Color(34, 139, 34)); // normal
            }
        });

        centerPanel.add(startButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(exitButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(creditButton);


        // Add spacing at bottom
        centerPanel.add(Box.createVerticalGlue());

        // Add center panel to main panel
        mainPanel.add(centerPanel);

        // Music
        musicPlayer = new MusicPlayer();
        musicPlayer.play("/assets/music/music_background.wav");

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

        validate();
        repaint();

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

    private Font loadCustomFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/SVBold.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size); // fallback
        }
    }

    private void openInputDialog() {
        // Create custom dialog
        JDialog dialog = new JDialog(this, "New Game Setup", true);
        dialog.setSize(500, 400); // Made dialog bigger to fit image
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(34, 139, 34)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Create Your Character");
        titleLabel.setFont(loadCustomFont(24f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Create a panel to hold inputs and preview image side by side
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        contentPanel.setOpaque(false);

        // Input fields panel
        JPanel inputsPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        inputsPanel.setOpaque(false);

        // Player preview panel
        JPanel previewPanel = new JPanel();
        previewPanel.setOpaque(false);
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        JLabel previewLabel = new JLabel();
        previewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Load initial male image
        ImageIcon maleIcon = new ImageIcon(getClass().getResource("/assets/player/player_male.png"));
        previewLabel.setIcon(maleIcon);
        
        // Player Name
        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(loadCustomFont(16f));
        playerNameField = new JTextField();
        playerNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleTextField(playerNameField);

        // Gender with preview update
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(loadCustomFont(16f));
        genderCombo = new JComboBox<>(new String[]{"M", "F"});
        genderCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleComboBox(genderCombo);
        
        // Update preview image when gender changes
        genderCombo.addActionListener(e -> {
            String gender = (String)genderCombo.getSelectedItem();
            String imagePath = "/assets/player/player_" + 
                            (gender.equals("M") ? "male" : "female") + ".png";
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            previewLabel.setIcon(icon);
        });

        // Farm Name
        JLabel farmLabel = new JLabel("Farm Name:");
        farmLabel.setFont(loadCustomFont(16f));
        farmNameField = new JTextField();
        farmNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        styleTextField(farmNameField);

        // Add components to inputs panel
        inputsPanel.add(nameLabel);
        inputsPanel.add(playerNameField);
        inputsPanel.add(genderLabel);
        inputsPanel.add(genderCombo);
        inputsPanel.add(farmLabel);
        inputsPanel.add(farmNameField);

        // Preview components
        previewPanel.add(new JLabel("Character Preview:"));
        previewPanel.add(Box.createVerticalStrut(10));
        previewPanel.add(previewLabel);

        // Content panel
        contentPanel.add(inputsPanel);
        contentPanel.add(previewPanel);
        
        mainPanel.add(contentPanel);
        mainPanel.add(Box.createVerticalStrut(20));
    
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
    
        JButton confirmButton = new JButton("Start Game");
        JButton cancelButton = new JButton("Cancel");
    
        // Style buttons
        styleButton(confirmButton, new Color(34, 139, 34));
        styleButton(cancelButton, new Color(169, 169, 169));
    
        confirmButton.addActionListener(e -> {
            String playerName = playerNameField.getText().trim();
            Character gender = ((String)genderCombo.getSelectedItem()).charAt(0);
            String farmName = farmNameField.getText().trim();
    
            if (playerName.isEmpty() || farmName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please fill in all fields!", 
                    "Missing Information", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            dialog.dispose();
            startGame(playerName, gender, farmName);
        });
    
        cancelButton.addActionListener(e -> dialog.dispose());
    
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);
    
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    // Helper methods for styling components
    private void styleTextField(JTextField textField) {
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(loadCustomFont(16f));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    private void startGame(String playerName, Character gender, String farmName) {
        // Create initial game objects using player input
        FarmMap farmMap = new FarmMap();
        WorldMap worldMap = new WorldMap();
        Player player = new Player(playerName, gender, farmName);
        player.setPosition(new java.awt.Point(11, 10));
    
        // Add starter items to inventory
        player.getInventory().addItem(SeedsRegistry.getSeeds("Wheat Seeds"), 5);
        player.getInventory().addItem(CropRegistry.getCrop("Wheat"), 5);
        player.getInventory().addItem(FishRegistry.getFish("Salmon"), 3);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Baguette Recipe"), 1);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Pumpkin Pie Recipe"), 1);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Wine Recipe"), 1);
        player.getInventory().addItem(RecipeRegistry.getRecipe("Spakbor Salad Recipe"), 1);
        player.getInventory().addItem(MiscRegistry.getMisc("Coal"), 5);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Hoe"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Watering Can"), 1);
        player.getInventory().addItem(EquipmentRegistry.getEquipment("Pickaxe"), 1);
    
        // Create game state and window
        GameState gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false, null);

        // Then create window with gameState
        FarmWindow window = new FarmWindow(player, farmMap, gameState, worldMap);
        
        // Update GameState with window as listener
        gameState = new GameState(Weather.SUNNY, Season.SPRING, farmMap, player, false, window);
        window.setGameState(gameState);
    
        // Show game window
        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);
        });
    
        dispose(); // Close main menu
    }
}