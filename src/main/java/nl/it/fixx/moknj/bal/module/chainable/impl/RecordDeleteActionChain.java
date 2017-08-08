package nl.it.fixx.moknj.bal.module.chainable.impl;

import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBaseBal;
import nl.it.fixx.moknj.bal.module.recordaction.delete.DeleteAction;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RecordDeleteActionChain extends ModuleChainBaseBal<DeleteAction> {

    @Autowired
    public RecordDeleteActionChain(ApplicationContext context) {
        super(context, LoggerFactory.getLogger(RecordDeleteActionChain.class));
    }

    @Override
    public Class<DeleteAction> getClazz() {
        return DeleteAction.class;
    }

}
