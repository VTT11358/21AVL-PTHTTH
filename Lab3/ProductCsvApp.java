package Lab3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProductCsvApp {

    public static void main(String[] args) {

        Path input = Path.of(
                "data",
                "products.csv"
        );

        Path report = Path.of(
                "data",
                "report.txt"
        );

        List<Product> products =
                new ArrayList<>();

        // =========================
        // DOC CSV
        // =========================

        try (
                BufferedReader reader =
                        Files.newBufferedReader(
                                input,
                                StandardCharsets.UTF_8
                        )
        ) {

            // Bo qua dong tieu de
            reader.readLine();

            String line;

            int lineNumber = 1;

            while (
                    (line = reader.readLine())
                            != null
            ) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",", -1);

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
                        IllegalArgumentException e
                ) {

                    System.err.println(
                            "Dong "
                                    + lineNumber
                                    + " khong hop le: "
                                    + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Khong doc duoc CSV: "
                            + e.getMessage()
            );

            return;
        }

        // =========================
        // HIEN THI
        // =========================

        double total = 0;

        System.out.println(
                "===== DANH SACH SAN PHAM ====="
        );

        for (Product product : products) {

            System.out.println(product);

            total +=
                    product.inventoryValue();
        }

        System.out.printf(
                "Tong gia tri ton kho: %,.0f VND%n",
                total
        );

        // =========================
        // GHI REPORT
        // =========================

        try (
                BufferedWriter writer =
                        Files.newBufferedWriter(
                                report,
                                StandardCharsets.UTF_8
                        )
        ) {

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

            System.out.println(
                    "Da ghi bao cao: "
                            + report.toAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Khong ghi duoc bao cao: "
                            + e.getMessage()
            );
        }
    }
}