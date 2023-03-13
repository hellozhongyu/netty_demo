package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.message.LoginRequestMessage;
import netty.message.LoginResponseMessage;
import netty.protocol.MessageCodecSharable;
import netty.protocol.ProtocolFrameDecoder;
import netty.server.service.UserServiceFactory;

public class ChatServer {

    public static void main(String[] args) {


        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
                                    String username = msg.getUsername();
                                    String password = msg.getPassword();
                                    boolean login = UserServiceFactory.getUserService().login(username, password);
                                    LoginResponseMessage response;
                                    if (login) {
                                        response = new LoginResponseMessage(true, "登录成功");
                                    } else {
                                        response = new LoginResponseMessage(false, "登录失败");
                                    }
                                    ctx.writeAndFlush(response);
                                }
                            });
                        }
                    });

            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
