import java.io.File;

public class DeleteFolderRecursive {
    public static boolean deleteFolder(String source) {
        File folder = new File(source);

        if (!folder.exists()) {
            System.out.println("Folder khong ton tai: " + source);
            return false;
        }

        if (folder.isDirectory()) {
            File[] listFile = folder.listFiles();

            if (listFile != null) {
                for (File file : listFile) {
                    if (file.isDirectory()) {
                        deleteFolder(file.getAbsolutePath());
                    } else {
                        file.delete();
                    }
                }
            }
        }

        if (folder.delete()) {
            System.out.println("Delete thanh cong: " + folder.getAbsolutePath());
            return true;
        }

        System.out.println("Khong the xoa: " + folder.getAbsolutePath());
        return false;
    }

    public static void main(String[] args) {
        deleteFolder("D:/HocJava/TestDeleteDir");
    }
}
