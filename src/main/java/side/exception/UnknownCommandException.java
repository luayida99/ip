package side.exception;

/**
 * UnknownCommandException is thrown when Side is given an invalid command.
 *
 * @author Lua Yi Da
 */

public class UnknownCommandException extends SideException {

    public UnknownCommandException() {
        super("No such command, what a drag...");
    }
}