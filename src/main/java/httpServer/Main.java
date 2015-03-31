package httpServer;

import org.apache.log4j.Logger;

/**
 * Created by zhangdekun on 15-3-31-上午11:18.
 */
public class Main {
    private static  Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        int port = Config.httpServerPort();
        try {
            port = Integer.parseInt(args[0]);
        }catch (Exception e){

        }
        HttpServer server = new HttpServer(port);
        try {
            server.start();
            log.info("http server(port:"+port+") start ...");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
