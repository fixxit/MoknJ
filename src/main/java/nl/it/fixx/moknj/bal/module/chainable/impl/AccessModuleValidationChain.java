package nl.it.fixx.moknj.bal.module.chainable.impl;

import org.springframework.stereotype.Component;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBaseBal;
import nl.it.fixx.moknj.bal.module.validator.access.AccessModule;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Find all bean classes which are of certain type. These classes are then
 * chained. The first class in the chain is returned. This class should be
 * initialized on the main aspect responsible for executing the class.
 *
 * @author SmarBullet
 */
@Component
public class AccessModuleValidationChain extends ModuleChainBaseBal<AccessModule> {

    @Autowired
    public AccessModuleValidationChain(ApplicationContext context) {
        super(context, LoggerFactory.getLogger(AccessModuleValidationChain.class));
    }

    @Override
    public Class<AccessModule> getClazz() {
        return AccessModule.class;
    }

}
