package org.mib.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.mib.rpc.server.handler.EchoServerHandler;

@Slf4j
public class EchoServer {

    private final int port;

    public EchoServer(final int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("port must be positive");
        }
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        EventLoopGroup elg = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(elg).channel(NioServerSocketChannel.class).localAddress(port).childHandler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(handler);
                    }
                }
            );
            ChannelFuture cf = sb.bind().sync();
            cf.channel().closeFuture().sync();
        } finally {
            elg.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(Integer.parseInt(args[0])).start();
    }
}
