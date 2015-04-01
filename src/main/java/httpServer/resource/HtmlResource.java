package httpServer.resource;

import httpServer.HttpResult;
import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/html/2")
public class HtmlResource {

    @ResourceMethod("chart")
    public HttpResult chart() {
        return new HttpResult();
    }
}
