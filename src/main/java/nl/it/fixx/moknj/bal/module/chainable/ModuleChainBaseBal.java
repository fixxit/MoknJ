package nl.it.fixx.moknj.bal.module.chainable;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

public abstract class ModuleChainBaseBal<POINTER extends ModuleChainPointer> implements ModuleChainBal<POINTER> {

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
    public POINTER getChain() {
        POINTER head = null;
        POINTER pointer = null;
        Class<POINTER> cls = getClazz();
        Map<String, POINTER> types = context.getBeansOfType(cls);
        Iterator<Map.Entry<String, POINTER>> pointers = types.entrySet().iterator();
        while (pointers.hasNext()) {
            Map.Entry<String, POINTER> entry = pointers.next();
            log.info("Discovered " + entry.getKey() + "...");
            if (pointer != null && !pointer.hasNext()) {
                log.info("Chaining " + entry.getKey() + " to " + pointer.getClass().getSimpleName());
                pointer.setNext(entry.getValue());
            }
            pointer = entry.getValue();
            if (head == null) {
                head = entry.getValue();
            }
        }
        if (head != null) {
            log.info("{} is the 1st in the chain", head.getClass().getSimpleName());
        }
        return head;
    }
}
