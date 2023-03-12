package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class EchoServer {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = msg instanceof ByteBuf ? (ByteBuf) msg : null;
                                System.out.println("hello, i am server, i get msg:" + buf.toString(Charset.defaultCharset()));
                                super.channelRead(ctx, msg);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = msg instanceof ByteBuf ? (ByteBuf) msg : null;
                                ByteBuf buf1 = ctx.alloc().buffer(50);
                                buf1.writeBytes(("hello, i am server, i get msg:" + buf.toString(Charset.defaultCharset())).getBytes());
                                ctx.writeAndFlush(buf1);
                                buf.release();
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
