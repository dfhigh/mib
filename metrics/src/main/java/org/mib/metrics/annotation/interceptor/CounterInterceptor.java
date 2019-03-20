package org.mib.metrics.annotation.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.mib.metrics.MetricsScope;
import org.mib.metrics.annotation.Counter;

public class CounterInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Counter counter = invocation.getMethod().getAnnotation(Counter.class);
        if (counter != null) {
            String name = counter.value();
            if (StringUtils.isBlank(name)) name = invocation.getMethod().getName();
            MetricsScope.addCounter(name, 1);
        }
        return invocation.proceed();
    }
}
