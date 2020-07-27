package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccountExecutive;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AccountExecutive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountExecutiveRepository extends JpaRepository<AccountExecutive, Long>, JpaSpecificationExecutor<AccountExecutive> {
}
