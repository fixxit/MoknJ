package nl.it.fixx.moknj.bal;

import org.springframework.data.mongodb.repository.MongoRepository;

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
 * @param <REPOSITORY>
 */
public abstract class BalBase<REPOSITORY extends MongoRepository> implements Bal<REPOSITORY> {
    
    protected final REPOSITORY repository;
    
    public BalBase(REPOSITORY repository) {
        this.repository = repository;
    }
    
}