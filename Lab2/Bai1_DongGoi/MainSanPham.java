public class MainSanPham {
    public static void main(String[] args) {

        // Tao san pham
        SanPham sp1 = new SanPham(
                "SP01",
                "Laptop Dell",
                15000000,
                5
        );

        SanPham sp2 = new SanPham(
                "SP02",
                "Chuot Logitech",
                500000,
                10
        );

        // Hien thi thong tin ban dau
        System.out.println("===== THONG TIN BAN DAU =====");

        sp1.hienThiThongTin();
        sp2.hienThiThongTin();

        // Nhap them hang cho san pham 1
        System.out.println("===== NHAP THEM HANG =====");

        sp1.nhapHang(3);
        sp1.hienThiThongTin();

        // Thu ban hang thanh cong
        System.out.println("===== BAN HANG THANH CONG =====");

        sp1.banHang(2);
        sp1.hienThiThongTin();

        // Thu ban so luong lon hon ton kho
        System.out.println("===== BAN QUA SO LUONG TON KHO =====");

        sp1.banHang(100);
        sp1.hienThiThongTin();
    }
}