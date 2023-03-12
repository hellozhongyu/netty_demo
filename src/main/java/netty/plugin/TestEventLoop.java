package netty.plugin;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

public class TestEventLoop {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup(2);

        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

//        group.next().execute(() ->{
//            System.out.println("hello group thread: " + Thread.currentThread().getName());
//        });

        group.next().scheduleAtFixedRate(() -> {
            System.out.println("hello group thread: " + Thread.currentThread().getName());
        }, 0, 1, TimeUnit.SECONDS);

        System.out.println("hello main: " + Thread.currentThread().getName());
    }
}
