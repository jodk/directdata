package httpServer;


import httpServer.resource.Resource;
import io.netty.handler.codec.http.HttpContent;

import java.lang.reflect.Method;

/**
 * Created by zhangdekun on 15-4-1-上午11:23.
 */
public class DefaultHttpHandler implements HttpHandler {
    private final HttpContext context;
    private final String path;

    public DefaultHttpHandler(String path, HttpContext context) {
        this.context = context;
        this.path = path;
    }

    @Override
    public HttpResult handle() throws RuntimeException {
        try {
            Method method = Resource.resourceMethodMap.get(path);
            Class clz = Resource.resourceMethodInClassMap.get(path);
            HttpResult result = new HttpResult();
            if (null != method && null != clz) {
                Object obj = method.invoke(clz.newInstance(), null);
                result.setData(obj);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
