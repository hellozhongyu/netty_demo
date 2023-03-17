package netty.server.handler;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.server.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(QuitHandler.class);

    //连接断开时触发 inactive 事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug(" {} 已经断开", ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug(" {} 已经断开, 异常信息：{}", ctx.channel(), cause.getMessage());
    }
}
