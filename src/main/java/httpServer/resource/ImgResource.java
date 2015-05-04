package httpServer.resource;

import httpServer.HttpContext;
import httpServer.config.Config;
import httpServer.resource.annotation.ResourceMethod;
import httpServer.resource.annotation.ResourceUri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.sql.Connection;

/**
 * Created by zhangdekun on 15-4-1-下午3:08.
 */
@ResourceUri("/")
public class ImgResource extends AbstractResource{

    public ImgResource(Object param) {
        super(param);
    }

    private static String getPath(String fileName){
        return Config.imgPosition()+File.separator+fileName;
    }
    @ResourceMethod("/img")
    public Object image() {
        HttpContext context = (HttpContext)super.getParam();
        String path = getPath(context.getName());
        File localFile = new File(path);
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(localFile, "r");
            byte[] buf = new byte[1024];
            ByteBuf bbf = Unpooled.buffer();
            int bytereda = 0;
            while ((bytereda = rf.read(buf)) != -1) {
                bbf.writeBytes(buf,0,bytereda);
            }
            return bbf;
        } catch (Exception e) {
            throw  new RuntimeException("load "+path+" error.");
        }
    }
    public static void main(String[] args) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType("test.html");
        System.out.printf(contentType);
    }
}
