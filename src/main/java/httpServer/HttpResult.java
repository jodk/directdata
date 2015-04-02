package httpServer;

import java.io.Serializable;

/**
 * Created by zhangdekun on 15-3-31-下午4:29.
 */
public class HttpResult implements Serializable{
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
