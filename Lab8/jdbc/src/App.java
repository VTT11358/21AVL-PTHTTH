import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:nhanvien.db";

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                System.out.println("Ket noi SQLite thanh cong!");

                // 1. Tao bang NhanVien
                String sqlCreate = """
                    CREATE TABLE IF NOT EXISTS NhanVien (
                        id INTEGER PRIMARY KEY,
                        ten TEXT NOT NULL,
                        chuc_vu TEXT
                    )
                    """;

                stmt.executeUpdate(sqlCreate);

                // 2. Them du lieu
                String[] sqlInsert = {
                    "INSERT OR IGNORE INTO NhanVien " +
                    "VALUES (1, 'Nguyen Van An', 'Quan ly')",

                    "INSERT OR IGNORE INTO NhanVien " +
                    "VALUES (2, 'Tran Thi Binh', 'Ke toan')",

                    "INSERT OR IGNORE INTO NhanVien " +
                    "VALUES (3, 'Le Van Cuong', 'Lap trinh vien')"
                };

                conn.setAutoCommit(false);

                try {
                    for (String sql : sqlInsert) {
                        stmt.executeUpdate(sql);
                    }

                    conn.commit();
                    System.out.println("Them du lieu thanh cong!");

                } catch (SQLException e) {
                    conn.rollback();
                    throw e;

                } finally {
                    conn.setAutoCommit(true);
                }

                // 3. Truy van du lieu
                String sqlSelect =
                    "SELECT id, ten, chuc_vu FROM NhanVien";

                try (ResultSet rs = stmt.executeQuery(sqlSelect)) {

                    System.out.println("\nDANH SACH NHAN VIEN");
                    System.out.println("--------------------------------");

                    while (rs.next()) {
                        System.out.println(
                            rs.getInt("id") + " | " +
                            rs.getString("ten") + " | " +
                            rs.getString("chuc_vu")
                        );
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay JDBC Driver!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Loi SQL!");
            e.printStackTrace();
        }
    }
}
