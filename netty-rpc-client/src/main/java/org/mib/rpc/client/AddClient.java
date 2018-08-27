package org.mib.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.mib.rpc.client.handler.AddClientHandler;
import org.mib.rpc.codec.SimpleBytesCodec;
import org.mib.rpc.codec.SimpleJsonCodec;
import org.mib.rpc.model.Response;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateStringNotBlank;

public class AddClient {

    private final String host;
    private final int port;

    public AddClient(final String host, final int port) {
        validateStringNotBlank(host, "host");
        validateIntPositive(port, "port");
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup elg = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(elg).channel(NioSocketChannel.class).remoteAddress(host, port).handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast("decryption", SimpleBytesCodec.getInstance())
                                .addLast("deserialization", new SimpleJsonCodec<>(Response.class))
                                .addLast("output", AddClientHandler.getInstance());
                    }
                }
            );
            ChannelFuture cf = b.connect().sync();
            cf.channel().closeFuture().sync();
        } finally {
            elg.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new AddClient(args[0], Integer.parseInt(args[1])).start();
    }
}
