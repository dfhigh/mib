package org.mib.metrics.analysis.filter;

import org.mib.metrics.Metrics;

public class DefaultMetricsFilter implements MetricsFilter {

    @Override
    public boolean apply(Metrics metrics) {
        return true;
    }

    @Override
    public MetricsFilter and(MetricsFilter filter) {
        return filter;
    }

}
