package io.dmt.moneyxchange.repository;

import io.dmt.moneyxchange.domain.Currency;
import io.dmt.moneyxchange.domain.SpotExchange;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.Optional;


/**
 * Spring Data JPA repository for the SpotExchange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotExchangeRepository extends JpaRepository<SpotExchange, Long>, JpaSpecificationExecutor<SpotExchange> {
    public Optional<SpotExchange> findFirstByFromInstantBeforeAndSourceCurrencyAndTargetCurrencyOrderByFromInstantDesc(Instant instant, Currency sourceCurrency, Currency targetCurrency);
    public Optional<SpotExchange> findFirstByFromInstantAfterAndSourceCurrencyAndTargetCurrencyOrderByFromInstantAsc(Instant instant, Currency sourceCurrency, Currency targetCurrency);
}
