
package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpEchoServer {

    private static final int PORT = 5010;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                    "TCP Echo Server dang chay tai port " + PORT
            );

            while (true) {
                Socket socket = server.accept();

                System.out.println(
                        "TCP client: " + socket.getRemoteSocketAddress()
                );

                // Xử lý kết nối hiện tại.
                // Benchmark chỉ cần một client tại một thời điểm.
                handleClient(socket);
            }

        } catch (IOException e) {
            System.err.println(
                    "TCP Server error: " + e.getMessage()
            );
        }
    }

    private static void handleClient(Socket socket) {

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
            String message;

            while ((message = in.readLine()) != null) {
                // Echo: trả lại đúng nội dung đã nhận.
                out.println(message);
            }

        } catch (IOException e) {
            System.err.println(
                    "TCP client disconnected: " + e.getMessage()
            );
        }
    }
}