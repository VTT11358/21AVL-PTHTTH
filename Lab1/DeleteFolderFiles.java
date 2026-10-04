import java.io.File;

public class DeleteFolderFiles {
    public static boolean deleteFolderFiles(String source) {
        File folder = new File(source);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder khong ton tai: " + source);
            return false;
        }

        File[] listFile = folder.listFiles();

        if (listFile != null) {
            for (File file : listFile) {
                if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Da xoa: " + file.getAbsolutePath());
                    }
                }
            }
        }

        if (folder.delete()) {
            System.out.println("Delete folder thanh cong!");
            return true;
        }

        System.out.println("Khong the xoa folder.");
        return false;
    }

    public static void main(String[] args) {
        deleteFolderFiles("D:/HocJava/TestFolder");
    }
}
