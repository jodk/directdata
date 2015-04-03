package httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangdekun on 15-3-31-下午5:16.
 */
public class HttpResponse {
    private static final Logger log = Logger.getLogger(HttpResponse.class);
    private final HttpRequest request;
    private final ChannelHandlerContext ctx;
    private final ByteBuf buffer = Unpooled.buffer();

    private boolean keepAlive;
    private HttpResponseStatus status = HttpResponseStatus.OK;
    private Map<String, Object> headers = new HashMap<String, Object>();
    private Set<Cookie> cookies = new HashSet<Cookie>();

    public HttpResponse(HttpRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
        this.keepAlive = HttpHeaders.isKeepAlive(request);
    }


    public ByteBuf getBuffer() {
        return buffer;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void setHeader(String key, Object value) {
        this.headers.put(key, value);
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(String key, String value) {
        Cookie cookie = new DefaultCookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 1000);
        this.cookies.add(cookie);
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public <T> void send(T msg) {
        if (keepAlive) {
            setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        appendBody(msg);
        if (!this.headers.containsKey(HttpHeaders.Names.CONTENT_TYPE)) {
            String content_type = request.headers().get(HttpHeaders.Names.CONTENT_TYPE);
            if (content_type != null) {
                setHeader(HttpHeaders.Names.CONTENT_TYPE, content_type);
            } else {
                setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/octet-stream; charset=UTF-8");
            }
        }
        setHeader(HttpHeaders.Names.CONTENT_LENGTH, buffer.writerIndex());
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(buffer));

        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            response.headers().set(entry.getKey(), entry.getValue());
        }

        for (Cookie cookie : cookies) {
            response.headers().add(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.encode(cookie));
        }
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public void error(HttpResponseStatus status) {
        this.status = status;
    }

    private <T> void appendBody(T msg) {
        if (msg == null) {
            return;
        }
        if (msg instanceof ByteBuf) {
            buffer.writeBytes((ByteBuf) msg);
        } else {
            buffer.writeBytes(msg.toString().getBytes(CharsetUtil.UTF_8));
        }
    }
}
