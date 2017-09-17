package nl.it.fixx.moknj.bal.module.chainable;

public interface ModuleChainPointer<INTERFACE> {

    void setNext(INTERFACE pointer);

    boolean hasNext();
}
