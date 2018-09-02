package org.mib.rest.pipe;

import org.mib.rest.pipe.io.PipeInputProvider;
import org.mib.rest.pipe.io.PipeOutputConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mib.rest.utils.Validator.validateIntPositive;
import static org.mib.rest.utils.Validator.validateObjectNotNull;

public class PipeDriver<I, O> {

    private final PipeInputProvider<I> inputProvider;
    private final PipeOutputConsumer<I, O> outputConsumer;
    private final ExecutorService es;
    private final int parallel;

    public PipeDriver(final PipeInputProvider<I> inputProvider, final PipeOutputConsumer<I, O> outputConsumer, final int parallel) {
        validateObjectNotNull(inputProvider, "input provider");
        validateObjectNotNull(outputConsumer, "output consumer");
        validateIntPositive(parallel, "consumer thread count");
        this.inputProvider = inputProvider;
        this.outputConsumer = outputConsumer;
        this.es = Executors.newFixedThreadPool(parallel);
        this.parallel = parallel;
    }

    public void start() {
        for (int i = 0; i < parallel; i++) {
            es.submit(() -> {
                while (!inputProvider.isClosed()) {
                    I input = inputProvider.take();
                    if (input == null) continue;
                    outputConsumer.consume(input);
                }
            });
        }
    }
}
