package nl.it.fixx.moknj.bal.module.chainable;

public interface ChainBal<INTERFACE> {

    void setNextIn(INTERFACE action);

    boolean hasNext();
}
