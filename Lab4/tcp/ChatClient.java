
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    public static void main(String[] args) {

        String host = args.length >= 1
                ? args[0] : "localhost";

        String nickname = args.length >= 2
                ? args[1] : "";

        int port = 5006;

        if (nickname.isEmpty()) {
            System.out.println(
                    "Cach dung: ChatClient <host> <nickname>"
            );
            return;
        }

        try (
            Socket socket = new Socket(host, port);

            BufferedReader console = new BufferedReader(
                new InputStreamReader(
                    System.in,
                    StandardCharsets.UTF_8
                )
            );

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                    socket.getOutputStream(),
                    StandardCharsets.UTF_8
                ),
                true
            )
        ) {
            // Đăng ký nickname.
            out.println("HELLO " + nickname);

            // Đọc phản hồi đăng ký trước khi bắt đầu chat.
            String response = in.readLine();

            if (response == null
                    || !response.startsWith("OK HELLO ")) {
                System.out.println(
                        "Dang ky that bai: " + response
                );
                return;
            }

            System.out.println(response);
            System.out.println("Da ket noi chat!");
            System.out.println("Lenh:");
            System.out.println("  USERS");
            System.out.println("  MSG <noi dung>");
            System.out.println("  QUIT");

            // Luồng riêng nhận tin nhắn từ server.
            Thread receiver = new Thread(() -> {
                try {
                    String message;

                    while ((message = in.readLine()) != null) {
                        System.out.println();
                        System.out.println(message);
                        System.out.print("> ");
                    }

                    System.out.println(
                            "\nServer da dong ket noi."
                    );

                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        System.out.println(
                                "Loi nhan tin: " + e.getMessage()
                        );
                    }
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            // Luồng chính đọc lệnh từ bàn phím.
            String command;

            while ((command = console.readLine()) != null) {

                out.println(command);

                if (command.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }

                System.out.print("> ");
            }

        } catch (IOException e) {
            System.err.println(
                    "Khong ket noi duoc server: "
                            + e.getMessage()
            );
        }
    }
}