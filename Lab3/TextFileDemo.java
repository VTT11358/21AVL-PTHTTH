package Lab3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileDemo {

    public static void main(String[] args) {

        Path file = Path.of(
                "data",
                "ghi_chu.txt"
        );

        try {

            // Tao thu muc data neu chua co
            Files.createDirectories(
                    file.getParent()
            );

            // =========================
            // GHI FILE
            // =========================

            try (BufferedWriter writer =
                         Files.newBufferedWriter(
                                 file,
                                 StandardCharsets.UTF_8
                         )) {

                writer.write(
                        "Java I/O lam viec voi cac luong du lieu."
                );

                writer.newLine();

                writer.write(
                        "BufferedWriter giup ghi van ban hieu qua."
                );

                writer.newLine();

                writer.write(
                        "UTF-8 ho tro tieng Viet on dinh."
                );
            }

            // =========================
            // DOC FILE
            // =========================

            try (BufferedReader reader =
                         Files.newBufferedReader(
                                 file,
                                 StandardCharsets.UTF_8
                         )) {

                String line;
                int number = 1;

                while ((line = reader.readLine()) != null) {

                    System.out.printf(
                            "%d. %s%n",
                            number,
                            line
                    );

                    number++;
                }
            }

            System.out.println();
            System.out.println(
                    "Duong dan file:"
            );

            System.out.println(
                    file.toAbsolutePath()
            );

        } catch (IOException e) {

            System.err.println(
                    "Loi xu ly tep "
                            + file
                            + ": "
                            + e.getMessage()
            );
        }
    }
}