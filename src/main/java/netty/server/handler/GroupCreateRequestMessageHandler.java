package netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.GroupCreateRequestMessage;
import netty.message.GroupCreateResponseMessage;
import netty.server.session.Group;
import netty.server.session.GroupSession;
import netty.server.session.GroupSessionFactory;

import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        //群管理器
        GroupSession session = GroupSessionFactory.getGroupSession();
        Group group = session.createGroup(groupName, members);

        if (group == null) {
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }
    }
}
