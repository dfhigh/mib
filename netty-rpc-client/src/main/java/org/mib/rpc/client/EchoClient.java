package org.mib.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mib.rpc.client.handler.EchoClientHandler;

@Slf4j
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(final String host, final int port) {
        if (StringUtils.isBlank(host)) {
            throw new IllegalArgumentException("host can't be blank");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("port must be positive");
        }
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup elg = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(elg).channel(NioSocketChannel.class).remoteAddress(host, port).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new EchoClientHandler());
                }
            });
            ChannelFuture cf = b.connect().sync();
            cf.channel().closeFuture().sync();
        } finally {
            elg.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient(args[0], Integer.parseInt(args[1])).start();
    }
}
