package org.mib.metrics.time;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * Defines a time consuming event.
 */
public class TimeEvent {

    private final String name;
    private final long start;
    private final long end;
    private final TimeUnit timeUnit;

    public TimeEvent(final String name, final long start, final long end, final TimeUnit timeUnit) {
        this.name = requireNonNull(name);
        if (start > end) {
            throw new IllegalArgumentException("invalid start and end timestamps");
        }
        this.start = start;
        this.end = end;
        this.timeUnit = timeUnit;
    }

    public String getName() {
        return name;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getDuration() {
        return end - start;
    }

    public long getDuration(TimeUnit tu) {
        return tu.convert(getDuration(), timeUnit);
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String toString() {
        return new StringBuilder(name).append(":").append(getDuration()).append(" ").append(timeUnit.name()).toString();
    }

}
