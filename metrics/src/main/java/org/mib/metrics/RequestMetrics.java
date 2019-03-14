package org.mib.metrics;

import com.google.common.collect.Maps;
import org.mib.metrics.time.TimeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class RequestMetrics implements Metrics {

    private static final String LINE_DELIMITER = "\n";
    private static final String METRICS_DELIMITER = "-----------------------------------------------------------------";

    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    );

    private final String service;
    private final String operation;
    private final String endpoint;
    private final String requestId;
    private long timestamp;
    private final Map<String, Long> counters;
    private final Map<String, String> metrics;
    private final Map<String, TimeEvent> timeEvents;
    private boolean closed;
    private String status;

    public RequestMetrics(final String service, final String operation, final String hostname, final int port,
                          final String requestId) {
        this(service, operation, hostname + ":" + port, requestId);
    }

    private RequestMetrics(final String service, final String operation, final String endpoint, final String requestId) {
        this.service = requireNonNull(service);
        this.operation = requireNonNull(operation);
        this.endpoint = service + "_" + operation + "_" + requireNonNull(endpoint);
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

    public static Metrics fromLogs(List<String> lines) throws Exception {
        RequestMetrics metrics = new RequestMetrics(
                // example: Service:predictor
                lines.get(1).trim().substring(8).trim(),
                // example: Operation:predict
                lines.get(2).trim().substring(10).trim(),
                // example: Endpoint:predictor_predict_docker02:7711
                lines.get(3).trim().substring(9).trim(),
                // example: RequestId:c4de56c4-9443-4e3f-8d3f-d8cdfc42c15c
                lines.get(4).trim().substring(10).trim()
        );
        // example: Time:2016-12-16 16:29:55.468
        metrics.timestamp = SDF.get().parse(lines.get(0).trim().substring(5).trim()).getTime();
        // example: Status:OK
        metrics.status = lines.get(5).trim().substring(7).trim();
        // example: Counters:{request.success=1, cannon.requests=1}
        String countersStr = lines.get(6).trim().substring(9).trim();
        if (countersStr.length() > 2) {
            String[] entries = countersStr.substring(1, countersStr.length() - 1).trim().split(",");
            for (String entry : entries) {
                String[] fields = entry.trim().split("=");
                metrics.counters.put(fields[0].trim(), Long.parseLong(fields[1].trim()));
            }
        }
        // example: Metrics:{cannon.hit=149, signature.total=2062}
        String metricsStr = lines.get(7).trim().substring(8).trim();
        if (metricsStr.length() > 2) {
            String[] entries = metricsStr.substring(1, metricsStr.length() - 1).trim().split(",");
            for (String entry : entries) {
                String[] fields = entry.trim().split("=");
                metrics.metrics.put(fields[0].trim(), fields[1].trim());
            }
        }
        // example: Timers:[prediction.latency:129000 MICROSECONDS, fe.latency:67000 MICROSECONDS]
        String timersStr = lines.get(8).trim().substring(7).trim();
        if (timersStr.length() > 2) {
            String[] entries = timersStr.substring(1, timersStr.length() - 1).trim().split(",");
            for (String entry : entries) {
                String[] fields = entry.trim().split(":");
                String[] subs = fields[1].trim().split(" ");
                metrics.timeEvents.put(fields[0], new TimeEvent(fields[0], 0, Long.parseLong(subs[0].trim()), TimeUnit.valueOf(subs[1].trim())));
            }
        }
        return metrics;
    }

}
