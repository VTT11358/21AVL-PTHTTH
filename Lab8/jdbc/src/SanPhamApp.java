import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SanPhamApp {

    private static final String URL = "jdbc:sqlite:nhanvien.db";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL)) {

                taoBang(conn);
                themDuLieuMau(conn);
                chayMenu(conn);

            }

        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay JDBC Driver!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Loi co so du lieu: " + e.getMessage());
        }

        sc.close();
    }

    // 1. CREATE TABLE
    private static void taoBang(Connection conn)
            throws SQLException {

        String sql = """
            CREATE TABLE IF NOT EXISTS SanPham (
                maSP TEXT PRIMARY KEY,
                tenSP TEXT NOT NULL,
                gia REAL NOT NULL CHECK(gia >= 0),
                soluong INTEGER NOT NULL CHECK(soluong >= 0)
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tao bang SanPham thanh cong!");
        }
    }

    // Them 3 san pham mau bang Transaction
    private static void themDuLieuMau(Connection conn)
            throws SQLException {

        String sql = """
            INSERT OR IGNORE INTO SanPham
            (maSP, tenSP, gia, soluong)
            VALUES (?, ?, ?, ?)
            """;

        conn.setAutoCommit(false);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            String[][] data = {
                {"SP01", "Laptop Dell", "15000000", "10"},
                {"SP02", "Laptop Asus", "18000000", "8"},
                {"SP03", "Chuot Logitech", "500000", "20"}
            };

            for (String[] sp : data) {
                ps.setString(1, sp[0]);
                ps.setString(2, sp[1]);
                ps.setDouble(3, Double.parseDouble(sp[2]));
                ps.setInt(4, Integer.parseInt(sp[3]));
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Kiem tra du lieu mau thanh cong!");

        } catch (SQLException e) {
            conn.rollback();
            throw e;

        } finally {
            conn.setAutoCommit(true);
        }
    }

    // MENU
    private static void chayMenu(Connection conn)
            throws SQLException {

        int chon;

        do {
            System.out.println("\n===== QUAN LY SAN PHAM =====");
            System.out.println("1. Them san pham");
            System.out.println("2. Hien thi tat ca san pham");
            System.out.println("3. Tim san pham theo ten");
            System.out.println("4. Cap nhat gia va so luong");
            System.out.println("5. Xoa san pham");
            System.out.println("6. Thong ke san pham");
            System.out.println("0. Thoat");
            System.out.print("Nhap lua chon: ");

            chon = nhapInt();

            try {
                switch (chon) {
                    case 1 -> themSanPham(conn);
                    case 2 -> hienThiSanPham(conn);
                    case 3 -> timSanPham(conn);
                    case 4 -> capNhatSanPham(conn);
                    case 5 -> xoaSanPham(conn);
                    case 6 -> thongKe(conn);
                    case 0 -> System.out.println("Ket thuc chuong trinh!");
                    default -> System.out.println("Lua chon khong hop le!");
                }

            } catch (SQLException e) {
                System.out.println("Thao tac that bai: " + e.getMessage());
            }

        } while (chon != 0);
    }

    // 2. CREATE: Them san pham
    private static void themSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ma SP: ");
        String ma = sc.nextLine();

        System.out.print("Nhap ten SP: ");
        String ten = sc.nextLine();

        System.out.print("Nhap gia: ");
        double gia = nhapDouble();

        System.out.print("Nhap so luong: ");
        int soLuong = nhapInt();

        if (ten.isBlank() || gia < 0 || soLuong < 0) {
            System.out.println("Du lieu khong hop le!");
            return;
        }

        String sql = """
            INSERT INTO SanPham(maSP, tenSP, gia, soluong)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);
            ps.setString(2, ten);
            ps.setDouble(3, gia);
            ps.setInt(4, soLuong);

            ps.executeUpdate();
            System.out.println("Them san pham thanh cong!");
        }
    }

    // 3. READ: Hien thi tat ca
    private static void hienThiSanPham(Connection conn)
            throws SQLException {

        String sql = "SELECT * FROM SanPham ORDER BY maSP";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            inTieuDe();

            while (rs.next()) {
                inSanPham(rs);
            }
        }
    }

    // Tim san pham theo ten
    private static void timSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ten can tim: ");
        String ten = sc.nextLine();

        String sql = """
            SELECT * FROM SanPham
            WHERE tenSP LIKE ?
            ORDER BY maSP
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");

            try (ResultSet rs = ps.executeQuery()) {
                inTieuDe();

                boolean found = false;

                while (rs.next()) {
                    inSanPham(rs);
                    found = true;
                }

                if (!found) {
                    System.out.println("Khong tim thay san pham!");
                }
            }
        }
    }

    // 4. UPDATE: Cap nhat gia va so luong
    private static void capNhatSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ma SP can sua: ");
        String ma = sc.nextLine();

        System.out.print("Nhap gia moi: ");
        double gia = nhapDouble();

        System.out.print("Nhap so luong moi: ");
        int soLuong = nhapInt();

        if (gia < 0 || soLuong < 0) {
            System.out.println("Gia va so luong phai >= 0!");
            return;
        }

        String sql = """
            UPDATE SanPham
            SET gia = ?, soluong = ?
            WHERE maSP = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, gia);
            ps.setInt(2, soLuong);
            ps.setString(3, ma);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cap nhat thanh cong!");
            } else {
                System.out.println("Khong tim thay ma SP!");
            }
        }
    }

    // 5. DELETE: Xoa san pham
    private static void xoaSanPham(Connection conn)
            throws SQLException {

        System.out.print("Nhap ma SP can xoa: ");
        String ma = sc.nextLine();

        String sql = "DELETE FROM SanPham WHERE maSP = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ma);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Xoa thanh cong!");
            } else {
                System.out.println("Khong tim thay ma SP!");
            }
        }
    }

    // 6. Thong ke nang cao
    private static void thongKe(Connection conn)
            throws SQLException {

        String sql = """
            SELECT COUNT(*) AS soSP,
                   SUM(soluong) AS tongSoLuong,
                   AVG(gia) AS giaTrungBinh,
                   MIN(gia) AS giaThapNhat,
                   MAX(gia) AS giaCaoNhat
            FROM SanPham
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("So mat hang: "
                    + rs.getInt("soSP"));
                System.out.println("Tong so luong: "
                    + rs.getInt("tongSoLuong"));
                System.out.printf("Gia trung binh: %.2f%n",
                    rs.getDouble("giaTrungBinh"));
                System.out.println("Gia thap nhat: "
                    + rs.getDouble("giaThapNhat"));
                System.out.println("Gia cao nhat: "
                    + rs.getDouble("giaCaoNhat"));
            }
        }
    }

    private static void inTieuDe() {
        System.out.println(
            "\nMA SP | TEN SAN PHAM | GIA | SO LUONG"
        );
        System.out.println("----------------------------------------");
    }

    private static void inSanPham(ResultSet rs)
            throws SQLException {

        System.out.printf("%s | %s | %.2f | %d%n",
            rs.getString("maSP"),
            rs.getString("tenSP"),
            rs.getDouble("gia"),
            rs.getInt("soluong"));
    }

    private static int nhapInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Vui long nhap so nguyen: ");
            }
        }
    }

    private static double nhapDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(
                    sc.nextLine().trim()
                );

                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Nhap lai neu khong phai so
            }

            System.out.print("Vui long nhap mot so hop le: ");
        }
    }
}
