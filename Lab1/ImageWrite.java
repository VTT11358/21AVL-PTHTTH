import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageWrite {
    public static void saveFile(File path, String fileType, byte[] imageBytes) {
        try {
            BufferedImage image =
                    ImageIO.read(new ByteArrayInputStream(imageBytes));

            ImageIO.write(image, fileType, path);

            System.out.println("Ghi file anh thanh cong: " + path.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Loi ghi file anh: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        System.out.println("ImageWrite: goi saveFile() voi byte[] cua anh.");
    }
}
