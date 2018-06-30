package io.dmt.moneyxchange.repository;

import io.dmt.moneyxchange.domain.SpotExchange;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SpotExchange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotExchangeRepository extends JpaRepository<SpotExchange, Long>, JpaSpecificationExecutor<SpotExchange> {

}
