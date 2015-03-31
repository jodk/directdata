package httpServer;

/**
 * Created by zhangdekun on 15-3-31-下午4:44.
 */
public interface HttpDispatcher {
    public HttpHandler getHandler();
    public void dispatch(HttpContext content);
}
