package org.mib.metrics;

import org.mib.metrics.time.TimeEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Interface for metrics.
 */
public interface Metrics {

    /**
     * Increase a countable metric.
     * addCounter("a", 2);
     * addCounter("a", 3);
     * Above statements will result in 5 stored against counter "a".
     * @param name name of the counter
     * @param count number we want to increase
     */
    default void addCounter(String name, long count) {}

    /**
     * Set a key-value mapping in the metrics.
     * @param name name of the metric
     * @param value value of the metric
     */
    default void addMetric(String name, String value) {}

    /**
     * Set a key-value mapping in the metrics.
     * @param name name of the metric
     * @param value value of the metric
     */
    default void addMetric(String name, boolean value) {}

    /**
     * Set a key-value mapping in the metrics.
     * @param name name of the metric
     * @param value value of the metric
     */
    default void addMetric(String name, int value) {}

    /**
     * Set a key-value mapping in the metrics.
     * @param name name of the metric
     * @param value value of the metric
     */
    default void addMetric(String name, long value) {}

    /**
     * Set a key-value mapping in the metrics.
     * @param name name of the metric
     * @param value value of the metric
     */
    default void addMetric(String name, double value) {}

    /**
     * Set a timer metric of an event.
     * @param name name of the timer
     * @param start start nanos of the event
     * @param end end nanos of the event
     * @param tu time unit of the time values
     */
    default void addTime(String name, long start, long end, TimeUnit tu) {}

    /**
     * Set the status of current operation.
     * @param status string format of operation status
     */
    default void setStatus(String status) {}

    /**
     * Get which service we are collecting metrics.
     * @return service name
     */
    default String getService() {
        return null;
    }

    /**
     * Get from which operation or API we are collecting metrics.
     * @return operation name of the operation or API
     */
    default String getOperation() {
        return null;
    }

    /**
     * Get from which endpoint we are collecting metrics.
     * @return endpoint consists of host and port
     */
    default String getEndpoint() {
        return null;
    }

    /**
     * Get for which request we are collecting metrics.
     * @return global unique requestId
     */
    default String getRequestId() {
        return null;
    }

    /**
     * Get the timestamp when this metrics is initialized.
     * @return start time of this metrics
     */
    default long getTimestamp() {
        return 0;
    }

    /**
     * Get counters map.
     * @return counter container
     */
    default Map<String, Long> getCounters() {
        return null;
    }

    /**
     * Get metrics map.
     * @return metric container
     */
    default Map<String, String> getMetrics() {
        return null;
    }

    /**
     * Get timer list.
     * @return timer list
     */
    default Map<String, TimeEvent> getTimers() {
        return null;
    }

    /**
     * Get status of current operation.
     * @return operation status in string format
     */
    default String getStatus() {
        return null;
    }

    /**
     * If this metrics has been closed.
     * @return close status
     */
    default boolean isClosed() {
        return true;
    }

    /**
     * Set this metrics to closed.
     */
    default void close() {}

}
