package nl.it.fixx.moknj.repository.wrapper;

import java.util.List;
import nl.it.fixx.moknj.domain.core.record.Record;
import nl.it.fixx.moknj.repository.RecordRepository;

public abstract class BaseWrapper<DOMAIN extends Record, REPO extends RecordRepository<DOMAIN>>
        implements ModuleWrapper<DOMAIN, REPO> {

    private final REPO repository;

    public BaseWrapper(REPO repository) {
        this.repository = repository;
    }

    @Override
    public DOMAIN save(DOMAIN s) {
        return repository.save(s);
    }

    @Override
    public void delete(DOMAIN t) {
        repository.delete(t);
    }

    @Override
    public List<DOMAIN> getAllByTypeId(String id) {
        return repository.getAllByTypeId(id);
    }

    @Override
    public REPO getRepository() {
        return repository;
    }

    @Override
    public List<DOMAIN> findAll() {
        return repository.findAll();
    }

}
