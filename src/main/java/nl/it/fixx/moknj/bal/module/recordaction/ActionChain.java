package nl.it.fixx.moknj.bal.module.recordaction;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Find all bean classes which are of certain type. These classes are then 
 * chained. The first class in the chain is returned. This class should be
 * initialized on the main aspect responsible for executing the class.
 *
 * @author SmarBullet
 * @param <ACTION> must be of Action interface.
 */
@Component
public class ActionChain<ACTION extends Action> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionChain.class);

    private final ApplicationContext context;

    @Autowired
    public ActionChain(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Compile execution chain for all beans sharing common interface.
     *
     * @param cls
     * @return Action chain
     */
    public ACTION getActionChain(Class<ACTION> cls) {
        ACTION action = null;
        ACTION focus = null;
        
        Map<String, ACTION> types = context.getBeansOfType(cls);
        Iterator<Map.Entry<String, ACTION>> actions = types.entrySet().iterator();
        while (actions.hasNext()) {
            Map.Entry<String, ACTION> pair = actions.next();
            LOG.info("Discovered " + pair.getKey() + ", adding [" + cls.getSimpleName() + "] record chain...");
            if (focus != null && !focus.hasNext()) {
                LOG.info("[" + cls.getSimpleName() + "] Chaining " + pair.getKey() + " to " + focus.getClass().getSimpleName());
                focus.setNextIn(pair.getValue());
            }
            focus = pair.getValue();
            if (action == null) {
                action = pair.getValue();
            }
        }
        if (action != null) {
            LOG.info("Record Action [" + action.getClass().getSimpleName() + "] is the 1st in the chain [" + cls.getSimpleName() + "]");
        }
        return action;
    }

}
