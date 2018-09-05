package org.mib.metrics;

import com.google.common.collect.Maps;
import org.mib.metrics.time.TimeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class ProphetMetrics implements Metrics {

    private static final String LINE_DELIMITER = "\n";
    private static final String METRICS_DELIMITER = "-----------------------------------------------------------------";

    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    );

    private final String service;
    private final String operation;
    private final String endpoint;
    private final String requestId;
    private final long timestamp;
    private final Map<String, Long> counters;
    private final Map<String, String> metrics;
    private final Map<String, TimeEvent> timeEvents;
    private boolean closed;
    private String status;

    public ProphetMetrics(final String service, final String operation, final String hostname,
                          final int port, final String requestId) {
        this.service = requireNonNull(service);
        this.operation = requireNonNull(operation);
        this.endpoint = service + "_" + operation + "_" + requireNonNull(hostname) + ":" + port;
        this.requestId = requireNonNull(requestId);
        this.timestamp = System.currentTimeMillis();
        this.counters = Maps.newConcurrentMap();
        this.metrics = Maps.newConcurrentMap();
        this.timeEvents = Maps.newConcurrentMap();
        this.closed = false;
        this.status = "in-progress";
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, Long> getCounters() {
        return counters;
    }

    @Override
    public Map<String, String> getMetrics() {
        return metrics;
    }

    @Override
    public Map<String, TimeEvent> getTimers() {
        return timeEvents;
    }

    @Override
    public void addCounter(String name, long count) {
        Long value = counters.get(name);
        counters.put(name, value == null ? count : count + value);
    }

    @Override
    public void addMetric(String name, String value) {
        metrics.put(name, value);
    }

    @Override
    public void addMetric(String name, boolean value) {
        metrics.put(name, Boolean.toString(value));
    }

    @Override
    public void addMetric(String name, int value) {
        metrics.put(name, Integer.toString(value));
    }

    @Override
    public void addMetric(String name, long value) {
        metrics.put(name, Long.toString(value));
    }

    @Override
    public void addMetric(String name, double value) {
        metrics.put(name, Double.toString(value));
    }

    @Override
    public void addTime(String name, long start, long end, TimeUnit tu) {
        timeEvents.put(name, new TimeEvent(name, start, end, tu));
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time:").append(SDF.get().format(new Date(timestamp))).append(LINE_DELIMITER);
        sb.append("Service:").append(service).append(LINE_DELIMITER);
        sb.append("Operation:").append(operation).append(LINE_DELIMITER);
        sb.append("Endpoint:").append(endpoint).append(LINE_DELIMITER);
        sb.append("RequestId:").append(requestId).append(LINE_DELIMITER);
        sb.append("Status:").append(status).append(LINE_DELIMITER);

        if (counters != null) {
            sb.append("Counters:").append(counters.toString()).append(LINE_DELIMITER);
        }
        if (metrics != null) {
            sb.append("Metrics:").append(metrics.toString()).append(LINE_DELIMITER);
        }
        if (timeEvents != null) {
            sb.append("Timers:").append(timeEvents.values().toString()).append(LINE_DELIMITER);
        }
        return sb.append(METRICS_DELIMITER).toString();
    }

}
