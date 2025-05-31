package model;

public class IngredientRequirement {
    private String category;
    private String name;

    public IngredientRequirement(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public boolean match(Item item){
        if (category == null) return item.getItemName().equals(name);
        return item.getClass().getSimpleName().equalsIgnoreCase(category)
               && (name.equalsIgnoreCase("Any") || item.getItemName().equalsIgnoreCase(name));
    }

    public boolean matches(Item item) {
        // First check if the item is null
        if (item == null) return false;
    
        // For Misc items, just check the name
        if (category.equals("Misc")) {
            return item.getItemName().equals(name);
        }
        
        // For other categories, check both category and name
        boolean categoryMatch = item.getClass().getSimpleName().equals(category);
        boolean nameMatch = name.equals("Any") || item.getItemName().equals(name);
        
        return categoryMatch && nameMatch;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientRequirement)) return false;
        IngredientRequirement that = (IngredientRequirement) o;
        return category.equalsIgnoreCase(that.category) && name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return category.toLowerCase().hashCode() + name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return category + ":" + name;
    }
}
