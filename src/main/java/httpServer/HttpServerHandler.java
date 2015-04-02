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
                context.getHttpResponse().error(HttpResponseStatus.BAD_REQUEST);
                return;
            }
            if (HttpHeaders.is100ContinueExpected(request)) {
                context.getHttpResponse().error(HttpResponseStatus.CONTINUE);
                return;
            }
        }
        if (msg instanceof HttpContext) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                try {
                    context.getBuffer().writeBytes(content);
                } catch (Exception e) {
                    context.getHttpResponse().error(HttpResponseStatus.BAD_REQUEST);
                    return;
                }
            }

        }

        if (msg instanceof LastHttpContent) {
            try {
                HttpResult result = dispatcher.getHandler().handle();
                context.getHttpResponse().send(result.getData());
            } catch (Exception e) {
                context.getHttpResponse().error(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                return;
            } finally {
                context.getBuffer().clear();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
