package websocketServer;

import httpServer.config.Config;
import org.apache.log4j.Logger;

/**
 * Created by zhangdekun on 15-3-31-上午11:18.
 */
public class Main {
    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        int port = Config.websoketServerPort();
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {

        }
        WebsocketServer server = new WebsocketServer(port);
        try {
            server.start();
            log.info("websoket server listening on" + port + " start ...");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
