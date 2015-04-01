package httpServer.resource;

import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;
import org.apache.log4j.Logger;
import utils.Finder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangdekun on 15-4-1-下午2:02.
 */
public final class Resource {
    private static Logger log = Logger.getLogger(Resource.class);
    private static final String RESOURCE_PACKAGE = "httpServer.resource"; //Default
    private static final String SEP = "/";
    /**
     * 统一资源定位
     * uri能够访问到的class
     */
    public static final Map<String, Class<?>> resourceClassMap = new HashMap<String, Class<?>>();
    /**
     * 统一资源定位
     * uri能够访问到的method
     */
    public static final Map<String, Method> resourceMethodMap = new HashMap<String, Method>();

    /**
     * 初始化部分资源
     */
    public static void init() {
        register(RESOURCE_PACKAGE);
    }

    /**
     * 注册使用的所有资源
     *
     * @param packageName
     */
    public static void register(String packageName) {
        Set<Class<?>> classes = Finder.getClasses(packageName);
        registerClass(classes);
    }

    private static void registerClass(Set<Class<?>> classes) {
        if (null != classes) {
            for (Class clz : classes) {
                if (clz.isAnnotationPresent(ResourceUri.class)) {
                    ResourceUri resourceUriAnnotation = (ResourceUri) clz.getAnnotation(ResourceUri.class);
                    String uri = resourceUriAnnotation.value();
                    if (resourceClassMap.containsKey(uri)) {
                        log.warn("uri:" + uri + " has exist,ignore this resource:" + clz.getName());
                    } else {
                        resourceClassMap.put(uri, clz);
                        log.info("Map{" + uri + "=" + clz.getName() + "}");
                        // register method in this class
                        Method[] methods = clz.getDeclaredMethods();
                        registerMethod(uri, methods);
                    }
                }
            }
        }
    }

    private static void registerMethod(String uri, Method[] methods) {
        if (null != methods) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(ResourceMethod.class)) {
                    ResourceMethod resourceMethodAnnotation = method.getAnnotation(ResourceMethod.class);
                    String methodUri = methodUri(uri, resourceMethodAnnotation.value());
                    if (resourceMethodMap.containsKey(methodUri)) {
                        log.warn("uri:" + methodUri + " has exist,ignore this resource:" + method.getName());
                    } else {
                        log.info("Map{" + methodUri + "=" + method.getName() + "}");
                    }
                }
            }
        }
    }

    private static String methodUri(String classUri, String methodUri) {
        String uri = classUri + SEP + methodUri;
        uri = uri.replaceAll("\\" + SEP + "+", SEP);
        return uri;
    }

    public static void main(String[] args) {
        init();
    }
}
