package org.mib.metrics.analysis;

import com.google.common.collect.Lists;
import org.mib.metrics.Metrics;
import org.mib.metrics.RequestMetrics;
import org.mib.metrics.analysis.filter.MetricsFilter;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

public class MetricsLogAnalyzer {

    public static void analyze(String logPath, MetricsFilter filter) throws Exception {
        List<String> lines = Files.readAllLines(FileSystems.getDefault().getPath(logPath));
        List<Metrics> metrics = Lists.newArrayList();
        List<String> block = Lists.newArrayList();
        for (String line : lines) {
            if (line.trim().startsWith("----")) {
                Metrics m = RequestMetrics.fromLogs(block);
                block.clear();
                if (filter.apply(m)) metrics.add(m);
            } else {
                block.add(line.trim());
            }
        }
        if (!block.isEmpty()) {
            Metrics m = RequestMetrics.fromLogs(block);
            if (filter.apply(m)) metrics.add(m);
        }
        CounterAnalyzer.analyze(metrics);
        LatencyAnalyzer.analyze(metrics);
    }

}
