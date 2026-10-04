package Lab3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BinaryFileCopy {

    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {

        // Phai co 2 tham so:
        // 1. File nguon
        // 2. File dich

        if (args.length != 2) {

            System.out.println(
                    "Cach dung:"
            );

            System.out.println(
                    "java BinaryFileCopy <nguon> <dich>"
            );

            return;
        }

        Path source = Path.of(args[0]);
        Path target = Path.of(args[1]);

        long totalBytes = 0;

        try {

            // Kiem tra file nguon
            if (!Files.exists(source)) {

                System.err.println(
                        "Khong tim thay file nguon: "
                                + source
                );

                return;
            }

            // Tao thu muc dich neu can
            if (target.getParent() != null) {

                Files.createDirectories(
                        target.getParent()
                );
            }

            try (
                    InputStream input =
                            new BufferedInputStream(
                                    Files.newInputStream(source)
                            );

                    OutputStream output =
                            new BufferedOutputStream(
                                    Files.newOutputStream(target)
                            )
            ) {

                byte[] buffer =
                        new byte[BUFFER_SIZE];

                int bytesRead;

                while (
                        (bytesRead =
                                input.read(buffer)) != -1
                ) {

                    output.write(
                            buffer,
                            0,
                            bytesRead
                    );

                    totalBytes += bytesRead;
                }
            }

            System.out.println(
                    "Da sao chep "
                            + totalBytes
                            + " byte."
            );

            System.out.println(
                    "Kich thuoc file nguon: "
                            + Files.size(source)
                            + " byte"
            );

            System.out.println(
                    "Kich thuoc file dich: "
                            + Files.size(target)
                            + " byte"
            );

        } catch (IOException e) {

            System.err.println(
                    "Sao chep that bai: "
                            + e.getMessage()
            );
        }
    }
}
