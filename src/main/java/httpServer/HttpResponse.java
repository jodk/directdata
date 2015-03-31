package httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangdekun on 15-3-31-下午5:16.
 */
public class HttpResponse {
    private final HttpRequest request;
    private final ChannelHandlerContext ctx;
    private final ByteBuf buffer = Unpooled.buffer();

    private boolean keepAlive;
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Set<Cookie> cookies = new HashSet<Cookie>();

    public HttpResponse(HttpRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    public ByteBuf getBuffer() {
        return buffer;
    }
}
