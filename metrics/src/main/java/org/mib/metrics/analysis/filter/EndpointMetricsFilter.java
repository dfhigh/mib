package org.mib.metrics.analysis.filter;

import lombok.NonNull;
import org.mib.metrics.Metrics;

import static java.util.Objects.requireNonNull;

public class EndpointMetricsFilter implements MetricsFilter {

    private final String expectedEndpoint;

    public EndpointMetricsFilter(final String expectedEndpoint) {
        this.expectedEndpoint = requireNonNull(expectedEndpoint);
    }

    @Override
    public boolean apply(@NonNull Metrics metrics) {
        return expectedEndpoint.equals(metrics.getEndpoint());
    }

}
