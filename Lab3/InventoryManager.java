package Lab3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryManager {

    private static final Path INVENTORY_FILE =
            Path.of(
                    "data",
                    "inventory.csv"
            );

    private static final Path REPORT_FILE =
            Path.of(
                    "data",
                    "inventory-report.txt"
            );

    public static void main(String[] args) {

        Scanner scanner =
                new Scanner(
                        System.in,
                        StandardCharsets.UTF_8
                );

        // =========================
        // 1. NHAP SAN PHAM
        // =========================

        List<Product> products =
                nhapDanhSach(scanner);

        if (products.isEmpty()) {

            System.out.println(
                    "Khong co san pham hop le."
            );

            return;
        }

        // =========================
        // 2. LUU CSV
        // =========================

        luuCsv(products);

        // =========================
        // 3. DOC LAI CSV
        // =========================

        List<Product> productsDocLai =
                docCsv();

        if (productsDocLai.isEmpty()) {

            System.out.println(
                    "Khong doc duoc san pham tu CSV."
            );

            return;
        }

        // =========================
        // 4. HIEN THI
        // =========================

        hienThiDanhSach(
                productsDocLai
        );

        // =========================
        // 5. TINH TONG
        // =========================

        double total =
                tinhTongTonKho(
                        productsDocLai
                );

        // =========================
        // 6. TIM MAX
        // =========================

        Product maxProduct =
                timSanPhamTonKhoCaoNhat(
                        productsDocLai
                );

        System.out.printf(
                "%nTong gia tri ton kho: %,.0f VND%n",
                total
        );

        System.out.printf(
                "San pham ton kho cao nhat: "
                        + "%s - %s (%,.0f VND)%n",
                maxProduct.getCode(),
                maxProduct.getName(),
                maxProduct.inventoryValue()
        );

        // =========================
        // 7. GHI BAO CAO
        // =========================

        ghiBaoCao(
                productsDocLai,
                total,
                maxProduct
        );
    }

    // ==================================================
    // NHAP DANH SACH
    // ==================================================

    private static List<Product> nhapDanhSach(
            Scanner scanner
    ) {

        List<Product> products =
                new ArrayList<>();

        System.out.print(
                "Nhap so luong san pham: "
        );

        int n;

        try {

            n = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            if (n <= 0) {

                System.out.println(
                        "So luong phai > 0."
                );

                return products;
            }

        } catch (NumberFormatException e) {

            System.out.println(
                    "So luong khong hop le."
            );

            return products;
        }

        // Nhap tung san pham
        for (int i = 1; i <= n; i++) {

            System.out.println(
                    "\n--- San pham "
                            + i
                            + " ---"
            );

            System.out.print(
                    "Ma san pham: "
            );

            String code =
                    scanner.nextLine().trim();

            System.out.print(
                    "Ten san pham: "
            );

            String name =
                    scanner.nextLine().trim();

            System.out.print(
                    "Don gia: "
            );

            String priceText =
                    scanner.nextLine().trim();

            System.out.print(
                    "So luong: "
            );

            String quantityText =
                    scanner.nextLine().trim();

            try {

                double price =
                        Double.parseDouble(
                                priceText
                        );

                int quantity =
                        Integer.parseInt(
                                quantityText
                        );

                Product product =
                        new Product(
                                code,
                                name,
                                price,
                                quantity
                        );

                products.add(product);

                System.out.println(
                        "Them san pham thanh cong."
                );

            } catch (
                    NumberFormatException e
            ) {

                System.err.println(
                        "Tu choi: gia hoac so luong "
                                + "khong phai so."
                );

            } catch (
                    IllegalArgumentException e
            ) {

                System.err.println(
                        "Tu choi: "
                                + e.getMessage()
                );
            }
        }

        return products;
    }

    // ==================================================
    // LUU CSV
    // ==================================================

    private static void luuCsv(
            List<Product> products
    ) {

        try {

            Files.createDirectories(
                    INVENTORY_FILE.getParent()
            );

            try (
                    BufferedWriter writer =
                            Files.newBufferedWriter(
                                    INVENTORY_FILE,
                                    StandardCharsets.UTF_8
                            )
            ) {

                writer.write(
                        "ma,ten,donGia,soLuong"
                );

                writer.newLine();

                for (
                        Product product :
                        products
                ) {

                    writer.write(
                            product.toCsv()
                    );

                    writer.newLine();
                }
            }

            System.out.println(
                    "\nDa luu file: "
                            + INVENTORY_FILE
                            .toAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Khong ghi duoc "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage()
            );
        }
    }

    // ==================================================
    // DOC CSV
    // ==================================================

    private static List<Product> docCsv() {

        List<Product> products =
                new ArrayList<>();

        try (
                BufferedReader reader =
                        Files.newBufferedReader(
                                INVENTORY_FILE,
                                StandardCharsets.UTF_8
                        )
        ) {

            // Bo qua header
            String header =
                    reader.readLine();

            if (header == null) {

                System.err.println(
                        "File "
                                + INVENTORY_FILE
                                + " rong."
                );

                return products;
            }

            String line;

            int lineNumber = 1;

            while (
                    (line =
                            reader.readLine())
                            != null
            ) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",", -1);

                // Kiem tra so cot
                if (parts.length != 4) {

                    System.err.println(
                            "Bo qua dong "
                                    + lineNumber
                                    + ": phai co 4 cot."
                    );

                    continue;
                }

                try {

                    Product product =
                            new Product(
                                    parts[0].trim(),
                                    parts[1].trim(),
                                    Double.parseDouble(
                                            parts[2].trim()
                                    ),
                                    Integer.parseInt(
                                            parts[3].trim()
                                    )
                            );

                    products.add(product);

                } catch (
                        NumberFormatException e
                ) {

                    System.err.println(
                            "Bo qua dong "
                                    + lineNumber
                                    + ": du lieu so "
                                    + "khong hop le."
                    );

                } catch (
                        IllegalArgumentException e
                ) {

                    System.err.println(
                            "Bo qua dong "
                                    + lineNumber
                                    + ": "
                                    + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Khong doc duoc "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage()
            );
        }

        return products;
    }

    // ==================================================
    // HIEN THI DANH SACH
    // ==================================================

    private static void hienThiDanhSach(
            List<Product> products
    ) {

        System.out.println(
                "\n===== DANH SACH TON KHO ====="
        );

        for (
                Product product :
                products
        ) {

            System.out.println(
                    product
            );

            System.out.printf(
                    "  Don gia: %,.0f VND%n",
                    product.getUnitPrice()
            );

            System.out.println(
                    "  So luong: "
                            + product.getQuantity()
            );
        }
    }

    // ==================================================
    // TINH TONG TON KHO
    // ==================================================

    private static double tinhTongTonKho(
            List<Product> products
    ) {

        double total = 0;

        for (
                Product product :
                products
        ) {

            total +=
                    product.inventoryValue();
        }

        return total;
    }

    // ==================================================
    // TIM SAN PHAM CO GIA TRI CAO NHAT
    // ==================================================

    private static Product timSanPhamTonKhoCaoNhat(
            List<Product> products
    ) {

        Product max =
                products.get(0);

        for (
                Product product :
                products
        ) {

            if (
                    product.inventoryValue()
                            > max.inventoryValue()
            ) {

                max = product;
            }
        }

        return max;
    }

    // ==================================================
    // GHI BAO CAO
    // ==================================================

    private static void ghiBaoCao(
            List<Product> products,
            double total,
            Product maxProduct
    ) {

        try {

            Files.createDirectories(
                    REPORT_FILE.getParent()
            );

            try (
                    BufferedWriter writer =
                            Files.newBufferedWriter(
                                    REPORT_FILE,
                                    StandardCharsets.UTF_8
                            )
            ) {

                writer.write(
                        "BAO CAO TON KHO"
                );

                writer.newLine();

                writer.write(
                        "=============================="
                );

                writer.newLine();

                writer.write(
                        "So san pham: "
                                + products.size()
                );

                writer.newLine();

                writer.write(
                        "Tong gia tri ton kho: "
                                + "%,.0f VND".formatted(
                                total
                        )
                );

                writer.newLine();

                writer.write(
                        "San pham co gia tri "
                                + "ton kho cao nhat: "
                                + maxProduct.getCode()
                                + " - "
                                + maxProduct.getName()
                );

                writer.newLine();

                writer.write(
                        "Gia tri: "
                                + "%,.0f VND".formatted(
                                maxProduct
                                        .inventoryValue()
                        )
                );

                writer.newLine();
            }

            System.out.println(
                    "\nDa ghi bao cao: "
                            + REPORT_FILE
                            .toAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Khong ghi duoc "
                            + REPORT_FILE
                            + ": "
                            + e.getMessage()
            );
        }
    }
}
