package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        // 客户端启动类
        new Bootstrap()
                // 添加EventLoop
                .group(new NioEventLoopGroup())
                // 选择客户端的 channel 实现
                .channel(NioSocketChannel.class)
                // 添加handler
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel()
                .writeAndFlush("hello world");
    }
}
