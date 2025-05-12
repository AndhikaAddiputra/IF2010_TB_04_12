package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {

    public OBJ_Door(GamePanel gp) {
        super(gp);

        name = "Door";
        down1 = setup("/res/image/objects/door", gp.tileSize, gp.tileSize);

        collision = true;
    }
}
