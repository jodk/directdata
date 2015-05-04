package httpServer;

import httpServer.resource.Resource;
import org.apache.log4j.Logger;

/**
 * Created by zhangdekun on 15-3-31-下午5:00.
 */
public class DefaultHttpDispatcher implements HttpDispatcher {
    private static Logger log = Logger.getLogger(DefaultHttpDispatcher.class);
    private HttpHandler httpHandler;

    @Override
    public HttpHandler getHandler() {
        return this.httpHandler;
    }

    @Override
    public void dispatch(HttpContext context) {
        String uri = context.getHttpRequest().getUri();
        int index = validUri(uri);
        int nameIndex = uri.lastIndexOf("/");
        String name = uri.substring(nameIndex + 1, index);
        String path = uri.substring(0, nameIndex);

        this.httpHandler = new DefaultHttpHandler(path, name, context);
    }

    /**
     * 验证uri
     * 格式为/xx/xx/xx.type
     * type类型在提供的范围内
     *
     * @param uri
     * @return 分割位置
     */
    private int validUri(String uri) {
        int dotIndex = uri.indexOf(".");
        int questionMarkIndex = uri.indexOf("?");
        int length = uri.length();
        if (dotIndex == -1) {
            log.error("uri invalid:" + uri);
            throw new RuntimeException("uri invalid.");
        } else {
            String mimeType = uri.substring(dotIndex + 1, questionMarkIndex == -1 ? length : questionMarkIndex);
            if (Resource.mimeTypes.getContentType(mimeType) == null) {
                log.error("this mime Type is not supported:" + mimeType);
                throw new RuntimeException("mime type is not supported.");
            }
        }
        return questionMarkIndex == -1 ? length : questionMarkIndex;
    }
}
