package newException;

import java.io.IOException;

public class NoValidOperationFound extends Exception {
    public NoValidOperationFound(String message) {
        super(message);
    }
}
