package nl.it.fixx.moknj.bal.module.recordaction.save;

import nl.it.fixx.moknj.bal.BAL;
import nl.it.fixx.moknj.bal.module.recordaction.Action;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

public abstract class SaveActionBase<JOINDOMAIN, DOMAIN, REPO extends MongoRepository>
        extends BAL<REPO> implements SaveAction<JOINDOMAIN, DOMAIN> {

    private SaveActionBase saveAction;
    private JOINDOMAIN join;

    public SaveActionBase(REPO repository) {
        super(repository);
    }

    @Override
    public void execute(ProceedingJoinPoint point, SaveRecord saverRecord) throws Throwable {
        Object[] args = point.getArgs();
        if (valid(args[0])) {
            DOMAIN domain = (DOMAIN) args[0];
            if (hasJoin(saverRecord.position(), Action.BEFORE)) {
                join = before(domain);
            }
            domain = (DOMAIN) point.proceed(new Object[]{domain});
            if (hasJoin(saverRecord.position(), Action.AFTER)) {
                after(join, domain);
            }
        } else {
            if (saveAction != null) {
                saveAction.execute(point, saverRecord);
            }
        }
    }

    private boolean hasJoin(Action[] actions, Action joinPoint) {
        for (Action action : actions) {
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
    public void setNextIn(SaveActionBase saveAction) {
        this.saveAction = saveAction;
    }

}
