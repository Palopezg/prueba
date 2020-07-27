package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.AccExecService;
import com.mycompany.myapp.domain.AccExec;
import com.mycompany.myapp.repository.AccExecRepository;
import com.mycompany.myapp.repository.search.AccExecSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AccExec}.
 */
@Service
@Transactional
public class AccExecServiceImpl implements AccExecService {

    private final Logger log = LoggerFactory.getLogger(AccExecServiceImpl.class);

    private final AccExecRepository accExecRepository;

    private final AccExecSearchRepository accExecSearchRepository;

    public AccExecServiceImpl(AccExecRepository accExecRepository, AccExecSearchRepository accExecSearchRepository) {
        this.accExecRepository = accExecRepository;
        this.accExecSearchRepository = accExecSearchRepository;
    }

    @Override
    public AccExec save(AccExec accExec) {
        log.debug("Request to save AccExec : {}", accExec);
        AccExec result = accExecRepository.save(accExec);
        accExecSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccExec> findAll(Pageable pageable) {
        log.debug("Request to get all AccExecs");
        return accExecRepository.findAll(pageable);
    }


    public Page<AccExec> findAllWithEagerRelationships(Pageable pageable) {
        return accExecRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccExec> findOne(Long id) {
        log.debug("Request to get AccExec : {}", id);
        return accExecRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccExec : {}", id);
        accExecRepository.deleteById(id);
        accExecSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccExec> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccExecs for query {}", query);
        return accExecSearchRepository.search(queryStringQuery(query), pageable);    }
}
