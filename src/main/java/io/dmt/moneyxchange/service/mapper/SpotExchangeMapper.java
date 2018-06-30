package io.dmt.moneyxchange.service.mapper;

import io.dmt.moneyxchange.domain.*;
import io.dmt.moneyxchange.service.dto.SpotExchangeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SpotExchange and its DTO SpotExchangeDTO.
 */
@Mapper(componentModel = "spring", uses = {CurrencyMapper.class})
public interface SpotExchangeMapper extends EntityMapper<SpotExchangeDTO, SpotExchange> {

    @Mapping(source = "sourceCurrency.id", target = "sourceCurrencyId")
    @Mapping(source = "sourceCurrency.code", target = "sourceCurrencyCode")
    @Mapping(source = "targetCurrency.id", target = "targetCurrencyId")
    @Mapping(source = "targetCurrency.code", target = "targetCurrencyCode")
    SpotExchangeDTO toDto(SpotExchange spotExchange);

    @Mapping(source = "sourceCurrencyId", target = "sourceCurrency")
    @Mapping(source = "targetCurrencyId", target = "targetCurrency")
    SpotExchange toEntity(SpotExchangeDTO spotExchangeDTO);

    default SpotExchange fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpotExchange spotExchange = new SpotExchange();
        spotExchange.setId(id);
        return spotExchange;
    }
}
