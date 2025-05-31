package model;

import java.awt.Point;

public class Character {
    protected String name;
    protected Point position;

    public Character(String name) {
        this.name = name;
        this.position = new Point(0, 0);
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public Point getPosition() { 
        return position; 
    }
    public void setPosition(Point pos) { 
        this.position = pos; 
    }

}
