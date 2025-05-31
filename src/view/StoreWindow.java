package view;

import controller.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.*;
import utility.ItemImageLoader;
import utility.MessageListener;
import utility.UserInputListener;

public class StoreWindow extends JFrame implements MessageListener, UserInputListener {
    private final Player player;
    private final Store store;
    private final GameState gameState;
    private final ControllerFactory controllerFactory;
    private final WorldActionController worldController;
    private final JTextArea messageBox;
    private final NPC npc = NPCRegistry.get("Emily");
    
    private JPanel buttonPanel;

    public StoreWindow(Player player, Store store, GameState gameState) {
        this.player = player;
        this.store = store;
        this.gameState = gameState;
        this.controllerFactory = new ControllerFactory(player, gameState, gameState.getFarmMap(), this, this);
        this.worldController = controllerFactory.createWorldActionController();

        setTitle("Store - " + store.getName());
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

        addButton(actionPanel, "Buy Items", this::openStore);
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
        addButton(actionPanel, "Back", this::exitStore);

        // Store Image (Tengah)
        JLabel storeImage = new JLabel(new ImageIcon(getClass().getResource("/assets/npc/Store.png")));
        storeImage.setHorizontalAlignment(SwingConstants.CENTER);

        // Kanan bawah: Message box
        messageBox = new JTextArea(10, 30);
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
        add(storeImage, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
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

    private void exitStore() {
        dispose();
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

    private void openStore() {
        JFrame storeFrame = new JFrame("Store Inventory");
        storeFrame.setSize(500, 600);
        storeFrame.setLayout(new BorderLayout());

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 3, 10, 10));

        Store currentStore = (Store) gameState.getCurrentWorldLocation();
        Map<String, InventoryEntry> items = currentStore.getInventory().getItems();

        for (InventoryEntry entry : items.values()) {
            Item item = entry.getItem();
            int quantity = entry.getQuantity();
            int price = Store.getBuyPrice(item.getItemName());

            JPanel slot = new JPanel(new BorderLayout());
            JLabel iconLabel = new JLabel(ItemImageLoader.getIcon(item.getItemName()));
            JLabel priceLabel = new JLabel(price + "g", SwingConstants.CENTER);
            JLabel itemNameLabel = new JLabel(item.getItemName(), SwingConstants.CENTER);

            slot.add(iconLabel, BorderLayout.CENTER);
            slot.add(priceLabel, BorderLayout.SOUTH);
            slot.add(itemNameLabel, BorderLayout.NORTH);
            slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            slot.setBackground(new Color(255, 230, 200)); // warna khas store

            slot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String input = JOptionPane.showInputDialog(storeFrame, "Enter quantity to buy for " + item.getItemName() + ":");
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            int qty = Integer.parseInt(input.trim());
                            if (qty <= 0) {
                                JOptionPane.showMessageDialog(storeFrame, "❌ Invalid quantity!");
                                return;
                            }
                            worldController.buy(item.getItemName(), qty);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(storeFrame, "❌ Invalid number format!");
                        }
                    }
                }
            });

            itemPanel.add(slot);
        }

        JScrollPane scrollPane = new JScrollPane(itemPanel);
        storeFrame.add(scrollPane, BorderLayout.CENTER);

        storeFrame.setLocationRelativeTo(this);
        storeFrame.setVisible(true);
    }

}

