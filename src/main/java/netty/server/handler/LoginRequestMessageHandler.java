package netty.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.LoginRequestMessage;
import netty.message.LoginResponseMessage;
import netty.server.service.UserServiceFactory;
import netty.server.session.SessionFactory;


@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage response;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            response = new LoginResponseMessage(true, "登录成功");
        } else {
            response = new LoginResponseMessage(false, "登录失败");
        }
        ctx.writeAndFlush(response);
    }
}
