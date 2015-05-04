package httpServer;


import httpServer.resource.Resource;
import io.netty.handler.codec.http.HttpContent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by zhangdekun on 15-4-1-上午11:23.
 */
public class DefaultHttpHandler implements HttpHandler {
    private final HttpContext context;
    private final String path;
    private final String name;

    public DefaultHttpHandler(String path,String name, HttpContext context) {
        this.context = context;
        this.path = path;
        this.name = name;
        this.context.setPath(path);
        this.context.setName(name);
    }

    @Override
    public HttpResult handle() throws RuntimeException {
        try {
            Method method = Resource.resourceMethodMap.get(path);
            Class clz = Resource.resourceMethodInClassMap.get(path);
            HttpResult result = new HttpResult();
            if (null != method && null != clz) {
                Constructor constructor = clz.getConstructor(Object.class);
                Object obj = method.invoke(constructor.newInstance(context), null);
                result.setData(obj);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
