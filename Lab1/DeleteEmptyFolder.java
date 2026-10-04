import java.io.File;

public class DeleteEmptyFolder {
    public static boolean deleteEmptyFolder(String source) {
        File folder = new File(source);

        if (folder.exists() && folder.isDirectory()) {
            if (folder.delete()) {
                System.out.println("Xoa folder thanh cong: " + source);
                return true;
            }
            System.out.println("Khong the xoa folder. Folder phai rong.");
        } else {
            System.out.println("Folder khong ton tai: " + source);
        }

        return false;
    }

    public static void main(String[] args) {
        deleteEmptyFolder("D:/HocJava/TestFolder");
    }
}
