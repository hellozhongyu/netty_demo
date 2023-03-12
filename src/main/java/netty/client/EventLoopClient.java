package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture ch = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞， main发起调用， 真正执行 connect 的是nio线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 同步阻塞， 此处会阻塞main线程，等待连接完成
//        ch.sync();
//        Channel channel = ch.channel();
//        System.out.println(channel);
//        System.out.println("");

        // 是用addListener(回调对象) 方法异步处理结果
        ch.addListener(new ChannelFutureListener() {
            @Override
            // 在nio 线程连接吉恩里好之后，会调用 operationComplete
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                System.out.println(channel);
                channel.writeAndFlush(" hello world");
            }
        });
    }
}
