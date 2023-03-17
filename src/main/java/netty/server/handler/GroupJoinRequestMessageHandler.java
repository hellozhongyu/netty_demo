package netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.GroupCreateResponseMessage;
import netty.message.GroupJoinRequestMessage;
import netty.message.GroupJoinResponseMessage;
import netty.server.session.Group;
import netty.server.session.GroupSession;
import netty.server.session.GroupSessionFactory;

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String member = msg.getUsername();

        GroupSession session = GroupSessionFactory.getGroupSession();
        Group group = session.joinMember(groupName, member);

        if (group != null) {
            session.getMembersChannel(groupName);
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, member + "加入成功"));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, groupName + "不存在"));
        }
    }
}
