package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Random;

public class HelloWorldClient {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            send();
        }
    }

    private static void send(){

        NioEventLoopGroup group = new NioEventLoopGroup();

        try{

            ChannelFuture channelFuture = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buf = sendMessageFixtLength(ctx);
                                    ctx.writeAndFlush(buf);
                                    ctx.channel().close();
                                }
                            });
                        }
                    })
                    .connect("localhost", 8080).sync();

            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    private static ByteBuf sendMessage(ChannelHandlerContext ctx){
        ByteBuf buf = ctx.alloc().buffer(16);
        buf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
        return buf;
    }

    private static ByteBuf sendMessageFixtLength(ChannelHandlerContext ctx){
        ByteBuf buf = ctx.alloc().buffer();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int fLen = random.nextInt(11);
            buf.writeBytes(fillLenStr(i+"", "_", fLen, 10).getBytes());
        }
        return buf;
    }

    private static String fillLenStr(String strVaild, String strInvaild, int lenVaild, int totalLen){
        String strVaildFinal = "";
        for (int i = 0; i < lenVaild; i++) {
            strVaildFinal += strVaild;
        }

        String strInvaildFinal = "";
        for (int i = 0; i < totalLen-lenVaild; i++) {
            strInvaildFinal += strInvaild;
        }

        return strVaildFinal+strInvaildFinal;
    }
}
