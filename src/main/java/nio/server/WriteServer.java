package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

import static test.TestByteBufferReadWrite.split;

public class WriteServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置 accept方法为非阻塞
        ssc.configureBlocking(false);
        // 注册selector到ssc，SelectionKey 就是将来事件发生后，通过它可以知道事件是哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 事件有四类：accept， connect， read， write
        // 注册selector 关注的事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        System.out.println("register key..." + sscKey);

        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            // select() 没有事件发生时线程阻塞，有事件发生时，线程才会恢复运行
            // 接收过的事件在未处理时不会阻塞，既事件未处理时selector在调用select方法时依然会返回未处理的事件，
            // 调用事件的cancel或channel的accept方法既认为事件已经处理
            selector.select();
            // 处理事件，selectKeys内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 处理key之前要把key从selectedKeys集合中删除，否则下次处理时这个key还会存在在selectedKeys中
                iterator.remove();
                System.out.println("key: " + key);

                if (key.isAcceptable()) {
                    //处理事件
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sc_key = sc.register(selector, 0, null);
//                    sc_key.interestOps(SelectionKey.OP_READ);

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < 5000000; i++) {
                        builder.append("a");
                    }


                    ByteBuffer buffer = Charset.defaultCharset().encode(builder.toString());
                    int write = sc.write(buffer);
                    System.out.println(write);

                    if (buffer.hasRemaining()) {
                        // 关注可写事件
                        sc_key.interestOps(sc_key.interestOps() + SelectionKey.OP_WRITE);
                        // 把未写完的数据挂到key的附件上
                        sc_key.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();

                    int write = sc.write(buffer);
                    System.out.println(write);
                    // 写完buffer中内容后清理掉写事件
                    if (!buffer.hasRemaining()) {
                        key.attach(null);
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }

                }
            }
        }
    }
}
