package utility;

public interface UserInputListener {
    void requestInput(String prompt, java.util.function.Consumer<String> onInputReceived);
}

