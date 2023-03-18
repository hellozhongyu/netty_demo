package netty.plugin;


import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import netty.message.LoginRequestMessage;
import netty.protocol.MessageCodecSharable;

import java.util.ArrayList;

public class EmbeddedChannelTest {

    public static void main(String[] args) throws Exception {
        MessageCodecSharable codec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(loggingHandler, codec, loggingHandler);

        LoginRequestMessage msg = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(msg);

        ArrayList<Object> list = new ArrayList<>();
        codec.encodeTest(null, msg, list);

        channel.writeInbound(list.get(0));
    }
}
