package org.mib.metrics.analysis.filter;

import lombok.NonNull;
import org.mib.metrics.Metrics;

@FunctionalInterface
public interface MetricsFilter {

    MetricsFilter DEFAULT = new DefaultMetricsFilter();

    boolean apply(@NonNull Metrics metrics);

    default MetricsFilter and(@NonNull MetricsFilter filter) {
        return metrics -> apply(metrics) && filter.apply(metrics);
    }

}
