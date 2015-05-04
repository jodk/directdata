package httpServer.config;

import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by zhangdekun on 15-3-31-上午10:53.
 */
public class Config {
    private static Logger log = Logger.getLogger(Config.class);
    private static final Properties pro = new Properties();
    private static final String name = "conf/config.properties";
    static {
        try {
            pro.load(Config.class.getClassLoader().getResourceAsStream(name));
        } catch (Exception e) {
            log.error("load config properties fail : " + e.getMessage());
        }
    }
    public static int httpServerPort() {
        int port = 8080;
        try {
            port = Integer.parseInt(pro.getProperty("http.server.port", "8080"));
        } catch (Exception e) {
            log.error("get http server port fail:" + e.getMessage());
        }
        return port;
    }
    public static String imgPosition() {
        try {
            return pro.getProperty("position.img", "");
        } catch (Exception e) {
            log.error("get img position fail:" + e.getMessage());
        }
        return "";
    }
    public static void main(String[] args) {

        System.out.println(Config.imgPosition());
    }
}
