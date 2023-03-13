package netty.server.session;

import io.netty.channel.Channel;

public interface Session {

    /**
     * 将channel和username绑定，以实现即时通讯
     * @param channel
     * @param username
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     * @param channel
     */
    void unbind(Channel channel);


    //获取属性
    Object getAttribute(Channel channel, String name);

    //设置属性
    void setAttribute(Channel channel, String name, Object value);


    // 根据用户名获取channel
    Channel getChannel(String username);
}
