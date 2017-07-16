package nl.it.fixx.moknj.bal.module.recordaction;

public interface Action<INTERFACE> {

    void setNextIn(INTERFACE action);

    boolean hasNext();
}
