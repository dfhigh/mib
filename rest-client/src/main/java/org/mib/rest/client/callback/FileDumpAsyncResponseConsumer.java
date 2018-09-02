package org.mib.rest.client.callback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentDecoderChannel;
import org.apache.http.nio.FileContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import static org.mib.rest.utils.Validator.validateObjectNotNull;
import static org.mib.rest.utils.Validator.validateStringNotBlank;

public class FileDumpAsyncResponseConsumer<T> extends AbstractAsyncResponseConsumer<T> {

    private final RandomAccessFile accessfile;
    private FileChannel fileChannel;
    private long idx = -1;

    public FileDumpAsyncResponseConsumer(String filePath) throws FileNotFoundException {
        validateStringNotBlank(filePath, "file path");
        this.accessfile = new RandomAccessFile(new File(filePath), "rw");
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) {}

    @Override
    protected void onEntityEnclosed(final HttpEntity entity, final ContentType contentType) throws IOException {
        fileChannel = accessfile.getChannel();
        idx = 0;
    }

    @Override
    protected void onContentReceived(final ContentDecoder decoder, final IOControl ioctrl) throws IOException {
        validateObjectNotNull(fileChannel, "file channel");
        final long transferred;
        if (decoder instanceof FileContentDecoder) {
            transferred = ((FileContentDecoder)decoder).transfer(fileChannel, idx, Integer.MAX_VALUE);
        } else {
            transferred = fileChannel.transferFrom(new ContentDecoderChannel(decoder), idx, Integer.MAX_VALUE);
        }
        if (transferred > 0) {
            idx += transferred;
        }
        if (decoder.isCompleted()) {
            fileChannel.close();
        }
    }

    @Override
    protected T buildResult(final HttpContext context) throws Exception {
        return null;
    }

    @Override
    protected void releaseResources() {
        try {
            accessfile.close();
        } catch (final IOException ignore) {
        }
    }
}
