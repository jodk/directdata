package httpServer.resource;

import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/html/2")
public class HtmlResource extends AbstractResource{

    public HtmlResource(Object param) {
        super(param);
    }


    @ResourceMethod("html")
    public Object html() {
        File localFile = new File("/home/supertool/test.html");
        BufferedReader br = null;
        try {
            StringBuffer sb = new StringBuffer("");
            br = new BufferedReader(new FileReader(localFile));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "helloworld ,中文 welcome your visit...";
    }

    public static void main(String[] args) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType("test.html");
        System.out.printf(contentType);
    }
}
