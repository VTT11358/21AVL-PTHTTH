import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {
    public static boolean copyFile(String source, String dest) throws IOException {
        File sourceFile = new File(source);
        File destFile = new File(dest);

        if (!sourceFile.exists() || !sourceFile.isFile()) {
            System.out.println("File nguon khong ton tai: " + source);
            return false;
        }

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {

            byte[] arr = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(arr)) != -1) {
                fos.write(arr, 0, bytesRead);
            }
        }

        System.out.println("Copy thanh cong!");
        return true;
    }

    public static void main(String[] args) throws IOException {
        copyFile("D:/HocJava/a.txt", "D:/HocJava/b.txt");
    }
}
