package nl.it.fixx.moknj.exception;

/**
 * This should extend on basic BA class and should be expanded further, the idea
 * behind this exception is that it is used in access BA layers and only thrown
 * there.
 *
 * @author Adriaan
 */
public class AccessException extends BalException {

    public AccessException(String s) {
        super(s);
    }

    public AccessException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AccessException(Throwable throwable) {
        super(throwable);
    }
}
