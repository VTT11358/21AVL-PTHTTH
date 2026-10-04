public class Main {

    public static void main(String[] args) {

       

        SinhVien sv1 = new SinhVien(
                "Nguyen Van An",
                2004,
                "TP. Ho Chi Minh",
                "SV001",
                "Cong nghe thong tin",
                8.8
        );

        SinhVien sv2 = new SinhVien(
                "Tran Thi Binh",
                2003,
                "Dong Nai",
                "SV002",
                "He thong thong tin",
                6.5
        );



        GiangVien gv1 = new GiangVien(
                "Nguyen Van Minh",
                1980,
                "TP. Ho Chi Minh",
                "GV001",
                "Lap trinh Java",
                5000000,
                3.2
        );

        GiangVien gv2 = new GiangVien(
                "Le Thi Hoa",
                1985,
                "Binh Duong",
                "GV002",
                "Co so du lieu",
                5500000,
                2.8
        );




        System.out.println("========== SINH VIEN 1 ==========");

        sv1.hienThiThongTin();


        System.out.println("\n========== SINH VIEN 2 ==========");

        sv2.hienThiThongTin();


        

        System.out.println("\n========== GIANG VIEN 1 ==========");

        gv1.hienThiThongTin();


        System.out.println("\n========== GIANG VIEN 2 ==========");

        gv2.hienThiThongTin();
    }
}