package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Segmento;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Segmento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SegmentoRepository extends JpaRepository<Segmento, Long>, JpaSpecificationExecutor<Segmento> {
}
