package websocketServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.Charset;

/**
 * Created by zhangdekun on 15-5-7-上午9:49.
 */
public class WebSocketHandler  extends SimpleChannelInboundHandler<Object> {
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        WebSocketFrame frame = (WebSocketFrame)msg;
        String id = channelHandlerContext.channel().remoteAddress().toString();
        ByteBuf buf = frame.content();
        String result = buf.toString(Charset.forName("UTF-8"));
        WebSocketFrame out = new TextWebSocketFrame(id+":"+result);
        //channelHandlerContext.pipeline().writeAndFlush(out);
        group.writeAndFlush(out);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        group.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        group.remove(ctx.channel());
        super.channelInactive(ctx);
    }
}
