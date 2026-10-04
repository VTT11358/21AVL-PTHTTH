import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReadBinaryFile {
    public static void loadSV(String src) throws IOException {
        ArrayList<SinhVien> listSV = new ArrayList<>();

        try (DataInputStream dis =
                     new DataInputStream(new FileInputStream(new File(src)))) {

            int size = dis.readInt();

            for (int i = 0; i < size; i++) {
                String mssv = dis.readUTF();
                String name = dis.readUTF();
                int age = dis.readInt();

                int sizeMH = dis.readInt();
                ArrayList<MonHoc> listMH = new ArrayList<>();

                for (int j = 0; j < sizeMH; j++) {
                    String tenMonHoc = dis.readUTF();
                    int tinChi = dis.readInt();
                    double diem = dis.readDouble();

                    listMH.add(new MonHoc(tenMonHoc, tinChi, diem));
                }

                listSV.add(new SinhVien(mssv, name, age, listMH));
            }
        }

        for (SinhVien sv : listSV) {
            System.out.println(sv);
        }
    }

    public static void main(String[] args) throws IOException {
        loadSV("D:/HocJava/a.dat");
    }
}
