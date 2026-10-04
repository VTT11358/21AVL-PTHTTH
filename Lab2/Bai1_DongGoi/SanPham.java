public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private double donGia;
    private int soLuong;

    public SanPham(String maSanPham, String tenSanPham,
                   double donGia, int soLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public double tinhThanhTien() {
        return donGia * soLuong;
    }

    public void nhapHang(int soLuongNhap) {
        if (soLuongNhap > 0) {
            soLuong += soLuongNhap;
            System.out.println("Nhap hang thanh cong!");
        } else {
            System.out.println("So luong nhap phai lon hon 0!");
        }
    }

    public boolean banHang(int soLuongBan) {
        if (soLuongBan <= 0) {
            System.out.println("So luong ban phai lon hon 0!");
            return false;
        }

        if (soLuongBan > soLuong) {
            System.out.println("Khong du so luong ton kho!");
            return false;
        }

        soLuong -= soLuongBan;
        System.out.println("Ban hang thanh cong!");
        return true;
    }

    public void hienThiThongTin() {
        System.out.println("Ma san pham: " + maSanPham);
        System.out.println("Ten san pham: " + tenSanPham);
        System.out.println("Don gia: " + donGia);
        System.out.println("So luong ton kho: " + soLuong);
        System.out.println("Thanh tien: " + tinhThanhTien());
        System.out.println("----------------------------");
    }
}