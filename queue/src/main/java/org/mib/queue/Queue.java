package org.mib.queue;

import org.mib.metrics.Metrics;
import org.mib.metrics.MetricsScope;

import java.util.concurrent.TimeUnit;

import static org.mib.common.validator.Validator.validateObjectNotNull;

public interface Queue<T> {

    void _push(T element);

    default void push(T element) {
        validateObjectNotNull(element, "queue element");
        long start = System.currentTimeMillis();
        _push(element);
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("qpush", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("qpush", 1);
    }

    T _pop();

    default T pop() {
        long start = System.currentTimeMillis();
        T result = _pop();
        long end = System.currentTimeMillis();
        Metrics metrics = MetricsScope.getMetrics();
        metrics.addTime("qpop", start, end, TimeUnit.MILLISECONDS);
        metrics.addCounter("qpop", 1);
        return result;
    }
}
