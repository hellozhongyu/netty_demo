package netty.plugin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import java.nio.charset.Charset;

public class TestSlice {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        System.out.println(buf.getClass());
        System.out.println(buf.toString(Charset.defaultCharset()));
        // 切片，该过程中没有发生数据复制
        ByteBuf buf1 = buf.slice(0, 5);
        ByteBuf buf2 = buf.slice(5, 5);

        System.out.println(buf1.toString(Charset.defaultCharset()));
        System.out.println(buf2.toString(Charset.defaultCharset()));

        buf1.setByte(0, 'b');
        System.out.println(buf1.toString(Charset.defaultCharset()));
        System.out.println(buf.toString(Charset.defaultCharset()));


//        buf1.release();
//        System.out.println(buf2.toString(Charset.defaultCharset()));
//        System.out.println(buf.toString(Charset.defaultCharset()));
//        System.out.println(buf1.toString(Charset.defaultCharset()));

        ByteBuf buf3 = ByteBufAllocator.DEFAULT.buffer(5);
        ByteBuf buf4 = ByteBufAllocator.DEFAULT.buffer(5);
        CompositeByteBuf cp_buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        buf3.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e'});
        buf4.writeBytes(new byte[]{ 'f', 'g', 'h', 'i', 'j'});
        cp_buffer.addComponents(true, buf3, buf4);
        System.out.println(cp_buffer.toString(Charset.defaultCharset()));

    }
}
