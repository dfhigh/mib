package org.mib.metrics.analysis.filter;

import org.mib.metrics.Metrics;

import static java.util.Objects.requireNonNull;

public class ServiceMetricsFilter implements MetricsFilter {

    private final String expectedService;

    public ServiceMetricsFilter(final String expectedService) {
        this.expectedService = requireNonNull(expectedService);
    }

    @Override
    public boolean apply(Metrics metrics) {
        return expectedService.equals(metrics.getService());
    }

}
