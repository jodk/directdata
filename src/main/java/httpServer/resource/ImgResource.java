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
public class ImgResource extends AbstractResource {

    public ImgResource(Object param) {
        super(param);
    }


    @ResourceMethod("/img")
    public Object image() {
        String path = getPath(Config.imgPosition());
        File localFile = new File(path);
        try {
            return super.file2ByteBuf(localFile);
        } catch (Exception e) {
            throw new RuntimeException("load " + path + " error.");
        }
    }
}
