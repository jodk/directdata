package httpServer.resource;

import httpServer.config.Config;
import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/html")
public class HtmlResource extends AbstractResource {

    public HtmlResource(Object param) {
        super(param);
    }

    @ResourceMethod("/file")
    public Object html() {
        String path = getPath(Config.htmlPosition());
        File localFile = new File(path);
        try {
            return super.file2ByteBuf(localFile);
        } catch (Exception e) {
            throw new RuntimeException("load " + path + " error.");
        }
    }

}
