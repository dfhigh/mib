package org.mib.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
@Sharable
public class SimpleBytesCodec extends MessageToMessageCodec<ByteBuf, byte[]> {

    private static volatile SimpleBytesCodec INSTANCE = null;

    public static SimpleBytesCodec getInstance() {
        if (INSTANCE == null) {
            synchronized (SimpleBytesCodec.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SimpleBytesCodec();
                }
            }
        }
        return INSTANCE;
    }

    private SimpleBytesCodec() {}

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        validateObjectNotNull(msg, "output message");
        out.add(Unpooled.wrappedBuffer(encode(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        validateObjectNotNull(msg, "input message");
        byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(0, array);
        out.add(decode(array));
    }

    private byte[] encode(byte[] decrypted) {
        for (int i = 0; i < decrypted.length; i++) {
            decrypted[i] = decrypted[i] == Byte.MAX_VALUE ? Byte.MIN_VALUE : (byte) (decrypted[i] + 1);
        }
        return decrypted;
    }

    private byte[] decode(byte[] encrypted) {
        for (int i = 0; i < encrypted.length; i++) {
            encrypted[i] = encrypted[i] == Byte.MIN_VALUE ? Byte.MAX_VALUE : (byte) (encrypted[i] - 1);
        }
        return encrypted;
    }

    public static void main(String[] args) {
        String input = "fuck you";
        SimpleBytesCodec sbc = SimpleBytesCodec.getInstance();
        System.out.println(new String(sbc.decode(sbc.encode(input.getBytes(UTF_8))), UTF_8));
    }
}
