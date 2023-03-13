package netty.plugin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import netty.message.LoginRequestMessage;
import netty.protocol.MessageCodec;
import netty.protocol.MessageCodecSharable;

import java.util.ArrayList;


public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
//                new MessageCodec()
                new MessageCodecSharable()
        );

        // encode
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        embeddedChannel.writeOutbound(message);

        // decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
//        new MessageCodec().encodeTest(null, message, buf);
        ArrayList<Object> list = new ArrayList<>();
        new MessageCodecSharable().encodeTest(null, message, list);

        embeddedChannel.writeInbound((ByteBuf) list.get(0));
    }
}
