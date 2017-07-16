package nl.it.fixx.moknj.bal.module.recordaction;

public interface Action {

    void setNextIn(Action action);

    boolean hasNext();
}
