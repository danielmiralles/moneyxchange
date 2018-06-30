package io.dmt.moneyxchange.repository.search;

import io.dmt.moneyxchange.domain.SpotExchange;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SpotExchange entity.
 */
public interface SpotExchangeSearchRepository extends ElasticsearchRepository<SpotExchange, Long> {
}
