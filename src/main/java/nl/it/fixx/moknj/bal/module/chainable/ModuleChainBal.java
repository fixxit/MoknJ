package nl.it.fixx.moknj.bal.module.chainable;

public interface ModuleChainBal<INTERFACE> {

    INTERFACE getChain();
    
    Class<INTERFACE> getClazz();
}
