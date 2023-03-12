package test;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;




@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(10);

            while (true) {
                int len = channel.read(buffer);
                System.out.println("读取到的字节 " + len);
                if (len == -1)
                    break;

                buffer.flip();

                while (buffer.hasRemaining()) {

                    byte b = buffer.get();
                    System.out.println("actual byte " + (char) b);

                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
