package view.cli;

/**
 * This interface will define the behaviour of a class that listens for an input.
 */
public interface InputListener {

    /**
     * Notifies that an input has been received.
     *
     * @param input the input
     */
    void notify(String input);
}
