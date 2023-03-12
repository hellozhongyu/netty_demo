package netty.plugin;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TestNettyFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventLoop eventLoop = new NioEventLoopGroup().next();

        Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 80;
            }
        });

        System.out.println("等待结果...");
        //同步等待结果
//        System.out.println("结果是： " + future.get());

        //异步等待结果
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                System.out.println("结果是： " + future.getNow());
            }
        });
    }
}
