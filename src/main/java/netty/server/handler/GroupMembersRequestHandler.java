package netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.GroupMembersRequestMessage;
import netty.message.GroupMembersResponseMessage;
import netty.server.session.GroupSession;
import netty.server.session.GroupSessionFactory;

import java.util.Set;

@ChannelHandler.Sharable
public class GroupMembersRequestHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();

        GroupSession session = GroupSessionFactory.getGroupSession();
        Set<String> members = session.getMembers(groupName);

        if (members != null) {
            ctx.writeAndFlush(new GroupMembersResponseMessage(members));
        } else {
            ctx.writeAndFlush(new GroupMembersResponseMessage(members));
        }
    }
}