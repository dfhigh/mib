package org.mib.metrics.analysis.filter;

import lombok.NonNull;
import org.mib.metrics.Metrics;

import static java.util.Objects.requireNonNull;

public class OperationMetricsFilter implements MetricsFilter {

    private final String expectedOperation;

    public OperationMetricsFilter(final String expectedOperation) {
        this.expectedOperation = requireNonNull(expectedOperation);
    }

    @Override
    public boolean apply(@NonNull Metrics metrics) {
        return expectedOperation.equals(metrics.getOperation());
    }

}
