package model;

public class MiscItem extends Item {
    private String description;
    private Integer used;

    public MiscItem(String name, String description) {
        super(name, false);
        this.description = description;
        this.used = 0;
    }

    public String getDescription() {
        return description;
    }

    public void usedMiscItem(){
        this.used++;
    }

    @Override
    public String toString() {
        return "MiscItem{" +
                "name='" + getItemName() + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
