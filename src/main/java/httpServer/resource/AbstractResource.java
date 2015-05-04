package httpServer.resource;

import httpServer.HttpContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by zhangdekun on 15-4-3-下午6:39.
 */
public abstract  class AbstractResource {
    private final Object param;

    public Object getParam() {
        return param;
    }

    public AbstractResource(Object param) {
        this.param = param;
    }

    protected ByteBuf file2ByteBuf(File file){
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(file, "r");
            byte[] buf = new byte[1024];
            ByteBuf bbf = Unpooled.buffer();
            int count = 0;
            while ((count = rf.read(buf)) != -1) {
                bbf.writeBytes(buf,0,count);
            }
            return bbf;
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    /**
     * 获得资源名称
     * @return
     */
    protected String getName(){
        HttpContext context = (HttpContext)getParam();
        return context.getName();
    }
    protected  String getPath(String type){
        return type+File.separator+getName();
    }
}
