package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Key extends SuperObject {
    public OBJ_Key() {
        // Constructor for the OBJ_Key class
        // Initialize the key object with default values
        name = "Key"; // Set the name of the key
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/image/object/key.png")); // Load the key image
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
}
