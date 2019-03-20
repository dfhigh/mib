package org.mib.metrics.annotation.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.mib.metrics.annotation.Counter;
import org.mib.metrics.annotation.Timer;
import org.mib.metrics.annotation.interceptor.CounterInterceptor;
import org.mib.metrics.annotation.interceptor.TimerInterceptor;

public class MetricsModule extends AbstractModule {

    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timer.class), new TimerInterceptor());
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Counter.class), new CounterInterceptor());
    }
}
