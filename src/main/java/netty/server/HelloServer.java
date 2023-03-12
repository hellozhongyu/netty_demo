package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {

    public static void main(String[] args) {

        // 启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                // BossEventLoop, WorkerEventLoop(selector, thread), group 组
                .group(new NioEventLoopGroup())
                // 选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // boss 负责处理连接， worker(child) 负责处理读写，决定了worker(child) 能执行哪些操作 (handler)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 添加具体 handler
                        // 将ByteBuf转换为字符串
                        ch.pipeline().addLast(new StringDecoder());
                        // 自定义handler
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换好的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 绑定监听端口
                .bind(8080);
    }
}
