package Lab4.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTransferServer {

    private static final int PORT = 5013;
    private static final long MAX_FILE_SIZE = 1024L * 1024 * 1024;

    private static final Path UPLOAD_DIR =
            Paths.get("upload").toAbsolutePath().normalize();

    public static void main(String[] args) throws IOException {
        Files.createDirectories(UPLOAD_DIR);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("File Transfer Server đang chạy tại port " + PORT);
            System.out.println("Thư mục lưu file: " + UPLOAD_DIR);

            while (true) {
                Socket client = server.accept();
                pool.submit(() -> handleClient(client));
            }
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        Path tempFile = null;

        try (Socket client = socket;
             DataInputStream in =
                     new DataInputStream(client.getInputStream());
             DataOutputStream out =
                     new DataOutputStream(client.getOutputStream())) {

            System.out.println("Client kết nối: " + client.getRemoteSocketAddress());

            // 1. Nhận metadata
            String fileName = in.readUTF();
            long fileSize = in.readLong();
            String expectedHash = in.readUTF();

            // 2. Kiểm tra tên file
            if (!isSafeFileName(fileName)) {
                out.writeUTF("ERR INVALID_FILENAME");
                out.flush();
                return;
            }

            // 3. Kiểm tra kích thước
            if (fileSize < 0 || fileSize > MAX_FILE_SIZE) {
                out.writeUTF("ERR INVALID_FILE_SIZE");
                out.flush();
                return;
            }

            if (!expectedHash.matches("[a-fA-F0-9]{64}")) {
                out.writeUTF("ERR INVALID_HASH");
                out.flush();
                return;
            }

            Path destination = UPLOAD_DIR.resolve(fileName).normalize();

            if (!destination.getParent().equals(UPLOAD_DIR)) {
                out.writeUTF("ERR INVALID_FILENAME");
                out.flush();
                return;
            }

            // Dùng file tạm để tránh lưu file chưa được xác minh.
            tempFile = Files.createTempFile(UPLOAD_DIR, "upload-", ".tmp");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 4. Đọc chính xác số byte được khai báo.
            try (OutputStream fileOut = Files.newOutputStream(tempFile)) {
                byte[] buffer = new byte[8192];
                long remaining = fileSize;

                while (remaining > 0) {
                    int bytesToRead = (int) Math.min(buffer.length, remaining);
                    int count = in.read(buffer, 0, bytesToRead);

                    if (count == -1) {
                        throw new IOException("Client ngắt kết nối khi đang gửi file");
                    }

                    fileOut.write(buffer, 0, count);
                    digest.update(buffer, 0, count);
                    remaining -= count;
                }
            }

            // 5. So sánh SHA-256
            String actualHash = toHex(digest.digest());

            if (!actualHash.equalsIgnoreCase(expectedHash)) {
                Files.deleteIfExists(tempFile);
                tempFile = null;

                out.writeUTF("ERR HASH_MISMATCH");
                out.flush();

                System.out.println("SHA-256 không khớp: " + fileName);
                return;
            }

            // 6. Chỉ đưa file đã xác minh vào thư mục upload.
            Files.move(
                    tempFile,
                    destination,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            tempFile = null;

            out.writeUTF("OK " + fileName);
            out.flush();

            System.out.println("Nhận file thành công: " + fileName);
            System.out.println("Kích thước: " + fileSize + " bytes");
            System.out.println("SHA-256: " + actualHash);

        } catch (Exception e) {
            System.err.println("Lỗi xử lý client: " + e.getMessage());

        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ignored) {
                    // Bỏ qua lỗi xóa file tạm.
                }
            }
        }
    }

    private static boolean isSafeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        // Chỉ cho phép tên file đơn, không nhận đường dẫn.
        if (fileName.contains("..")
                || fileName.contains("/")
                || fileName.contains("\\")
                || fileName.contains(":")) {
            return false;
        }

        return Paths.get(fileName).getFileName().toString().equals(fileName);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(String.format("%02x", b & 0xff));
        }

        return result.toString();
    }
}