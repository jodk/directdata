package httpServer.resource;

import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/html/2")
public class HtmlResource {

    @ResourceMethod("excel")
    public Object excel() {
        File localFile = new File("/home/supertool/img01.jpg");
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(localFile, "r");
            long length = rf.length();
            byte[] buf = new byte[1024];
            ByteBuf bbf = Unpooled.buffer();
            int bytereda = 0;
            while ((bytereda = rf.read(buf)) != -1) {
                //System.out.write(buf,0,bytereda);
                bbf.writeBytes(buf);
            }
            return bbf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "helloworld ,中文 welcome your visit...";
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
