package netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.GroupQuitRequestMessage;
import netty.message.GroupQuitResponseMessage;
import netty.server.session.Group;
import netty.server.session.GroupSession;
import netty.server.session.GroupSessionFactory;

@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String member = msg.getUsername();

        GroupSession session = GroupSessionFactory.getGroupSession();
        Group group = session.removeMember(groupName, member);

        if (group != null) {
            session.getMembersChannel(groupName);
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, member + "加入成功"));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, groupName + "不存在"));
        }
    }
}
