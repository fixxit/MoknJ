package nl.it.fixx.moknj.bal.module.recordaction.save;

import nl.it.fixx.moknj.bal.BalBase;
import nl.it.fixx.moknj.bal.module.recordaction.Intercept;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class SaveActionBase<JOINDOMAIN, DOMAIN, REPO extends MongoRepository>
        extends BalBase<REPO> implements SaveAction<JOINDOMAIN, DOMAIN> {

    private SaveAction saveAction;
    private JOINDOMAIN join;

    public SaveActionBase(REPO repository) {
        super(repository);
    }

    @Override
    public void execute(ProceedingJoinPoint point, Save saverRecord) throws Throwable {
        Object[] args = point.getArgs();
        if (valid(args[0])) {
            DOMAIN domain = (DOMAIN) args[0];
            if (hasJoin(saverRecord.intercepts(), Intercept.BEFORE)) {
                join = before(domain);
            }
            domain = (DOMAIN) point.proceed(new Object[]{domain});
            if (hasJoin(saverRecord.intercepts(), Intercept.AFTER)) {
                after(join, domain);
            }
        } else {
            if (saveAction != null) {
                saveAction.execute(point, saverRecord);
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
    public void setNextIn(SaveAction saveAction) {
        this.saveAction = saveAction;
    }

    @Override
    public boolean hasNext() {
        return this.saveAction != null;
    }

}
