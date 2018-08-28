package org.mib.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.mib.rpc.codec.SimpleBytesCodec;
import org.mib.rpc.codec.SimpleJsonCodec;
import org.mib.rpc.model.Request;
import org.mib.rpc.model.Response;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicReference;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

@Slf4j
public class AddClient implements Closeable {

    private final EventLoopGroup elg;
    private final ChannelPool pool;

    public AddClient(final String host, final int port, final int maxConn) throws Exception {
        validateStringNotBlank(host, "host");
        validateIntPositive(port, "port");
        validateIntPositive(maxConn, "max connection");
        this.elg = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap().group(elg).channel(NioSocketChannel.class).remoteAddress(host, port);
        ChannelHandler handler = new SimpleJsonCodec<>(Response.class);
        this.pool = new FixedChannelPool(b, new AbstractChannelPoolHandler() {
            @Override
            public void channelCreated(Channel ch) throws Exception {
                ch.pipeline().addLast("decryption", SimpleBytesCodec.getInstance())
                        .addLast("deserialization", handler);
            }
        }, maxConn);
    }

    public Response add(Request request) throws Exception {
        validateObjectNotNull(request, "request");
        AtomicReference<Response> ref = new AtomicReference<>();
        Channel channel = pool.acquire().get();
        channel.pipeline().addLast("response", new SimpleChannelInboundHandler<Response>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
                validateObjectNotNull(msg, "response");
                ref.set(msg);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                cause.printStackTrace();
                ctx.close();
            }
        });
        try {
            ChannelFuture cf = channel.writeAndFlush(request).sync();
            if (cf.isSuccess()) {
                while (ref.get() == null);
                return ref.get();
            } else if (cf.isCancelled()) {
                throw new IllegalStateException("operation cancelled");
            } else if (cf.cause() != null) {
                throw new Exception(cf.cause());
            } else {
                throw new IllegalStateException("unexpected state");
            }
        } finally {
            channel.pipeline().removeLast();
            pool.release(channel).sync();
        }
    }

    @Override
    public void finalize() throws Exception {
        close();
    }

    @Override
    public void close() {
        pool.close();
        elg.shutdownGracefully().syncUninterruptibly();
    }

    public static void main(String[] args) throws Exception {
        try (AddClient client = new AddClient("localhost", 8080, 4)) {
            System.out.println(client.add(new Request(1, 2)));
            System.out.println(client.add(new Request(3, 4)));
            System.out.println(client.add(new Request(7, 8)));
            System.out.println(client.add(new Request(100, 40)));
        }
    }
}
