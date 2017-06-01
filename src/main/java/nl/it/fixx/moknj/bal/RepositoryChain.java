package nl.it.fixx.moknj.bal;

/**
 * Main Business Access Layer interface. All common REST Controllers logic needs
 * to be consolidated on this layer. Ideally all business access layers should
 * only have its main repository initialized as class variable and only
 * initialize the business layers it requires. The reasoning behind this is if
 * you change individual ball it will reflect on all business access layers
 * which are implemented across the board.
 *
 * This interface is used as place holder interface or identifier.
 *
 * @author adriaan
 * @param <T>
 */
public abstract class RepositoryChain<T> implements BusinessAccessLayer {

    protected final T repository;

    public RepositoryChain(T repository) {
        this.repository = repository;
    }

}
