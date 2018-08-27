package org.mib.rpc.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.mib.rpc.model.Request;
import org.mib.rpc.server.service.AddService;

import static org.mib.common.validator.Validator.validateObjectNotNull;

@Sharable
public class AddServerHandler extends SimpleChannelInboundHandler<Request> {

    private final AddService service;

    public AddServerHandler(final AddService service) {
        validateObjectNotNull(service, "service");
        this.service = service;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        validateObjectNotNull(msg, "request");
        ctx.writeAndFlush(service.add(msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
