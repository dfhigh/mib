package org.mib.metrics.annotation.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.mib.metrics.MetricsScope;
import org.mib.metrics.annotation.Timer;

import java.util.concurrent.TimeUnit;

public class TimerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Timer timer = methodInvocation.getMethod().getAnnotation(Timer.class);
        if (timer == null) return methodInvocation.proceed();
        String name = timer.value();
        if (StringUtils.isBlank(name)) name = methodInvocation.getMethod().getName();
        long start = System.currentTimeMillis();
        try {
            return methodInvocation.proceed();
        } finally {
            MetricsScope.addTime(name, start, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }
}
