
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageLogClient {

    private static final int PORT = 5012;

    public static void main(String[] args) {

        String host = args.length >= 1
                ? args[0] : "localhost";

        String clientId = args.length >= 2
                ? args[1] : "";

        if (clientId.isEmpty()) {
            System.out.println(
                    "Cach dung: MessageLogClient <host> <clientId>"
            );
            System.out.println(
                    "Vi du: MessageLogClient localhost Tai"
            );
            return;
        }

        try (
            Socket socket = new Socket(host, PORT);

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
            // Bước 1: gửi định danh client.
            out.println("HELLO " + clientId);

            String response = in.readLine();

            if (response == null
                    || !response.startsWith("OK HELLO ")) {
                System.out.println(
                        "Dang ky that bai: " + response
                );
                return;
            }

            System.out.println("Server: " + response);
            System.out.println("Nhap noi dung can ghi log.");
            System.out.println("Nhap QUIT de thoat.");

            // Bước 2: gửi nội dung từng dòng.
            while (true) {

                System.out.print("Tin nhan: ");

                String message = console.readLine();

                if (message == null) {
                    break;
                }

                out.println(message);

                response = in.readLine();

                if (response == null) {
                    System.out.println(
                            "Server da dong ket noi."
                    );
                    break;
                }

                System.out.println("Server: " + response);

                if (message.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi ket noi: " + e.getMessage()
            );
        }
    }
}