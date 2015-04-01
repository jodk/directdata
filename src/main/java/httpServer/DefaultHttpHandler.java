package httpServer;


/**
 * Created by zhangdekun on 15-4-1-上午11:23.
 */
public class DefaultHttpHandler implements HttpHandler {
    private final String uri;

    public DefaultHttpHandler(String uri) {
        this.uri = uri;
    }

    @Override
    public HttpResult handle() {
        return null;
    }
}
