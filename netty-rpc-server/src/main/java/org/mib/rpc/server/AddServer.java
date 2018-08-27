package org.mib.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.mib.rpc.codec.SimpleBytesCodec;
import org.mib.rpc.codec.SimpleJsonCodec;
import org.mib.rpc.model.Request;
import org.mib.rpc.server.handler.AddServerHandler;
import org.mib.rpc.server.service.AddService;

import static org.mib.common.validator.Validator.validateIntPositive;

public class AddServer {

    private final int port;

    public AddServer(final int port) {
        validateIntPositive(port, "listening port");
        this.port = port;
    }

    public void start() throws Exception {
        final ChannelHandler bHandler = new AddServerHandler(AddService.getInstance());
        final ChannelHandler jsonHandler = new SimpleJsonCodec<>(Request.class);
        EventLoopGroup elg = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(elg).channel(NioServerSocketChannel.class).localAddress(port).childHandler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast("decryption", SimpleBytesCodec.getInstance())
                                    .addLast("deserializaiton", jsonHandler)
                                    .addLast("add", bHandler);
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
        new AddServer(Integer.parseInt(args[0])).start();
    }

}
