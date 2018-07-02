package io.dmt.moneyxchange.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import java.net.URI;


public class CalculatingExchangeRateException extends AbstractThrowableProblem {
    public CalculatingExchangeRateException(@Nullable URI type, @Nullable String title, @Nullable StatusType status) {
        super(ErrorConstants.EXCHANGE_CALCULATION_ERROR, "Error calculating rates", Status.INTERNAL_SERVER_ERROR);
    }
}
