package view;

import controller.*;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.*;
import utility.MessageListener;
import utility.UserInputListener;

public class FishingSpotWindow extends JFrame implements MessageListener, UserInputListener {
    private final Player player;
    private final Location location;
    private final GameState gameState;
    private FishingController fishingController;
    private final JTextArea messageBox;

    private JPanel buttonPanel;

    public FishingSpotWindow(Player player, Location location, GameState gameState, FishingController fishingController) {
        this.player = player;
        this.location = location;
        this.gameState = gameState;
        this.fishingController = fishingController;

        setTitle("Fishing at " + location.getName());
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Action Panel (kiri)
        JPanel actionPanel = new JPanel();
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

        // Fix: Pass correct parameters to fish method
        addButton(actionPanel, "Fish!", () -> {
            if (player.getEnergy() < 5) {
                onMessage("You don't have enough energy to fishing!");
                return;
            }
            fishingController.fish(player, gameState);
        });
        addButton(actionPanel, "Back", this::exitFishing);

        JLabel fishingImage = new JLabel(new ImageIcon(getClass().getResource("/assets/fishing/" + location.getName() + ".png")));
        fishingImage.setHorizontalAlignment(SwingConstants.CENTER);

        messageBox = new JTextArea(10, 30);
        messageBox.setEditable(false);
        JScrollPane scroll = new JScrollPane(messageBox);

        add(actionPanel, BorderLayout.WEST);
        add(fishingImage, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(150, 30));
        btn.addActionListener(e -> action.run());
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonPanel.add(btn);
    }

    private void exitFishing() {
        dispose();
    }

    @Override
    public void onMessage(String message) {
        messageBox.append(message + "\n");
    }

    @Override
    public void requestInput(String prompt, Consumer<String> callback) {
        String input = JOptionPane.showInputDialog(this, prompt);
        if (input != null) callback.accept(input.trim());
    }
}