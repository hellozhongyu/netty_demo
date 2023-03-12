package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import static test.TestByteBufferReadWrite.split;

public class Server {

    public static void main(String[] args) throws IOException {



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

                if (key.isAcceptable()){
                    ServerSocketChannel ssc_ref = (ServerSocketChannel) key.channel();
                    //处理事件
                    SocketChannel sc_channel = ssc_ref.accept();
                    sc_channel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment
                    // 将 buffer 作为附件关联到selectionKey上
                    SelectionKey scKey = sc_channel.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("channel: " + sc_channel);
                }else if (key.isReadable()){
                    try {
                        SocketChannel sc_channel = (SocketChannel) key.channel();
                        // 获取selectionKey上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 客户端正常断开，read方法返回-1，异常断开channel.read会报错，需要在catch中将key取消掉
                        int read = sc_channel.read(buffer);
                        if (read == -1){
                            key.cancel();
                        }else{
                            split(buffer);
                            if (buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
//                                System.out.println("message: " + StandardCharsets.UTF_8.decode(buffer));
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        key.cancel(); // 因为客户端断开， 需要将key取消 （从selector的keys集合中真正删除key）
                    }
                }

                //取消事件
                //key.cancel();
            }
        }

//        ArrayList<SocketChannel> channels = new ArrayList<>();
//        ByteBuffer buffer = ByteBuffer.allocate(16);
//        while (true) {
//            System.out.println("connecting ...");
//            //默认条件下accept为阻塞 block
//            SocketChannel sc = ssc.accept();
//            if (sc != null){
//                System.out.println("connected ...");
//                //设置 channel的read方法为非阻塞，默认是阻塞 block
//                sc.configureBlocking(false);
//                channels.add(sc);
//            }
//
//            for (SocketChannel channel : channels) {
//
//                System.out.println("before read ... " + channel);
//
//                int read = channel.read(buffer);//默认channel.read是阻塞 block
//
//                if (read > 0) {
//                    buffer.flip();
//                    String message = StandardCharsets.UTF_8.decode(buffer).toString();
//                    System.out.println(message);
//                    buffer.clear();
//                    System.out.println("after read ... " + sc);
//                }
//            }
//        }
    }
}
