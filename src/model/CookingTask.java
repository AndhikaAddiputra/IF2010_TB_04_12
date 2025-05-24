package model;

public class CookingTask {
    private Recipe recipe;
    private int remainingMinutes;

    public CookingTask(Recipe recipe) {
        this.recipe = recipe;
        this.remainingMinutes = 60; // default durasi cooking
    }

    public void advanceTime(int minutes) {
        remainingMinutes -= minutes;
    }

    public boolean isDone() {
        return remainingMinutes <= 0;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}

