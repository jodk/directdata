package httpServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created by zhangdekun on 15-3-31-下午4:25.
 */
public class HttpContext {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public ByteBuf getBuffer() {
        return buffer;
    }

    private final ByteBuf buffer = Unpooled.buffer();

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public void initContent(HttpRequest request, HttpResponse reponse) {
        this.httpRequest = request;
        this.httpResponse = reponse;
    }
}
