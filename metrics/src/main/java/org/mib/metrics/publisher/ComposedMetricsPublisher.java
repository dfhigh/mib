package org.mib.metrics.publisher;

import org.mib.metrics.Metrics;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * A composed implementation for metrics publisher.
 */
public class ComposedMetricsPublisher implements MetricsPublisher {

    private final Collection<MetricsPublisher> publishers;

    public ComposedMetricsPublisher(final Collection<MetricsPublisher> publishers) {
        this.publishers = requireNonNull(publishers, "publishers can't be null");
    }

    @Override
    public void publish(Metrics metrics) {
        publishers.forEach(p -> p.publish(metrics));
    }

}
