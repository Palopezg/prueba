package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.AccExec;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AccExec} entity.
 */
public interface AccExecSearchRepository extends ElasticsearchRepository<AccExec, Long> {
}
