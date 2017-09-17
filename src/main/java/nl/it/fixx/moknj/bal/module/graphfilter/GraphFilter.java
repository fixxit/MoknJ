package nl.it.fixx.moknj.bal.module.graphfilter;

import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;
import nl.it.fixx.moknj.bal.module.chainable.ModuleChainPointer;

public interface GraphFilter extends ModuleChainPointer<GraphFilter> {

    String getDefaultValueForModule();

    boolean valid(Graph graphInfo, String token);

    @Override
    boolean hasNext();

    @Override
    void setNext(GraphFilter graphSearch);

    GraphData execute(Graph graphInfo, String token) throws Exception;
}
