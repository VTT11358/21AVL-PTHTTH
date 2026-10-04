import java.io.File;

public class DeleteFile {
    public static boolean deleteFile(String source) {
        File file = new File(source);

        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("Xoa file thanh cong: " + source);
                return true;
            }
            System.out.println("Khong the xoa file.");
        } else {
            System.out.println("File khong ton tai: " + source);
        }

        return false;
    }

    public static void main(String[] args) {
        deleteFile("D:/HocJava/demo.txt");
    }
}
