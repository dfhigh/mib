package org.mib.metrics.publisher;

import org.mib.metrics.Metrics;
import org.slf4j.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Publish metrics to a appendable logger.
 */
public class MetricsLogger implements MetricsPublisher {

    private final Logger logger;

    public MetricsLogger(final Logger logger) {
        this.logger = requireNonNull(logger, "metrics logger can't be null");
    }

    @Override
    public void publish(Metrics metrics) {
        logger.info("{}", metrics);
    }

}
