package httpServer;

/**
 * Created by zhangdekun on 15-3-31-下午5:00.
 */
public class DefaultHttpDispatcher implements HttpDispatcher {
    private HttpHandler httpHandler;

    @Override
    public HttpHandler getHandler() {
        return this.httpHandler;
    }

    @Override
    public void dispatch(HttpContext content) {

    }
}
