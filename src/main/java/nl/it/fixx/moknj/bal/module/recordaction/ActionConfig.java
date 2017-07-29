package nl.it.fixx.moknj.bal.module.recordaction;

import nl.it.fixx.moknj.bal.module.recordaction.ActionChain;
import nl.it.fixx.moknj.bal.module.recordaction.delete.DeleteAction;
import nl.it.fixx.moknj.bal.module.recordaction.save.SaveAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActionConfig {

    @Bean
    public DeleteAction deleteActionChain(ActionChain<DeleteAction> chain) {
        return chain.getActionChain(DeleteAction.class);
    }

    @Bean
    public SaveAction saveActionChain(ActionChain<SaveAction> chain) {
        return chain.getActionChain(SaveAction.class);
    }

}
