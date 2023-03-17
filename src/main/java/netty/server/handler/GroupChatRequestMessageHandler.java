package netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.*;
import netty.server.session.*;

import java.util.List;


@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String content = msg.getContent();
        String from = msg.getFrom();

        GroupSession GroupSession = GroupSessionFactory.getGroupSession();
        List<Channel> channels = GroupSession.getMembersChannel(groupName);

        try{
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupChatResponseMessage(from, content));
            }
            ctx.writeAndFlush(new GroupChatResponseMessage(true, "发送成功"));
        }catch (Exception e) {
            e.printStackTrace();
            ctx.writeAndFlush(new GroupChatResponseMessage(false, e.getMessage()));
        }
    }
}
