import java.io.File;

public class FindFile {
    public static void findFile(String source, String key) {
        File file = new File(source);

        if (!file.exists()) {
            System.out.println("Source khong ton tai: " + source);
            return;
        }

        if (file.isFile()) {
            if (file.getName().endsWith(key)) {
                System.out.println(file.getAbsolutePath());
            }
            return;
        }

        File[] listFile = file.listFiles();

        if (listFile != null) {
            for (File f : listFile) {
                findFile(f.getAbsolutePath(), key);
            }
        }
    }

    public static void main(String[] args) {
        // Tim cac file co duoi .txt
        findFile("D:/HocJava", ".txt");
    }
}
