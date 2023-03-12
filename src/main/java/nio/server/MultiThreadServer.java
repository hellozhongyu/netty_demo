package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static test.TestByteBufferReadWrite.bb2str;

public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        // 创建固定的worker去执行读事件
        Worker worker = new Worker("worker-0");


        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();

            while (iter.hasNext()) {
                SelectionKey sscKey = iter.next();
                iter.remove();
                if (sscKey.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    System.out.println("connected..." + sc.getRemoteAddress());
                    System.out.println("before register ..." + sc.getRemoteAddress());
                    worker.register(sc);
                    System.out.println("after register..." + sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {

        private String name;
        private Thread thread;
        private Selector selector;
        private volatile boolean isStart = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!isStart) {
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                isStart = true;
            }

            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 唤醒selector
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select(); // 阻塞
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            sc.configureBlocking(false);
                            System.out.println("read ..." + sc.getRemoteAddress());
                            int read = sc.read(buffer);
                            if (read == -1)
                                key.cancel();
                            else {
                                buffer.flip();
                                System.out.println(bb2str(buffer));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
