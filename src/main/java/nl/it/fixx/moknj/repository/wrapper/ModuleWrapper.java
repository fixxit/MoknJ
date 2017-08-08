package nl.it.fixx.moknj.repository.wrapper;

import java.util.List;

public interface ModuleWrapper<DOMAIN, REPOSITORY> {

    DOMAIN save(DOMAIN s);

    void delete(DOMAIN t);

    List<DOMAIN> getAllByTypeId(String id);

    REPOSITORY getRepository();

    List<DOMAIN> findAll();

}
