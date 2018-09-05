package org.mib.metrics;

/**
 * This class provides a metrics that does nothing, it's the default implementation to prevent NPE if we
 * don't plan to collect.
 */
class NullAwareMetrics implements Metrics {
}
