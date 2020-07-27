package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.AccountExecutive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AccountExecutive} entity.
 */
public interface AccountExecutiveSearchRepository extends ElasticsearchRepository<AccountExecutive, Long> {
}
