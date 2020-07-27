package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccountExecutive;
import com.mycompany.myapp.service.AccountExecutiveService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.AccountExecutiveCriteria;
import com.mycompany.myapp.service.AccountExecutiveQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AccountExecutive}.
 */
@RestController
@RequestMapping("/api")
public class AccountExecutiveResource {

    private final Logger log = LoggerFactory.getLogger(AccountExecutiveResource.class);

    private static final String ENTITY_NAME = "accountExecutive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountExecutiveService accountExecutiveService;

    private final AccountExecutiveQueryService accountExecutiveQueryService;

    public AccountExecutiveResource(AccountExecutiveService accountExecutiveService, AccountExecutiveQueryService accountExecutiveQueryService) {
        this.accountExecutiveService = accountExecutiveService;
        this.accountExecutiveQueryService = accountExecutiveQueryService;
    }

    /**
     * {@code POST  /account-executives} : Create a new accountExecutive.
     *
     * @param accountExecutive the accountExecutive to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountExecutive, or with status {@code 400 (Bad Request)} if the accountExecutive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-executives")
    public ResponseEntity<AccountExecutive> createAccountExecutive(@RequestBody AccountExecutive accountExecutive) throws URISyntaxException {
        log.debug("REST request to save AccountExecutive : {}", accountExecutive);
        if (accountExecutive.getId() != null) {
            throw new BadRequestAlertException("A new accountExecutive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountExecutive result = accountExecutiveService.save(accountExecutive);
        return ResponseEntity.created(new URI("/api/account-executives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-executives} : Updates an existing accountExecutive.
     *
     * @param accountExecutive the accountExecutive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountExecutive,
     * or with status {@code 400 (Bad Request)} if the accountExecutive is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountExecutive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-executives")
    public ResponseEntity<AccountExecutive> updateAccountExecutive(@RequestBody AccountExecutive accountExecutive) throws URISyntaxException {
        log.debug("REST request to update AccountExecutive : {}", accountExecutive);
        if (accountExecutive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountExecutive result = accountExecutiveService.save(accountExecutive);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountExecutive.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /account-executives} : get all the accountExecutives.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountExecutives in body.
     */
    @GetMapping("/account-executives")
    public ResponseEntity<List<AccountExecutive>> getAllAccountExecutives(AccountExecutiveCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccountExecutives by criteria: {}", criteria);
        Page<AccountExecutive> page = accountExecutiveQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-executives/count} : count all the accountExecutives.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-executives/count")
    public ResponseEntity<Long> countAccountExecutives(AccountExecutiveCriteria criteria) {
        log.debug("REST request to count AccountExecutives by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountExecutiveQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-executives/:id} : get the "id" accountExecutive.
     *
     * @param id the id of the accountExecutive to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountExecutive, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-executives/{id}")
    public ResponseEntity<AccountExecutive> getAccountExecutive(@PathVariable Long id) {
        log.debug("REST request to get AccountExecutive : {}", id);
        Optional<AccountExecutive> accountExecutive = accountExecutiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountExecutive);
    }

    /**
     * {@code DELETE  /account-executives/:id} : delete the "id" accountExecutive.
     *
     * @param id the id of the accountExecutive to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-executives/{id}")
    public ResponseEntity<Void> deleteAccountExecutive(@PathVariable Long id) {
        log.debug("REST request to delete AccountExecutive : {}", id);
        accountExecutiveService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/account-executives?query=:query} : search for the accountExecutive corresponding
     * to the query.
     *
     * @param query the query of the accountExecutive search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-executives")
    public ResponseEntity<List<AccountExecutive>> searchAccountExecutives(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountExecutives for query {}", query);
        Page<AccountExecutive> page = accountExecutiveService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
