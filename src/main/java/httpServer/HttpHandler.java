package httpServer;

/**
 * Created by zhangdekun on 15-3-31-下午4:23.
 */
public interface HttpHandler {
    public HttpResult handle() throws RuntimeException;
}
