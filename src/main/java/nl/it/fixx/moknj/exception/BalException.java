package nl.it.fixx.moknj.exception;

/**
 * Basic exception implementation, this class needs to be implemented better and
 * expanded further, this exception should ideally be thrown in BA layers.
 *
 *
 * @author Adriaan
 */
public class BalException extends Exception {

    public BalException(String s) {
        super(s);
    }

    public BalException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BalException(Throwable throwable) {
        super(throwable);
    }
}
