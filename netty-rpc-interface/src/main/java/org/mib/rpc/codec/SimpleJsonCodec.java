package org.mib.rpc.codec;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.mib.common.ser.Serdes.deserializeFromJson;
import static org.mib.common.ser.Serdes.serializeAsJsonBytes;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
@Sharable
public class SimpleJsonCodec<I> extends MessageToMessageCodec<byte[], Object> {

    private final Class<I> clazz;

    public SimpleJsonCodec(final Class<I> clazz) {
        validateObjectNotNull(clazz, "input class");
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        validateObjectNotNull(msg, "output message");
        out.add(serializeAsJsonBytes(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        validateObjectNotNull(msg, "input message bytes");
        out.add(deserializeFromJson(msg, clazz));
    }
}
