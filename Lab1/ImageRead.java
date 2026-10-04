import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageRead {
    public static byte[] readFile(File path) {
        try (FileInputStream fis = new FileInputStream(path);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024];
            int readNum;

            while ((readNum = fis.read(buf)) != -1) {
                bos.write(buf, 0, readNum);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println("Loi doc file anh: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        File image = new File("D:/HocJava/image.jpg");
        byte[] data = readFile(image);

        if (data != null) {
            System.out.println("Doc anh thanh cong. So byte: " + data.length);
        }
    }
}
