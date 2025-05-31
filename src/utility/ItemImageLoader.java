package utility;

import java.awt.Image;
import java.util.HashMap;  
import java.util.Map;
import javax.swing.*;

public class ItemImageLoader {
    private static final String ASSET_PATH = "assets/items/";
    private static final Map<String, ImageIcon> cache = new HashMap<>();

    public static ImageIcon getIcon(String itemName) {
        if (cache.containsKey(itemName)) {
            return cache.get(itemName);
        }

        ImageIcon icon;
        try {
            // Use getResource() to load from classpath instead of direct file path
            String path = "/assets/items/" + itemName + ".png";
            icon = new ImageIcon(ItemImageLoader.class.getResource(path));
            
            // Resize image
            Image image = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        } catch (Exception e) {
            // Create empty icon if image not found
            System.out.println("Could not load image for: " + itemName);
            icon = new ImageIcon();
        }
        cache.put(itemName, icon);
        return icon;
    }
}

