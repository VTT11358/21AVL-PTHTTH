
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ChatServer {

    private static final int PORT = 5006;
    private static final int MAX_CLIENTS = 20;

    // Quản lý nickname và client an toàn giữa nhiều thread.
    private static final ConcurrentMap<String, ClientHandler>
            clients = new ConcurrentHashMap<>();

    private static final Pattern NICKNAME_PATTERN =
            Pattern.compile("[a-zA-Z0-9_-]{1,20}");

    public static void main(String[] args) {

        ExecutorService pool =
                Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                    "Chat Server dang chay tai port " + PORT
            );

            while (true) {
                Socket socket = server.accept();

                System.out.println(
                        "Client ket noi: "
                                + socket.getRemoteSocketAddress()
                );

                // Mỗi client được xử lý bởi thread trong pool.
                pool.submit(new ClientHandler(socket));
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi server: " + e.getMessage()
            );
        } finally {
            pool.shutdown();
        }
    }

    // Gửi tin nhắn đến tất cả client NGOẠI TRỪ người gửi.
    private static void broadcast(
            ClientHandler sender, String message) {

        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.send(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private PrintWriter out;
        private String nickname;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        // Đồng bộ thao tác gửi để tránh các thread
        // ghi đồng thời lên cùng một kết nối.
        private synchronized void send(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        @Override
        public void run() {

            try (
                Socket clientSocket = socket;

                BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                );

                PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(
                        clientSocket.getOutputStream(),
                        StandardCharsets.UTF_8
                    ),
                    true
                )
            ) {
                out = writer;

                // Bắt buộc đăng ký nickname trước khi chat.
                String firstLine = in.readLine();

                if (firstLine == null) {
                    return;
                }

                String[] hello = firstLine.trim().split("\\s+");

                if (hello.length != 2
                        || !hello[0].equalsIgnoreCase("HELLO")) {
                    send("ERR EXPECT_HELLO");
                    return;
                }

                nickname = hello[1];

                if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
                    send("ERR INVALID_NICKNAME");
                    return;
                }

                // putIfAbsent giúp nickname không bị trùng
                // khi nhiều client đăng ký gần như đồng thời.
                ClientHandler existing =
                        clients.putIfAbsent(nickname, this);

                if (existing != null) {
                    send("ERR NICKNAME_TAKEN");
                    return;
                }

                send("OK HELLO " + nickname);

                System.out.println(
                        nickname + " da tham gia chat."
                );

                broadcast(
                        this,
                        "NOTICE " + nickname + " da tham gia."
                );

                String request;

                while ((request = in.readLine()) != null) {

                    String trimmed = request.trim();

                    if (trimmed.equalsIgnoreCase("USERS")) {

                        String userList = String.join(
                                ", ", clients.keySet()
                        );

                        send("USERS " + userList);

                    } else if (
                            trimmed.equalsIgnoreCase("QUIT")) {

                        send("OK BYE");
                        break;

                    } else if (
                            trimmed.regionMatches(
                                    true, 0, "MSG ", 0, 4)) {

                        String message = request.substring(4).trim();

                        if (message.isEmpty()) {
                            send("ERR EMPTY_MESSAGE");
                            continue;
                        }

                        broadcast(
                                this,
                                "MSG [" + nickname + "] " + message
                        );

                        send("OK SENT");

                    } else {
                        send("ERR UNKNOWN_COMMAND");
                    }
                }

            } catch (IOException e) {
                System.err.println(
                        "Client "
                                + (nickname == null
                                    ? socket.getRemoteSocketAddress()
                                    : nickname)
                                + " ngat ket noi."
                );

            } finally {

                // Xóa client khi QUIT hoặc mất kết nối.
                if (nickname != null
                        && clients.remove(nickname, this)) {

                    broadcast(
                            this,
                            "NOTICE " + nickname + " da roi chat."
                    );

                    System.out.println(
                            nickname + " da roi chat."
                    );
                }
            }
        }
    }
}