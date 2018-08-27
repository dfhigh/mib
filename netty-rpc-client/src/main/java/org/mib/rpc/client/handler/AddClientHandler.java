package org.mib.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.mib.rpc.model.Request;
import org.mib.rpc.model.Response;

import static org.mib.common.validator.Validator.validateObjectNotNull;

public class AddClientHandler extends SimpleChannelInboundHandler<Response> {

    private static volatile AddClientHandler INSTANCE = null;

    private AddClientHandler() {}

    public static AddClientHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (AddClientHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AddClientHandler();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Request req = new Request();
        req.setX(1);
        req.setY(7);
        ctx.writeAndFlush(req);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        validateObjectNotNull(msg, "response");
        System.out.println("result is " + msg.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
