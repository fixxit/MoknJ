package nl.it.fixx.moknj.bal.module.chainable;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

public abstract class ModuleChainBaseBal<ACTION extends ChainBal> implements ModuleChainBal<ACTION> {

    private final Logger log;

    private final ApplicationContext context;

    public ModuleChainBaseBal(ApplicationContext context, Logger log) {
        this.context = context;
        this.log = log;
    }

    /**
     * Compile execution chain for all beans sharing common interface.
     *
     * @return Action chain
     */
    @Override
    public ACTION getChain() {
        ACTION action = null;
        ACTION focus = null;
        Class<ACTION> cls = getClazz();
        Map<String, ACTION> types = context.getBeansOfType(cls);
        Iterator<Map.Entry<String, ACTION>> actions = types.entrySet().iterator();
        while (actions.hasNext()) {
            Map.Entry<String, ACTION> pair = actions.next();
            log.info("Discovered " + pair.getKey() + ", adding [" + cls.getSimpleName() + "] record chain...");
            if (focus != null && !focus.hasNext()) {
                log.info("[" + cls.getSimpleName() + "] Chaining " + pair.getKey() + " to " + focus.getClass().getSimpleName());
                focus.setNextIn(pair.getValue());
            }
            focus = pair.getValue();
            if (action == null) {
                action = pair.getValue();
            }
        }
        if (action != null) {
            log.info("{} is the 1st in the chain", action.getClass().getSimpleName());
        }
        return action;
    }
}
