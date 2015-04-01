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
    public HttpResult handle() {
        try {
            Method method = Resource.resourceMethodMap.get(uri);
            Class clz = Resource.resourceMethodInClassMap.get(uri);
            if (null != method && null != clz) {
                return (HttpResult) method.invoke(clz.newInstance(), null);
            } else {
                return new HttpResult();
            }
        } catch (Exception e) {
            return new HttpResult();
        }
    }
}
