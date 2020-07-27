package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Segmento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Segmento} entity.
 */
public interface SegmentoSearchRepository extends ElasticsearchRepository<Segmento, Long> {
}
