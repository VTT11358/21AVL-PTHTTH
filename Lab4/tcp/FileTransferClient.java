package Lab4.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class FileTransferClient {

    private static final int PORT = 5013;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    "Cách dùng: java -cp bin Lab4.tcp.FileTransferClient <host> <filePath>"
            );
            return;
        }

        String host = args[0];
        Path file = Paths.get(args[1]).toAbsolutePath().normalize();

        if (!Files.isRegularFile(file)) {
            System.err.println("Không tìm thấy file: " + file);
            return;
        }

        try {
            String fileName = file.getFileName().toString();
            long fileSize = Files.size(file);
            String sha256 = calculateSHA256(file);

            System.out.println("File: " + fileName);
            System.out.println("Kích thước: " + fileSize + " bytes");
            System.out.println("SHA-256: " + sha256);

            try (Socket socket = new Socket(host, PORT);
                 DataOutputStream out =
                         new DataOutputStream(socket.getOutputStream());
                 DataInputStream in =
                         new DataInputStream(socket.getInputStream());
                 InputStream fileIn = Files.newInputStream(file)) {

                // 1. Gửi metadata trước.
                out.writeUTF(fileName);
                out.writeLong(fileSize);
                out.writeUTF(sha256);
                out.flush();

                // 2. Gửi dữ liệu file dưới dạng byte.
                byte[] buffer = new byte[8192];
                int count;

                while ((count = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }

                out.flush();

                // 3. Nhận kết quả từ server.
                String response = in.readUTF();
                System.out.println("Phản hồi server: " + response);
            }

        } catch (Exception e) {
            System.err.println("Lỗi gửi file: " + e.getMessage());
        }
    }

    private static String calculateSHA256(Path file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream in = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int count;

            while ((count = in.read(buffer)) != -1) {
                digest.update(buffer, 0, count);
            }
        }

        StringBuilder result = new StringBuilder();

        for (byte b : digest.digest()) {
            result.append(String.format("%02x", b & 0xff));
        }

        return result.toString();
    }
}