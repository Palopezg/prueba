package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.AccountExecutiveService;
import com.mycompany.myapp.domain.AccountExecutive;
import com.mycompany.myapp.repository.AccountExecutiveRepository;
import com.mycompany.myapp.repository.search.AccountExecutiveSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AccountExecutive}.
 */
@Service
@Transactional
public class AccountExecutiveServiceImpl implements AccountExecutiveService {

    private final Logger log = LoggerFactory.getLogger(AccountExecutiveServiceImpl.class);

    private final AccountExecutiveRepository accountExecutiveRepository;

    private final AccountExecutiveSearchRepository accountExecutiveSearchRepository;

    public AccountExecutiveServiceImpl(AccountExecutiveRepository accountExecutiveRepository, AccountExecutiveSearchRepository accountExecutiveSearchRepository) {
        this.accountExecutiveRepository = accountExecutiveRepository;
        this.accountExecutiveSearchRepository = accountExecutiveSearchRepository;
    }

    @Override
    public AccountExecutive save(AccountExecutive accountExecutive) {
        log.debug("Request to save AccountExecutive : {}", accountExecutive);
        AccountExecutive result = accountExecutiveRepository.save(accountExecutive);
        accountExecutiveSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountExecutive> findAll(Pageable pageable) {
        log.debug("Request to get all AccountExecutives");
        return accountExecutiveRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AccountExecutive> findOne(Long id) {
        log.debug("Request to get AccountExecutive : {}", id);
        return accountExecutiveRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountExecutive : {}", id);
        accountExecutiveRepository.deleteById(id);
        accountExecutiveSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountExecutive> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AccountExecutives for query {}", query);
        return accountExecutiveSearchRepository.search(queryStringQuery(query), pageable);    }
}
