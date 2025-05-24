package view;

import controller.*;
import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.*;
import utility.*;

public class NPCWindow extends JFrame implements MessageListener, UserInputListener {
    private final Player player;
    private final NPC npc;
    private final GameState gameState;
    private final ControllerFactory controllerFactory;
    private final WorldActionController worldController;
    private final JTextArea messageBox;

    private JPanel buttonPanel;

    public NPCWindow(Player player, NPC npc, GameState gameState) {
        this.player = player;
        this.npc = npc;
        this.gameState = gameState;
        this.controllerFactory = new ControllerFactory(player, gameState, gameState.getFarmMap(), this, this);
        this.worldController = controllerFactory.createWorldActionController();

        setTitle("Visit " + npc.getName());
        setSize(800, 800);
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

        addButton(actionPanel, "Chat", () -> worldController.chat(npc.getName()));
        addButton(actionPanel, "Gift", () -> {
            String itemName = JOptionPane.showInputDialog(this, "Enter Item name:");
            if (itemName != null && !itemName.trim().isEmpty()) {
                worldController.gift(npc.getName(), itemName.trim());
            } else {
                showMessage("Invalid food name.");
            }
        });

        addButton(actionPanel, "Propose", () -> worldController.propose(npc.getName()));
        addButton(actionPanel, "Marry", () -> worldController.marry(npc.getName()));
        addButton(actionPanel, "Back", this::exitNPC);

        // NPC Image (Tengah)
        JLabel npcHouseImage = new JLabel(new ImageIcon(getClass().getResource("/assets/npc/" + npc.getName() + " House.png")));
        npcHouseImage.setHorizontalAlignment(SwingConstants.CENTER);

        // Message Box (Bawah)
        messageBox = new JTextArea(5, 30);
        messageBox.setEditable(false);
        JScrollPane scroll = new JScrollPane(messageBox);

        // NPC Image
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/npc/" + npc.getName() + ".png"));
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel npcImage = new JLabel(new ImageIcon(scaled));
        //npcImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // NPC Info Panel (Kanan)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(npc.getName() + " Info"));
        infoPanel.setPreferredSize(new Dimension(250, 600));

        JLabel heartLabel = new JLabel("❤️ Heart Points: " + npc.getHeartPoints());
        infoPanel.add(heartLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        infoPanel.add(new JLabel("💖 Loved Items:"));
        infoPanel.add(makeItemList(npc.getLovedItems()));
        infoPanel.add(new JLabel("👍 Liked Items:"));
        infoPanel.add(makeItemList(npc.getLikedItems()));
        infoPanel.add(new JLabel("💔 Hated Items:"));
        infoPanel.add(makeItemList(npc.getHateItems()));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(npcImage);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(infoPanel);
        rightPanel.setPreferredSize(new Dimension(300, 600));

        add(actionPanel, BorderLayout.WEST);
        add(npcHouseImage, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(scroll, BorderLayout.SOUTH);
    }

    private JPanel makeItemList(Set<String> items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        if (items.isEmpty()) {
            panel.add(new JLabel("-"));
        } else {
            for (String item : items) {
                panel.add(new JLabel("- " + item));
            }
        }
        return panel;
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(150, 30));
        btn.addActionListener(e -> action.run());
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonPanel.add(btn);
    }

    private void exitNPC() {
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

    public void showMessage(String message) {
        messageBox.append(message + "\n");
    }
}

