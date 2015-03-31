package httpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by zhangdekun on 15-3-31-上午11:49.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final String DECODER = "decoder";
    private static final String ENCODER = "encoder";
    private static final String DEFLATER = "deflater";
    private static final String HANDLER = "handler";
    private final HttpDispatcher dispatcher;

    public HttpServerInitializer(HttpDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (HttpServer.isSSL) {
            //TODO
        }
        /**
         * http request 解码器
         */
        pipeline.addLast(DECODER, new HttpRequestDecoder());
        /**
         * http response 编码器
         */
        pipeline.addLast(ENCODER, new HttpResponseEncoder());
        /**
         * 压缩
         */
        pipeline.addLast(DEFLATER, new HttpContentCompressor());

        pipeline.addLast(HANDLER, new HttpServerHandler(this.dispatcher));
    }
}
