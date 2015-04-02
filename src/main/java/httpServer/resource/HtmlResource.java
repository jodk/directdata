package httpServer.resource;

import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/html/2")
public class HtmlResource {

    @ResourceMethod("chart")
    public Object chart() {

        return "helloworld ,中文 welcome your visit...";
    }
}
