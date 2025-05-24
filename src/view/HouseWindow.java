package view;

import controller.*;
import java.awt.*;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.*;
import utility.*;

public class HouseWindow extends JFrame implements MessageListener, UserInputListener {
    private Player player;
    private GameState gameState;
    private FarmActionController farmController;
    private FarmMap farmMap;
    private WorldMap worldMap;
    private ControllerFactory controllerFactory;
    private CookingController cookingController;

    private JPanel actionPanel, buttonPanel;
    private JLabel timeLabel, weatherLabel, seasonLabel, goldLabel, energyLabel;
    private JTextArea messageBox;
    private JLabel centerImageLabel;

    public HouseWindow(Player player, GameState gameState, FarmActionController farmController, FarmMap farmMap, WorldMap worldMap) {
        this.player = player;
        this.gameState = gameState;
        this.farmController = farmController;
        this.farmMap = farmMap;
        this.worldMap = worldMap;

        this.controllerFactory = new ControllerFactory(player, gameState, farmMap, this, this);
        this.cookingController = controllerFactory.createCookingController();

        setTitle("Inside House - Spakbor Hills");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLayout(new BorderLayout());

        initUI();
        refreshAll();
    }
    

    private void initUI() {
        // Kanan atas: Status
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

        addButton("Sleep", () -> {
            farmController = new FarmActionController(
                player, 
                farmMap, 
                gameState,
                this,    // Pass MessageListener
                this     // Pass UserInputListener
            );
            farmController.sleep();
            refreshAll();
        });
        addButton("Cook", () -> cookingController.startCookingFlow());
        addButton("Watch Weather", () -> showMessage("Today's Weather: " + gameState.getWeather()));
        addButton("Exit House", this::exitHouse);

        // Tengah: Gambar rumah
        centerImageLabel = new JLabel();
        centerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerImageLabel.setIcon(new ImageIcon(getClass().getResource("/assets/player/homePanel.png"))); 

        // Kanan bawah: Message box
        messageBox = new JTextArea(10, 30);
        messageBox.setEditable(false);
        JScrollPane scroll = new JScrollPane(messageBox);

        // Assembly
        add(actionPanel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.EAST);
        add(centerImageLabel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
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
        timeLabel.setText("Time: " + gameState.getTime());
        weatherLabel.setText("Weather: " + gameState.getWeather());
        seasonLabel.setText("Season: " + gameState.getSeason());
        goldLabel.setText("Gold: " + player.getGold());
        energyLabel.setText("Energy: " + player.getEnergy());
    }

    public void showMessage(String msg) {
        messageBox.append(msg + "\n");
    }

    private void exitHouse() {
        player.setInsideHouse(false);
        dispose(); // Close house window
        SwingUtilities.invokeLater(() -> {
            FarmWindow gameWindow = new FarmWindow(player, farmMap, gameState, worldMap);
            gameWindow.setVisible(true);
        });
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
}

