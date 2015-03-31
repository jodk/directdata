package httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

/**
 * Created by zhangdekun on 15-3-31-下午2:24.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger log = Logger.getLogger(HttpServerHandler.class);
    private final HttpDispatcher dispatcher;
    private HttpContext context;

    public HttpServerHandler(HttpDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            context = new HttpContext();
            HttpRequest request = (HttpRequest) msg;
            context.initContent(request, new HttpResponse(request, ctx));
            try {
                dispatcher.dispatch(context);
            } catch (Exception e) {
                log.error("request dispatcher fail:" + e.getMessage());
                //TODO return response
                return;
            }
            if (HttpHeaders.is100ContinueExpected(request)) {
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.write(response);
            }
        }
        if (msg instanceof HttpContext) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                try {
                    context.getBuffer().writeBytes(content);
                } catch (Exception e) {
                    //TODO
                    return;
                }
            }

        }

        if (msg instanceof LastHttpContent) {
            try {
                HttpResult result = dispatcher.getHandler().handle();
                //TODO
            } catch (Exception e) {
                //TODO
                return;
            } finally {
                context.getBuffer().clear();
                context.getHttpResponse().getBuffer().clear();
            }
        }
    }
}
