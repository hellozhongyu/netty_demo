package netty.plugin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestLengthFieldDecoder {

    public static void main(String[] args) {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(
                        1024, 0, 4, 1, 4
                ),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "hello world");
        send(buf, "hello server");
        embeddedChannel.writeInbound(buf);
    }

    private static void send(ByteBuf buf, String msg) {
        int msgLen = msg.length();
        buf.writeInt(msgLen);
        buf.writeByte(97);
        buf.writeBytes(msg.getBytes());
    }
}
