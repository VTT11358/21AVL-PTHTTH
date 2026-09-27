
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class MessageLogServer {

    private static final int PORT = 5012;
    private static final int MAX_CLIENTS = 20;

    // Thư mục log được cố định ở thư mục chạy chương trình.
    private static final Path LOG_DIR =
            Paths.get("data", "logs");

    // Chỉ chấp nhận chữ, số, dấu - và dấu _.
    private static final Pattern CLIENT_ID_PATTERN =
            Pattern.compile("[a-zA-Z0-9_-]{1,30}");

    // Tránh ghi đồng thời vào cùng file khi trùng clientId.
    private static final ConcurrentMap<String, Object> FILE_LOCKS =
            new ConcurrentHashMap<>();

    public static void main(String[] args) {

        ExecutorService pool =
                Executors.newFixedThreadPool(MAX_CLIENTS);

        try {
            Files.createDirectories(LOG_DIR);

            try (ServerSocket server = new ServerSocket(PORT)) {

                System.out.println(
                        "Message Log Server dang chay tai port "
                                + PORT
                );

                System.out.println(
                        "Log directory: "
                                + LOG_DIR.toAbsolutePath()
                );

                while (true) {
                    Socket socket = server.accept();

                    pool.submit(() -> handleClient(socket));
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Server error: " + e.getMessage()
            );
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {

        String clientId = null;

        try (
            Socket client = socket;

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    client.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                    client.getOutputStream(),
                    StandardCharsets.UTF_8
                ),
                true
            )
        ) {
            // Bước 1: yêu cầu HELLO clientId.
            String firstLine = in.readLine();

            if (firstLine == null) {
                return;
            }

            String[] parts = firstLine.trim().split("\\s+");

            if (parts.length != 2
                    || !parts[0].equalsIgnoreCase("HELLO")) {
                out.println("ERR EXPECT_HELLO");
                return;
            }

            clientId = parts[1];

            if (!CLIENT_ID_PATTERN.matcher(clientId).matches()) {
                out.println("ERR INVALID_CLIENT_ID");
                return;
            }

            Path logFile = LOG_DIR.resolve(clientId + ".txt");

            out.println("OK HELLO " + clientId);

            System.out.println(
                    clientId + " connected from "
                            + client.getRemoteSocketAddress()
            );

            // Bước 2: nhận nội dung cho đến khi QUIT.
            String message;

            while ((message = in.readLine()) != null) {

                if (message.trim().equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                String timestamp = OffsetDateTime.now().format(
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                );

                String remoteAddress =
                        String.valueOf(client.getRemoteSocketAddress());

                String logLine = timestamp
                        + " | " + remoteAddress
                        + " | " + message;

                Object lock = FILE_LOCKS.computeIfAbsent(
                        clientId, id -> new Object()
                );

                // Ghi nối tiếp, không ghi đè log cũ.
                synchronized (lock) {
                    Files.writeString(
                            logFile,
                            logLine + System.lineSeparator(),
                            StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.WRITE,
                            StandardOpenOption.APPEND
                    );
                }

                out.println("OK LOGGED");
            }

        } catch (IOException e) {
            System.err.println(
                    "Client "
                            + (clientId == null ? "unknown" : clientId)
                            + " disconnected: " + e.getMessage()
            );
        }
    }
}