package org.mib.metrics.analysis.filter;

import lombok.NonNull;
import org.mib.metrics.Metrics;

import static java.util.Objects.requireNonNull;

public class StatusMetricsFilter implements MetricsFilter {

    private final String expectedStatus;

    public StatusMetricsFilter(final String expectedStatus) {
        this.expectedStatus = requireNonNull(expectedStatus);
    }

    @Override
    public boolean apply(@NonNull Metrics metrics) {
        return expectedStatus.equals(metrics.getStatus());
    }

}
