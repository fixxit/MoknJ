package nl.it.fixx.moknj.bal.module.chainable.impl;

import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBaseBal;
import nl.it.fixx.moknj.bal.module.recordaction.save.SaveAction;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RecordSaveActionChain extends ModuleChainBaseBal<SaveAction> {

    @Autowired
    public RecordSaveActionChain(ApplicationContext context) {
        super(context, LoggerFactory.getLogger(RecordSaveActionChain.class));
    }

    @Override
    public Class<SaveAction> getClazz() {
        return SaveAction.class;
    }

}
