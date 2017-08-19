package nl.it.fixx.moknj.bal.module.graphfilter;

import nl.it.fixx.moknj.bal.module.chainable.ChainBal;
import nl.it.fixx.moknj.domain.core.graph.Graph;
import nl.it.fixx.moknj.domain.core.graph.GraphData;

public interface GraphFilter extends ChainBal<GraphFilter> {

    String getDefaultValueForModule();

    boolean valid(Graph graphInfo, String token);

    @Override
    boolean hasNext();

    @Override
    void setNextIn(GraphFilter graphSearch);

    GraphData execute(Graph graphInfo, String token) throws Exception;
}
