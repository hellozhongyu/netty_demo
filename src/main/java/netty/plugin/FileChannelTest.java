package netty.plugin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    public static void main(String[] args) {


        try (
                FileChannel from = new FileInputStream("D:\\大文件\\境内外国人轨迹维度应用列表输出.zip").getChannel();
                FileChannel to = new FileOutputStream("D:\\大文件\\test.zip").getChannel()
        ) {
            //高效，零拷贝，一次最多传2G数据，如果from超过2G，传完之后to文件只会有2G
//            from.transferTo(0, from.size(), to);

            long size = from.size();

            for (long left = 0; left < size;){
                left += from.transferTo(left, size-left, to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}