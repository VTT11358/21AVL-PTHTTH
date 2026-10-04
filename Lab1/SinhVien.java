import java.io.Serializable;
import java.util.ArrayList;

public class SinhVien implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mssv;
    private String ten;
    private int tuoi;
    private ArrayList<MonHoc> listMH;

    public SinhVien(String mssv, String ten, int tuoi, ArrayList<MonHoc> listMH) {
        this.mssv = mssv;
        this.ten = ten;
        this.tuoi = tuoi;
        this.listMH = listMH;
    }

    public String getMssv() {
        return mssv;
    }

    public String getTen() {
        return ten;
    }

    public int getTuoi() {
        return tuoi;
    }

    public ArrayList<MonHoc> getListMH() {
        return listMH;
    }

    @Override
    public String toString() {
        return "SinhVien{mssv='" + mssv + "', ten='" + ten
                + "', tuoi=" + tuoi + ", listMH=" + listMH + "}";
    }
}
