package httpServer.resource;

import java.util.Objects;

/**
 * Created by zhangdekun on 15-4-3-下午6:39.
 */
public class AbstractResource {
    private final Object param;

    public Object getParam() {
        return param;
    }

    public AbstractResource(Object param) {
        this.param = param;
    }
}
