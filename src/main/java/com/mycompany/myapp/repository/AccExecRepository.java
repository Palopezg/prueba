package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccExec;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AccExec entity.
 */
@Repository
public interface AccExecRepository extends JpaRepository<AccExec, Long>, JpaSpecificationExecutor<AccExec> {

    @Query(value = "select distinct accExec from AccExec accExec left join fetch accExec.regions",
        countQuery = "select count(distinct accExec) from AccExec accExec")
    Page<AccExec> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct accExec from AccExec accExec left join fetch accExec.regions")
    List<AccExec> findAllWithEagerRelationships();

    @Query("select accExec from AccExec accExec left join fetch accExec.regions where accExec.id =:id")
    Optional<AccExec> findOneWithEagerRelationships(@Param("id") Long id);
}
