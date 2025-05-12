package object;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends SuperObject {
    public OBJ_Door() {
        // Constructor for the OBJ_Door class
        name = "Door"; // Set the name of the door
        collision = true; // Set collision to true for the door
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/image/object/door.png")); // Load the door image
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
    
}
