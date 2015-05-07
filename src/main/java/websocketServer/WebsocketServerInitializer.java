package websocketServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


/**
 * Created by zhangdekun on 15-5-7-上午9:44.
 */
public class WebsocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("decoder", new HttpRequestDecoder());
        socketChannel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast("encoder", new HttpResponseEncoder());
        socketChannel.pipeline().addLast("handshake", new WebSocketServerProtocolHandler("/websoket", "", true));
        socketChannel.pipeline().addLast(new WebSocketHandler());
    }
}
