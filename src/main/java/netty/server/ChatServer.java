package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import netty.message.PongMessage;
import netty.protocol.MessageCodecSharable;
import netty.protocol.ProtocolFrameDecoder;
import netty.server.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServer {

    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) {


        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
        GroupMembersRequestHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
        QuitHandler QUIT_HANDLER = new QuitHandler();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // 如果 5S 内没有收到channel的数据， 会出发一个 IdleState#READER_IDLE 事件
                            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                            ch.pipeline().addLast(new ChannelDuplexHandler(){
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    // 触发了读空闲事件
                                    if (event.state() == IdleState.READER_IDLE){
                                        log.debug("已经 5S 没有读到数据了");
//                                        ctx.writeAndFlush(new PongMessage());
                                    }
                                    super.userEventTriggered(ctx, evt);
                                }
                            });
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(LOGIN_HANDLER);
                            ch.pipeline().addLast(CHAT_HANDLER);
                            ch.pipeline().addLast(GROUP_CREATE_HANDLER);
                            ch.pipeline().addLast(GROUP_JOIN_HANDLER);
                            ch.pipeline().addLast(GROUP_QUIT_HANDLER);
                            ch.pipeline().addLast(GROUP_MEMBERS_HANDLER);
                            ch.pipeline().addLast(GROUP_CHAT_HANDLER);
                            ch.pipeline().addLast(QUIT_HANDLER);
                        }
                    });

            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
