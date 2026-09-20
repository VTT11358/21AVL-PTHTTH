package Lab4.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitServer {

    private static final int PORT = 5002;

    public static void main(String[] args) {

        try (ServerSocket server =
                     new ServerSocket(PORT)) {

            System.out.println(
                    "Digit Server running on port "
                            + PORT
            );

            while (true) {

                try (Socket socket =
                             server.accept()) {

                    handleClient(socket);

                } catch (IOException e) {

                    System.err.println(
                            "Lỗi client: "
                                    + e.getMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Không thể mở server: "
                            + e.getMessage()
            );
        }
    }

    private static void handleClient(
            Socket socket) throws IOException {

        try (
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream(),
                                        StandardCharsets.UTF_8
                                )
                        );

                PrintWriter out =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream(),
                                        StandardCharsets.UTF_8
                                ),
                                true
                        )
        ) {

            String input;

            while ((input = in.readLine()) != null) {

                if (input.equalsIgnoreCase("QUIT")) {

                    out.println("OK BYE");
                    break;
                }

                out.println(convertDigit(input));
            }
        }
    }

    private static String convertDigit(
            String input) {

        if (input.length() != 1) {
            return "ERR INVALID_DIGIT";
        }

        switch (input.charAt(0)) {

            case '0':
                return "OK KHÔNG";

            case '1':
                return "OK MỘT";

            case '2':
                return "OK HAI";

            case '3':
                return "OK BA";

            case '4':
                return "OK BỐN";

            case '5':
                return "OK NĂM";

            case '6':
                return "OK SÁU";

            case '7':
                return "OK BẢY";

            case '8':
                return "OK TÁM";

            case '9':
                return "OK CHÍN";

            default:
                return "ERR INVALID_DIGIT";
        }
    }
}