package nl.it.fixx.moknj.bal.module.chainable.impl;

import org.springframework.stereotype.Component;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBaseBal;
import nl.it.fixx.moknj.bal.module.validator.field.FieldModule;
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
@Component("fieldModuleValidationChain")
public class FieldModuleValidationChain extends ModuleChainBaseBal<FieldModule> {

    @Autowired
    public FieldModuleValidationChain(ApplicationContext context) {
        super(context, LoggerFactory.getLogger(FieldModuleValidationChain.class));
    }

    @Override
    public Class<FieldModule> getClazz() {
        return FieldModule.class;
    }

}
