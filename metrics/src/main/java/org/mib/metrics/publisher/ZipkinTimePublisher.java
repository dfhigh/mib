package org.mib.metrics.publisher;

import org.mib.metrics.Metrics;
import org.mib.metrics.time.TimeEvent;
import org.mib.common.hash.FNVHash;
import com.google.common.collect.Lists;
import zipkin.Annotation;
import zipkin.BinaryAnnotation;
import zipkin.Endpoint;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.urlconnection.URLConnectionSender;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This publisher publishes time duration metrics to zipkin.
 */
@SuppressWarnings("unused")
public class ZipkinTimePublisher implements MetricsPublisher {

    private static final String TIME_SPAN_PARENT_ID = "TimeSpanParentId";
    private static final int LOCALHOST_IP = 127 << 24 | 1;

    private static final Comparator<TimeEvent> TE_COMPARATOR = (te1, te2) -> {
        if (te1.getStart() > te2.getStart()) return 1;
        if (te1.getStart() < te2.getStart()) return -1;
        return (int) (te2.getEnd() - te1.getEnd());
    };

    private final AsyncReporter<Span> reporter;
    private final AtomicBoolean hasSpans;

    public ZipkinTimePublisher(final String zipkinEndpoint) {
        if (zipkinEndpoint == null) {
            throw new IllegalArgumentException("invalid zipkin endpoint");
        }
        this.reporter = AsyncReporter.builder(URLConnectionSender.create(zipkinEndpoint)).build();
        this.hasSpans = new AtomicBoolean(false);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            if (hasSpans.get()) reporter.flush();
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void publish(Metrics metrics) {
        List<TimeEvent> timers = Lists.newArrayList(metrics.getTimers().values());
        if (timers.isEmpty()) return;
        timers.sort(TE_COMPARATOR);

        Endpoint ep = Endpoint.create(metrics.getEndpoint(), LOCALHOST_IP);

        String requestId = metrics.getRequestId();
        long traceId = FNVHash.hash64(requestId);
        Long parentId = null;
        try {
            parentId = Long.valueOf(metrics.getMetrics().get(TIME_SPAN_PARENT_ID));
        } catch (Exception e) { /* ignore it */}

        TimeEvent parentTE = null;
        Span parentSpan = null;
        int i = 0;
        while (i < timers.size()) {
            TimeEvent childTE = timers.get(i++);
            if (parentTE == null || !isParentAndChild(parentTE, childTE)) {
                parentTE = childTE;
                parentSpan = createNewSpan(parentTE, requestId, parentId, traceId, ep);
                reporter.report(parentSpan);
            } else {
                reporter.report(createNewSpan(childTE, requestId, parentSpan.id, traceId, ep));
            }
        }
        if (!hasSpans.get()) hasSpans.set(true);
    }

    // judge parent and child relationship between time events
    private boolean isParentAndChild(TimeEvent parent, TimeEvent child) {
        return parent.getStart() <= child.getStart() && parent.getEnd() >= child.getEnd();
    }

    private Span createNewSpan(TimeEvent te, String requestId, Long parentId, long traceId, Endpoint ep) {
        long spanId = (parentId != null) ? ThreadLocalRandom.current().nextLong() : traceId;
        List<Annotation> annotations = Lists.newArrayList(Annotation.create(te.getStart(), "cs", ep),
                Annotation.create(te.getEnd(), "cr", ep));
        List<BinaryAnnotation> binaryAnnotations = Lists.newArrayList(BinaryAnnotation.create("requestId", requestId, ep));

        return Span.builder()
                .id(spanId)
                .parentId(parentId)
                .traceId(traceId)
                .name(te.getName())
                .timestamp(te.getStart())
                .duration(te.getDuration(TimeUnit.MICROSECONDS))
                .annotations(annotations)
                .binaryAnnotations(binaryAnnotations)
                .build();
    }

}
