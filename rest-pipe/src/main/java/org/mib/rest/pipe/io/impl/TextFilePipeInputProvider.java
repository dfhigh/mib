package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeInputProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

/**
 * Created by dufei on 18/5/11.
 */
@Slf4j
public abstract class TextFilePipeInputProvider<T> implements PipeInputProvider<T>, Runnable {

    private final PipeInputProvider<T> internal;
    private final BufferedReader br;

    public TextFilePipeInputProvider(final String fileName, final PipeInputProvider<T> internal) {
        validateStringNotBlank(fileName, "file name");
        validateObjectNotNull(internal, "internal provider instance");
        this.internal = internal;
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        try (BufferedReader reader = br) {
            String line;
            while ((line = reader.readLine()) != null) {
                offer(convert(line));
            }
            internal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void offer(T payload) {
        internal.offer(payload);
    }

    @Override
    public T take() {
        return internal.take();
    }

    @Override
    public boolean isClosed() {
        return internal.isClosed();
    }

    @Override
    public void close() throws IOException {
        internal.close();
        if (br != null) br.close();
    }

    @Override
    public long provided() {
        return internal.provided();
    }

    protected abstract T convert(String line);
}
