package org.mib.metrics;

import org.mib.metrics.publisher.MetricsPublisher;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * Static entry for metrics collection.
 */
@SuppressWarnings("unused")
public class MetricsScope {

    private static final ThreadLocal<Metrics> metricsHolder = ThreadLocal.withInitial(NullAwareMetrics::new);

    public static Metrics getMetrics() {
        return metricsHolder.get();
    }

    public static void setMetrics(Metrics metrics) {
        metricsHolder.set(requireNonNull(metrics));
    }

    public static void addCounter(String name, long count) {
        metricsHolder.get().addCounter(name, count);
    }

    public static void addMetric(String name, String value) {
        metricsHolder.get().addMetric(name, value);
    }

    public static void addMetric(String name, boolean value) {
        metricsHolder.get().addMetric(name, value);
    }

    public static void addMetric(String name, int value) {
        metricsHolder.get().addMetric(name, value);
    }

    public static void addMetric(String name, long value) {
        metricsHolder.get().addMetric(name, value);
    }

    public static void addMetric(String name, double value) {
        metricsHolder.get().addMetric(name, value);
    }

    public static void addTime(String name, long start, long end, TimeUnit tu) {
        metricsHolder.get().addTime(name, start, end, tu);
    }

    public static void setStatus(String status) {
        metricsHolder.get().setStatus(status);
    }

    public static void close(MetricsPublisher publisher) {
        Metrics metrics = metricsHolder.get();
        if (!metrics.isClosed()) {
            publisher.publish(metrics);
            metrics.close();
        }
    }

}
