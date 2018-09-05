package org.mib.metrics.publisher;

import org.mib.metrics.Metrics;

/**
 * Metrics publisher publishes metrics to pre-defined destination.
 */
public interface MetricsPublisher {

    /**
     * Publish metrics to pre-defined destination.
     * @param metrics that needs to be published
     */
    default void publish(Metrics metrics) {}

}
