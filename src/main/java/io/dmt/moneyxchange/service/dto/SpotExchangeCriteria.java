package io.dmt.moneyxchange.service.dto;

import java.io.Serializable;
import io.dmt.moneyxchange.domain.enumeration.Operation;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the SpotExchange entity. This class is used in SpotExchangeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /spot-exchanges?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SpotExchangeCriteria implements Serializable {
    /**
     * Class for filtering Operation
     */
    public static class OperationFilter extends Filter<Operation> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private InstantFilter fromInstant;

    private OperationFilter operation;

    private LongFilter sourceCurrencyId;

    private LongFilter targetCurrencyId;

    public SpotExchangeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getFromInstant() {
        return fromInstant;
    }

    public void setFromInstant(InstantFilter fromInstant) {
        this.fromInstant = fromInstant;
    }

    public OperationFilter getOperation() {
        return operation;
    }

    public void setOperation(OperationFilter operation) {
        this.operation = operation;
    }

    public LongFilter getSourceCurrencyId() {
        return sourceCurrencyId;
    }

    public void setSourceCurrencyId(LongFilter sourceCurrencyId) {
        this.sourceCurrencyId = sourceCurrencyId;
    }

    public LongFilter getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(LongFilter targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    @Override
    public String toString() {
        return "SpotExchangeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fromInstant != null ? "fromInstant=" + fromInstant + ", " : "") +
                (operation != null ? "operation=" + operation + ", " : "") +
                (sourceCurrencyId != null ? "sourceCurrencyId=" + sourceCurrencyId + ", " : "") +
                (targetCurrencyId != null ? "targetCurrencyId=" + targetCurrencyId + ", " : "") +
            "}";
    }

}
