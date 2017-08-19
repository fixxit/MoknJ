package nl.it.fixx.moknj.bal.module.chainable.impl;

import nl.it.fixx.moknj.bal.module.graphfilter.GraphFilter;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainBaseBal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("graphFilterChain")
public class GraphFilterChain extends ModuleChainBaseBal<GraphFilter> {

    @Autowired
    public GraphFilterChain(ApplicationContext context) {
        super(context, LoggerFactory.getLogger(GraphFilterChain.class));
    }

    @Override
    public Class<GraphFilter> getClazz() {
        return GraphFilter.class;
    }

}
