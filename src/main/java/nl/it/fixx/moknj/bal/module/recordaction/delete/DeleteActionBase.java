package nl.it.fixx.moknj.bal.module.recordaction.delete;

import nl.it.fixx.moknj.bal.BAL;
import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class DeleteActionBase<JOINDOMAIN, DOMAIN, REPO extends MongoRepository> extends BAL<REPO> implements DeleteAction<JOINDOMAIN, DOMAIN> {

    private DeleteAction deleteAction;
    private JOINDOMAIN join;

    public DeleteActionBase(REPO repository) {
        super(repository);
    }

    @Override
    public void execute(ProceedingJoinPoint point, Delete deleteAnnotation) throws Throwable {
        Object[] args = point.getArgs();
        if (valid(args[0])) {
            DOMAIN domain = (DOMAIN) args[0];
            if (hasJoin(deleteAnnotation.intercepts(), Intercept.BEFORE)) {
                join = before(domain);
            }
            domain = (DOMAIN) point.proceed(new Object[]{domain});
            if (hasJoin(deleteAnnotation.intercepts(), Intercept.AFTER)) {
                after(join, domain);
            }
        } else {
            if (deleteAction != null) {
                deleteAction.execute(point, deleteAnnotation);
            }
        }
    }

    private boolean hasJoin(Intercept[] actions, Intercept joinPoint) {
        for (Intercept action : actions) {
            if (joinPoint.equals(action)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JOINDOMAIN before(DOMAIN domain) {
        // empty if not overriden then do nothing.
        return null;
    }

    @Override
    public void after(JOINDOMAIN JOIN, DOMAIN domain) {
        // empty if not overriden then do nothing.
    }

    @Override
    public void setNextIn(DeleteAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    @Override
    public boolean hasNext() {
        return this.deleteAction != null;
    }

}
