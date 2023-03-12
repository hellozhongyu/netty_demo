package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class EventLoopServer {

    public static void main(String[] args) {

        EventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // boss 只负责 ServerSocketChannel 上的 accept 事件；  worker 只负责 socketChannel 上的读写
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline()
                                .addLast("handler-1", new ChannelInboundHandlerAdapter() {
                                    @Override                                           // ByteBuf
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("current thread: "
                                                + Thread.currentThread().getName()
                                                + buf.toString(Charset.defaultCharset()));
                                        ctx.fireChannelRead(msg); // 让消息传递给下一个handler
                                    }
                                })
                                .addLast(group, "handler-2", new ChannelInboundHandlerAdapter() {
                                    @Override                                           // ByteBuf
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("current thread: "
                                                + Thread.currentThread().getName()
                                                + buf.toString(Charset.defaultCharset()));
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
