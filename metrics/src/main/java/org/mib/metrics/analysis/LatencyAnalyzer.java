package org.mib.metrics.analysis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.mib.metrics.Metrics;
import org.mib.metrics.time.TimeEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
class LatencyAnalyzer {

    static void analyze(List<Metrics> metrics) throws Exception {
        if (metrics.isEmpty()) return;
        Map<String, List<Long>> latencies = Maps.newHashMap();
        for (Metrics m : metrics) {
            for (TimeEvent te : m.getTimers().values()) {
                String name = te.getName();
                if (name.startsWith("pool-") || name.startsWith("http-nio-")) name = name.substring(name.indexOf(".")+1);
                List<Long> existed = latencies.computeIfAbsent(name, k -> Lists.newArrayList());
                existed.add(TimeUnit.MILLISECONDS.convert(te.getDuration(), te.getTimeUnit()));
            }
        }
        latencies.values().parallelStream().forEach(Collections::sort);
        int size = metrics.size();
        double duration = (double) (metrics.get(size-1).getTimestamp()-metrics.get(0).getTimestamp()) / 1000.0;
        log.info("timer distribution on {} requests in {} seconds:", size, duration);
        log.info("name\t\t\t\tmin\tmax\tp50\tp90\tp95\tp99\tp999");
        log.info("====================================================================================");
        for (Map.Entry<String, List<Long>> entry : latencies.entrySet()) {
            String name = entry.getKey();
            if (name.length() < 16) name += "\t";
            List<Long> durations = entry.getValue();
            int count = durations.size();
            log.info("{}\t\t{}\t{}\t{}\t{}\t{}\t{}\t{}", name, durations.get(0), durations.get(count-1),
                    durations.get(count/2), durations.get(count*9/10), durations.get(count*19/20), durations.get(count*99/100), durations.get(count*999/1000));
        }
        log.info("----------------------------------------------------------------------------------------------");
    }

}
