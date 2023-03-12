package netty.plugin;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException {

        EventLoop eventLoop = new NioEventLoopGroup().next();

        DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);

        new Thread(()->{
            System.out.println("开始计算");
            try {
                int i = 1/0;
                Thread.sleep(1000);
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();

        System.out.println("等待结果...");
        try {
            System.out.println("结果是： " + promise.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
