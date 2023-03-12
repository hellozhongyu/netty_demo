package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        buffer.flip();

        //buffer.get(i)不改变position的位置
        System.out.println((char) buffer.get(0));

        //buffer.get() position+=1
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());

        //mark方法标记position的当前位置，当调用reset方法时，position回到mark标记的位置
        buffer.mark();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());

        //mark方法标记position的当前位置，当调用reset方法时，position回到mark标记的位置
        buffer.reset();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());


        /**
         *
         *
         * 创建ByteBuffer、ByteBuffer转String
         */
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello");
        System.out.println(bb2str(buffer1));

        String str = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str);


        ByteBuffer buffer2 = ByteBuffer.wrap("hello".getBytes());
        System.out.println(bb2str(buffer2));


        /**
         *
         * 分散读取
         */
        try (FileChannel channel = new RandomAccessFile("data.txt", "rw").getChannel()) {

            ByteBuffer buffer3 = ByteBuffer.allocate(3);
            ByteBuffer buffer4 = ByteBuffer.allocate(3);
            ByteBuffer buffer5 = ByteBuffer.allocate(5);

            //分散读取
            channel.read(new ByteBuffer[]{buffer3, buffer4, buffer5});
            System.out.println(bb2StrClear(buffer3));
            System.out.println(bb2StrClear(buffer4));
            System.out.println(bb2StrClear(buffer5));

        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         *
         * 集中写入
         */
        try (FileChannel channel = new RandomAccessFile("data1.txt", "rw").getChannel()) {

            ByteBuffer buffer6 = StandardCharsets.UTF_8.encode("hello");
            ByteBuffer buffer7 = StandardCharsets.UTF_8.encode("world");
            ByteBuffer buffer8 = StandardCharsets.UTF_8.encode("你好");

            //集中写入
            channel.write(new ByteBuffer[]{buffer6, buffer7, buffer8});

        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         *
         * 黏包，半包拆解
         */
        ByteBuffer source = ByteBuffer.allocate(34);
        ByteBuffer source1 = ByteBuffer.allocate(14);
        source1.put("hello,zhangsan".getBytes());
        split(source1);
        source.put("hello,zhangsan\nhello,lisi\nhello,".getBytes());
        split(source);
        source.put("netty\n".getBytes());
        split(source);
    }


    public static void split(ByteBuffer source){
        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            //找一条完整信息
            if (source.get(i) == '\n'){
                int length = i - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);

                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                source.get();
                System.out.println(bb2StrClear(target));
            }

        }

        source.compact();

    }

    public static String bb2StrClear(ByteBuffer buffer){
        return bb2str((ByteBuffer) buffer.clear());
    }


    public static String bb2str(ByteBuffer buffer){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = buffer.position(); i < buffer.limit(); i++) {
            stringBuilder.append((char) buffer.get(i));
        }
        return stringBuilder.toString();
    }
}
