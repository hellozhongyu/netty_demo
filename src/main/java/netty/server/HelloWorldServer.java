package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

public class HelloWorldServer {

   private static void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try{

            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    // 调整系统的接受缓冲区(滑动窗口)
                    // .option(ChannelOption.SO_RCVBUF, 10)
                    // 调整netty的接受缓冲区(滑动窗口)
//                     .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16))
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // FixedLengthFrameDecoder：定长请求解码器
                            // ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                            // LineBasedFrameDecoder：根据换行符做为不同请求的分隔 linux: \n  windows: \r\n
                            // ch.pipeline().addLast(new LineBasedFrameDecoder(1024*1024));
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                    System.out.println(msg.uri());

                                    DefaultFullHttpResponse response =
                                            new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);

                                    byte[] bytes = "<h1>hello world!<h1>".getBytes();
                                    response.headers().setInt(CONTENT_LENGTH, bytes.length);
                                    response.content().writeBytes(bytes);

                                    ctx.writeAndFlush(response);
                                }
                            });
                        }
                    })
                    .bind(8080)
                    .sync();

            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        start();
    }
}
