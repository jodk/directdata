package httpServer;


import httpServer.resource.Resource;

import java.lang.reflect.Method;

/**
 * Created by zhangdekun on 15-4-1-上午11:23.
 */
public class DefaultHttpHandler implements HttpHandler {
    private final String uri;

    public DefaultHttpHandler(String uri) {
        this.uri = uri;
    }

    @Override
    public HttpResult handle() throws RuntimeException {
        try {
            Method method = Resource.resourceMethodMap.get(uri);
            Class clz = Resource.resourceMethodInClassMap.get(uri);
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
